package me.zhenxin.zmusic.notice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoticeServiceTest {

    @Test
    void parsesCurrentNoticeFromApiResponse() {
        String response = "{\"code\":200,\"msg\":\"success\",\"data\":{"
                + "\"id\":\"notice-1\",\"title\":\"维护公告\","
                + "\"content\":\"今晚维护\",\"createdAt\":\"2026-08-10T00:00:00Z\"}}";

        Notice notice = NoticeService.parseCurrentNotice(response);

        assertEquals("notice-1", notice.getId());
        assertEquals("维护公告", notice.getTitle());
        assertEquals("今晚维护", notice.getContent());
    }

    @Test
    void parsesEmptyResponseAsNoCurrentNotice() {
        assertNull(NoticeService.parseCurrentNotice("{\"code\":200,\"msg\":\"success\",\"data\":null}"));
    }

    @Test
    void rejectsNoticeWithoutRequiredFields() {
        assertThrows(IllegalArgumentException.class,
                () -> NoticeService.parseCurrentNotice("{\"code\":200,\"msg\":\"success\","
                        + "\"data\":{\"id\":\"notice-1\",\"title\":\"维护公告\"}}"));
    }
}
