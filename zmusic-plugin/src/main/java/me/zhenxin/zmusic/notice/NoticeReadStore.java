package me.zhenxin.zmusic.notice;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 按玩家 UUID 持久化已读公告，避免玩家改名后重复收到公告。
 */
class NoticeReadStore {

    private static final Gson GSON = new Gson();

    private final File file;
    private final Map<String, String> readNotices = new LinkedHashMap<>();

    NoticeReadStore(File file) {
        this.file = file;
        load();
    }

    synchronized boolean isRead(String playerId, String noticeId) {
        return noticeId.equals(readNotices.get(playerId));
    }

    synchronized boolean markRead(String playerId, String noticeId) throws IOException {
        String previousNoticeId = readNotices.put(playerId, noticeId);
        if (noticeId.equals(previousNoticeId)) {
            return false;
        }
        try {
            save();
        } catch (IOException e) {
            if (previousNoticeId == null) {
                readNotices.remove(playerId);
            } else {
                readNotices.put(playerId, previousNoticeId);
            }
            throw e;
        }
        return true;
    }

    private void load() {
        if (!file.isFile()) {
            return;
        }
        try {
            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JsonObject root = GSON.fromJson(json, JsonObject.class);
            if (root == null) {
                return;
            }
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                JsonElement value = entry.getValue();
                String noticeId;
                if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                    noticeId = value.getAsString();
                } else if (value.isJsonArray()) {
                    noticeId = null;
                    for (JsonElement element : value.getAsJsonArray()) {
                        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                            throw new IllegalStateException("公告 ID 不是字符串: " + entry.getKey());
                        }
                        noticeId = element.getAsString();
                    }
                } else {
                    throw new IllegalStateException("玩家已读记录格式无效: " + entry.getKey());
                }
                if (noticeId != null && !noticeId.isEmpty()) {
                    readNotices.put(entry.getKey(), noticeId);
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("无法读取公告已读记录: " + file.getPath(), e);
        }
    }

    private void save() throws IOException {
        File parent = file.getParentFile();
        if (parent != null) {
            Files.createDirectories(parent.toPath());
        }

        JsonObject root = new JsonObject();
        for (Map.Entry<String, String> entry : readNotices.entrySet()) {
            root.addProperty(entry.getKey(), entry.getValue());
        }

        File temporary = new File(file.getPath() + ".tmp");
        Files.write(temporary.toPath(), GSON.toJson(root).getBytes(StandardCharsets.UTF_8));
        try {
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
