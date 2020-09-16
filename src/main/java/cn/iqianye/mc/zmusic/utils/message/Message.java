package cn.iqianye.mc.zmusic.utils.message;

import net.md_5.bungee.api.chat.TextComponent;

public interface Message {

    void sendNormalMessage(String message, Object playerObj);

    void sendErrorMessage(String message, Object playerObj);

    void sendJsonMessage(TextComponent message, Object playerObj);

    void sendActionBarMessage(TextComponent message, Object playerObj);

    void sendTitleMessage(String title, String subTitle, Object playerObj);

    void sendNull(Object playerObj);

    default void sendPlayError(Object playerObj, String musicName) {
        sendErrorMessage("搜索§r[§e" + musicName + "§r]§c失败，可能为以下问题.", playerObj);
        sendErrorMessage("1.搜索的音乐不存在或已下架", playerObj);
        sendErrorMessage("2.搜索的音乐为付费音乐", playerObj);
        sendErrorMessage("3.搜索的音乐为试听音乐", playerObj);
        sendErrorMessage("4.服务器网络异常", playerObj);
    }
}
