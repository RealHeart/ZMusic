package me.zhenxin.zmusic.utils.message;

import me.zhenxin.zmusic.language.Lang;
import net.md_5.bungee.api.chat.TextComponent;

public interface Message {

    void sendNormalMessage(String message, Object playerObj);

    void sendErrorMessage(String message, Object playerObj);

    void sendJsonMessage(TextComponent message, Object playerObj);

    default void sendActionBarMessage(String message, Object playerObj) {

    }

    void sendActionBarMessage(TextComponent message, Object playerObj);

    void sendTitleMessage(String title, String subTitle, Object playerObj);

    void sendNull(Object playerObj);

    default void sendPlayError(Object playerObj, String musicName) {
        for (String s : Lang.playError) {
            sendErrorMessage(s.replaceAll("%musicName%", musicName), playerObj);
        }
    }
}
