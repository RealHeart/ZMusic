package me.zhenxin.zmusic.notice;

import me.zhenxin.zmusic.component.ZClickEvent;
import me.zhenxin.zmusic.component.ZComponent;
import me.zhenxin.zmusic.config.Config;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void createsOneMessagePerLineWithYellowActionButtons() {
        Notice notice = new Notice("notice-1", "维护公告",
                "第一行\n\n详情：https://example.com/form\n最后一行");

        List<ZComponent> messages = NoticeService.createMessages(notice);

        assertEquals(6, messages.size());
        assertEquals(Config.prefix + "§a[公告] 维护公告", messages.get(0).toPlainText());
        assertEquals(Config.prefix + "§a第一行", messages.get(1).toPlainText());
        assertEquals(Config.prefix + "§a", messages.get(2).toPlainText());
        assertEquals(Config.prefix + "§a详情：§e[点击打开]", messages.get(3).toPlainText());
        assertFalse(messages.get(3).toPlainText().contains("https://example.com/form"));

        ZComponent openUrlButton = messages.get(3).getChildren().get(1);
        assertEquals("§e[点击打开]", openUrlButton.getText());
        assertEquals(ZClickEvent.Action.OPEN_URL, openUrlButton.getClickEvent().getAction());
        assertEquals("https://example.com/form", openUrlButton.getClickEvent().getValue());

        assertEquals(Config.prefix + "§a最后一行", messages.get(4).toPlainText());
        assertEquals(Config.prefix + "§e[点击标记为已读]", messages.get(5).toPlainText());
        ZComponent readButton = messages.get(5).getChildren().get(0);
        assertEquals("§e[点击标记为已读]", readButton.getText());
        assertEquals(ZClickEvent.Action.RUN_COMMAND, readButton.getClickEvent().getAction());
        assertEquals("/zm notice read notice-1", readButton.getClickEvent().getValue());
    }
}
