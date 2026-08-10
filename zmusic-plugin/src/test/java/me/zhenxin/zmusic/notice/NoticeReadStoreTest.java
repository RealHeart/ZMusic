package me.zhenxin.zmusic.notice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoticeReadStoreTest {

    @TempDir
    Path dataFolder;

    @Test
    void persistsOnlyLastReadNoticeForEachPlayer() throws IOException {
        Path file = dataFolder.resolve("notice-read.json");
        NoticeReadStore store = new NoticeReadStore(file.toFile());

        assertTrue(store.markRead("player-1", "notice-1"));
        assertFalse(store.markRead("player-1", "notice-1"));
        assertTrue(store.markRead("player-1", "notice-2"));
        assertFalse(store.isRead("player-1", "notice-1"));
        assertTrue(store.isRead("player-1", "notice-2"));
        assertFalse(store.isRead("player-2", "notice-1"));

        NoticeReadStore reloaded = new NoticeReadStore(file.toFile());
        assertTrue(reloaded.isRead("player-1", "notice-2"));
        assertEquals("{\"player-1\":\"notice-2\"}",
                new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
    }

    @Test
    void loadsLegacyArraysAndMigratesThemOnNextSave() throws IOException {
        Path file = dataFolder.resolve("notice-read.json");
        Files.write(file, "{\"player-1\":[\"notice-1\",\"notice-2\"]}"
                .getBytes(StandardCharsets.UTF_8));

        NoticeReadStore store = new NoticeReadStore(file.toFile());

        assertFalse(store.isRead("player-1", "notice-1"));
        assertTrue(store.isRead("player-1", "notice-2"));
        assertTrue(store.markRead("player-2", "notice-3"));
        assertEquals("{\"player-1\":\"notice-2\",\"player-2\":\"notice-3\"}",
                new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
    }
}
