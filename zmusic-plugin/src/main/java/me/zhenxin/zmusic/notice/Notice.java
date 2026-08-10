package me.zhenxin.zmusic.notice;

/**
 * 公告接口返回的玩家可见内容。
 */
public class Notice {

    private final String id;
    private final String title;
    private final String content;

    public Notice(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
