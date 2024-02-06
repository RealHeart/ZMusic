package me.zhenxin.zmusic.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.BossBar;
import net.md_5.bungee.protocol.packet.Kick;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BCTextPacketHelper {

    private static final Class<BossBar> bossBarClass = BossBar.class;
    private static final Method bossBarSetTitle;

    private static final boolean isLegacy;

    static {
        boolean a = false;
        try {
            Kick.class.getConstructor(String.class).newInstance("");
            a=true;
        } catch (Exception ignore) {}
        isLegacy=a;

        try {
            bossBarSetTitle = bossBarClass.getMethod("setTitle", a ? String.class : BaseComponent.class);
        } catch (NoSuchMethodException e) {
            throw new FailedProcessPacketException("BossBar.setTitle", e);
        }
    }

    public static void setTitle(final BaseComponent component, final BossBar packet) {
        try {
            bossBarSetTitle.invoke(packet, isLegacy ? ComponentSerializer.toString(component) : component);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FailedProcessPacketException("BossBar.setTitle", e);
        }
    }

    private static class FailedProcessPacketException extends RuntimeException {
        private FailedProcessPacketException(final String fieldName, final Throwable throwable) {
            super("A exception has occurred when getting field: " + fieldName, throwable);
        }
    }

}
