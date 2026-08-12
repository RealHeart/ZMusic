package me.zhenxin.zmusic.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayListTest {

    @Test
    void parsesNeteasePlaylistIdFromDesktopLink() {
        assertEquals("363046232", PlayList.parseNeteasePlaylistId(
                "https://music.163.com/playlist?id=363046232"));
    }

    @Test
    void parsesNeteasePlaylistIdFromMobileShareLink() {
        assertEquals("363046232", PlayList.parseNeteasePlaylistId(
                "https://y.music.163.com/m/playlist?app_version=9.3.00&id=363046232&userid=265857414"));
    }

    @Test
    void parsesNeteasePlaylistIdFromFragmentLink() {
        assertEquals("363046232", PlayList.parseNeteasePlaylistId(
                "https://music.163.com/#/my/m/music/playlist?id=363046232"));
    }

    @Test
    void rejectsLinkWithoutPlaylistId() {
        assertThrows(IllegalArgumentException.class,
                () -> PlayList.parseNeteasePlaylistId("https://music.163.com/playlist"));
    }
}
