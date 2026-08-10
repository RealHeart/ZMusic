package me.zhenxin.zmusic.notice;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoticeServiceTest {

    @Test
    void parsesPublishedNoticesFromApiResponse() {
        String response = "[{\"id\":\"notice-1\",\"title\":\"维护公告\","
                + "\"content\":\"今晚维护\",\"created_at\":\"2026-08-10T00:00:00Z\"}]";

        List<Notice> notices = NoticeService.parseNotices(response);

        assertEquals(1, notices.size());
        assertEquals("notice-1", notices.get(0).getId());
        assertEquals("维护公告", notices.get(0).getTitle());
        assertEquals("今晚维护", notices.get(0).getContent());
    }

    @Test
    void rejectsNoticeWithoutRequiredFields() {
        assertThrows(IllegalArgumentException.class,
                () -> NoticeService.parseNotices("[{\"id\":\"notice-1\",\"title\":\"维护公告\"}]"));
    }
}
