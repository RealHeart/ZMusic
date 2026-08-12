package me.zhenxin.zmusic.notice;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.component.ZClickEvent;
import me.zhenxin.zmusic.component.ZComponent;
import me.zhenxin.zmusic.component.ZHoverEvent;
import me.zhenxin.zmusic.component.ZTextComponent;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拉取公告并向玩家发送尚未读过的内容。
 */
public class NoticeService {

    private static final String NOTICE_API = "https://api.zhenxin.me/zmusic/notice";
    private static final Gson GSON = new Gson();
    private static final Pattern URL_PATTERN = Pattern.compile(
            "https?://[^\\s<>\\[\\](){}，。；：！？、]+", Pattern.CASE_INSENSITIVE);

    private final NoticeReadStore readStore;
    private volatile Notice notice;

    public NoticeService(File dataFolder) {
        this.readStore = new NoticeReadStore(new File(dataFolder, "notice-read.json"));
    }

    /**
     * 刷新公告缓存。接口不可用时保留上一次成功取得的公告。
     */
    public void refresh() {
        String response = NetUtils.getNetString(NOTICE_API, null);
        if (response == null) {
            ZMusic.log.sendDebugMessage("[公告] 公告接口无响应");
            return;
        }
        try {
            notice = parseCurrentNotice(response);
        } catch (IllegalArgumentException e) {
            ZMusic.log.sendDebugMessage("[公告] 无法解析公告接口响应: " + e.getMessage());
        }
    }

    public void sendUnread(Object player) {
        Notice currentNotice = notice;
        if (currentNotice == null) {
            return;
        }
        String playerId = ZMusic.player.getUniqueId(player);
        if (!readStore.isRead(playerId, currentNotice.getId())) {
            for (ZComponent message : createMessages(currentNotice)) {
                ZMusic.message.sendJsonMessage(message, player);
            }
        }
    }

    public MarkReadResult markRead(Object player, String noticeId) {
        Notice notice = findNotice(noticeId);
        if (notice == null) {
            return MarkReadResult.NOT_FOUND;
        }
        String playerId = ZMusic.player.getUniqueId(player);
        try {
            if (!readStore.markRead(playerId, noticeId)) {
                return MarkReadResult.ALREADY_READ;
            }
            return MarkReadResult.MARKED;
        } catch (IOException e) {
            ZMusic.log.sendErrorMessage("保存公告已读状态失败: " + e.getMessage());
            return MarkReadResult.FAILED;
        }
    }

    private Notice findNotice(String noticeId) {
        Notice currentNotice = notice;
        if (currentNotice != null && currentNotice.getId().equals(noticeId)) {
            return currentNotice;
        }
        return null;
    }

    static List<ZComponent> createMessages(Notice notice) {
        List<ZComponent> messages = new ArrayList<>();
        messages.add(ZTextComponent.of(Config.prefix + "§a[公告] " + notice.getTitle()));
        for (String line : notice.getContent().split("\\r?\\n", -1)) {
            messages.add(createContentLine(line));
        }

        ZTextComponent readMessage = ZTextComponent.of(Config.prefix);
        ZTextComponent readButton = ZTextComponent.of("§e[点击标记为已读]");
        readButton.setClickEvent(ZClickEvent.runCommand("/zm notice read " + notice.getId()));
        readButton.setHoverEvent(ZHoverEvent.showText("§7点击后将不再接收此条公告"));
        readMessage.addChild(readButton);
        messages.add(readMessage);
        return messages;
    }

    private static ZComponent createContentLine(String line) {
        ZTextComponent message = ZTextComponent.of(Config.prefix);
        Matcher matcher = URL_PATTERN.matcher(line);
        int start = 0;
        while (matcher.find()) {
            if (matcher.start() > start) {
                message.addChild(ZTextComponent.of("§a" + line.substring(start, matcher.start())));
            }
            ZTextComponent openButton = ZTextComponent.of("§e[点击打开]");
            openButton.setClickEvent(ZClickEvent.openUrl(matcher.group()));
            openButton.setHoverEvent(ZHoverEvent.showText("§7点击打开链接"));
            message.addChild(openButton);
            start = matcher.end();
        }
        if (start < line.length()) {
            message.addChild(ZTextComponent.of("§a" + line.substring(start)));
        }
        if (message.getChildren().isEmpty()) {
            message.addChild(ZTextComponent.of("§a"));
        }
        return message;
    }

    static Notice parseCurrentNotice(String response) {
        try {
            JsonElement root = GSON.fromJson(response, JsonElement.class);
            if (root == null || root.isJsonNull()) {
                throw new IllegalArgumentException("响应为空");
            }
            if (!root.isJsonObject()) {
                throw new IllegalArgumentException("响应不是 JSON 对象");
            }
            JsonObject envelope = root.getAsJsonObject();
            int code = requiredInt(envelope, "code");
            if (code != 200) {
                throw new IllegalArgumentException("公告接口返回失败: " + code);
            }
            if (!envelope.has("data")) {
                throw new IllegalArgumentException("公告接口缺少 data 字段");
            }
            JsonElement data = envelope.get("data");
            if (data == null || data.isJsonNull()) {
                return null;
            }
            if (!data.isJsonObject()) {
                throw new IllegalArgumentException("公告 data 不是 JSON 对象");
            }
            JsonObject json = data.getAsJsonObject();
            String id = requiredString(json, "id");
            String title = requiredString(json, "title");
            String content = requiredString(json, "content");
            if (id.isEmpty()) {
                throw new IllegalArgumentException("公告 id 不能为空");
            }
            return new Notice(id, title, content);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("响应不是公告对象", e);
        }
    }

    private static String requiredString(JsonObject json, String name) {
        if (!json.has(name) || json.get(name).isJsonNull() || !json.get(name).isJsonPrimitive()
                || !json.get(name).getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("公告缺少字符串字段: " + name);
        }
        return json.get(name).getAsString();
    }

    private static int requiredInt(JsonObject json, String name) {
        if (!json.has(name) || json.get(name).isJsonNull() || !json.get(name).isJsonPrimitive()
                || !json.get(name).getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException("公告接口缺少数字字段: " + name);
        }
        return json.get(name).getAsInt();
    }

    public enum MarkReadResult {
        MARKED,
        ALREADY_READ,
        NOT_FOUND,
        FAILED
    }
}
