package me.zhenxin.zmusic.notice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoticeReadStoreTest {

    @TempDir
    Path dataFolder;

    @Test
    void persistsReadNoticeForEachPlayer() throws IOException {
        Path file = dataFolder.resolve("notice-read.json");
        NoticeReadStore store = new NoticeReadStore(file.toFile());

        assertTrue(store.markRead("player-1", "notice-1"));
        assertFalse(store.markRead("player-1", "notice-1"));
        assertTrue(store.isRead("player-1", "notice-1"));
        assertFalse(store.isRead("player-2", "notice-1"));

        NoticeReadStore reloaded = new NoticeReadStore(file.toFile());
        assertTrue(reloaded.isRead("player-1", "notice-1"));
        assertFalse(reloaded.isRead("player-1", "notice-2"));
    }
}
