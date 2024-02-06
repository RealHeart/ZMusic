package me.zhenxin.zmusic.utils.log;

public interface Log {
    /**
     * 发送普通日志
     *
     * @param message 消息
     */
    void sendNormalMessage(String message);

    /**
     * 发送调试日志
     *
     * @param message 消息
     */
    void sendDebugMessage(String message);

    /**
     * 发送错误日志
     *
     * @param message 消息
     */
    void sendErrorMessage(String message);

    Object getSender();
}
