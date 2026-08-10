package me.zhenxin.zmusic.notice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 按玩家 UUID 持久化已读公告，避免玩家改名后重复收到公告。
 */
class NoticeReadStore {

    private static final Gson GSON = new Gson();

    private final File file;
    private final Map<String, Set<String>> readNotices = new LinkedHashMap<>();

    NoticeReadStore(File file) {
        this.file = file;
        load();
    }

    synchronized boolean isRead(String playerId, String noticeId) {
        Set<String> noticeIds = readNotices.get(playerId);
        return noticeIds != null && noticeIds.contains(noticeId);
    }

    synchronized boolean markRead(String playerId, String noticeId) throws IOException {
        Set<String> noticeIds = readNotices.computeIfAbsent(playerId, ignored -> new LinkedHashSet<>());
        if (!noticeIds.add(noticeId)) {
            return false;
        }
        try {
            save();
        } catch (IOException e) {
            noticeIds.remove(noticeId);
            if (noticeIds.isEmpty()) {
                readNotices.remove(playerId);
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
                if (!entry.getValue().isJsonArray()) {
                    throw new IllegalStateException("玩家已读记录不是数组: " + entry.getKey());
                }
                Set<String> noticeIds = new LinkedHashSet<>();
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                        throw new IllegalStateException("公告 ID 不是字符串: " + entry.getKey());
                    }
                    noticeIds.add(element.getAsString());
                }
                readNotices.put(entry.getKey(), noticeIds);
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
        for (Map.Entry<String, Set<String>> entry : readNotices.entrySet()) {
            JsonArray noticeIds = new JsonArray();
            for (String noticeId : entry.getValue()) {
                noticeIds.add(noticeId);
            }
            root.add(entry.getKey(), noticeIds);
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
