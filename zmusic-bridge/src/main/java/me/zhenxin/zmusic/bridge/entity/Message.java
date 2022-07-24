package me.zhenxin.zmusic.bridge.entity;

import me.zhenxin.zmusic.bridge.enums.MessageType;

/**
 * 通信消息
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/24 11:59
 */
public class Message {
    /**
     * 消息类型
     */
    private MessageType type;
    /**
     * 音乐信息 (仅 {@link MessageType#INFO} 有效)
     */
    private MusicInfo info;
    /**
     * Toast 消息(仅 {@link MessageType#TOAST} 有效)
     */
    private String title;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MusicInfo getInfo() {
        return info;
    }

    public void setInfo(MusicInfo info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
