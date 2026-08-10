package me.zhenxin.zmusic.notice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

/**
 * 拉取公告并向玩家发送尚未读过的内容。
 */
public class NoticeService {

    private static final String NOTICE_API = "https://api.zhenxin.me/zmusic/notices";
    private static final Gson GSON = new Gson();

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
            ZMusic.message.sendJsonMessage(createMessage(currentNotice), player);
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

    private ZComponent createMessage(Notice notice) {
        ZTextComponent message = ZTextComponent.of(Config.prefix + "§6[公告] §e" + notice.getTitle()
                + "\n§f" + notice.getContent() + "\n");
        ZTextComponent readButton = ZTextComponent.of("§a[点击标记为已读]");
        readButton.setClickEvent(ZClickEvent.runCommand("/zm notice read " + notice.getId()));
        readButton.setHoverEvent(ZHoverEvent.showText("§7点击后将不再接收此条公告"));
        message.addChild(readButton);
        return message;
    }

    static Notice parseCurrentNotice(String response) {
        try {
            JsonArray array = GSON.fromJson(response, JsonArray.class);
            if (array == null) {
                throw new IllegalArgumentException("响应为空");
            }
            if (array.size() == 0) {
                return null;
            }
            JsonElement element = array.get(0);
            if (!element.isJsonObject()) {
                throw new IllegalArgumentException("公告不是 JSON 对象");
            }
            JsonObject json = element.getAsJsonObject();
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
            throw new IllegalArgumentException("响应不是公告数组", e);
        }
    }

    private static String requiredString(JsonObject json, String name) {
        if (!json.has(name) || json.get(name).isJsonNull() || !json.get(name).isJsonPrimitive()
                || !json.get(name).getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("公告缺少字符串字段: " + name);
        }
        return json.get(name).getAsString();
    }

    public enum MarkReadResult {
        MARKED,
        ALREADY_READ,
        NOT_FOUND,
        FAILED
    }
}
