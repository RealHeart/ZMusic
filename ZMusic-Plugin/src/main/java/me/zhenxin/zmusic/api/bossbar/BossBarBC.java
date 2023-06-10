package me.zhenxin.zmusic.api.bossbar;

import com.google.common.collect.Sets;
import me.zhenxin.zmusic.ZMusic;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BossBarBC implements BossBar {

    private static final AtomicInteger barID = new AtomicInteger(1);

    private final UUID uuid;
    private final Object player;
    private final double seconds;
    private final Set<ProxiedPlayer> players = Sets.newHashSet();
    private String title;
    private double progress = 0;
    private BarColor barColor;
    private BarStyle barStyle;
    private String compiledTitle;
    private boolean visible = false;

    public BossBarBC(Object palyer, String title, BarColor barColor, BarStyle barStyle, float seconds) {
        this.uuid = UUID.nameUUIDFromBytes(("BBB:" + barID.getAndIncrement()).getBytes(StandardCharsets.UTF_8));
        this.player = palyer;
        this.title = title;
        this.compiledTitle = ComponentSerializer.toString(new TextComponent(this.title));
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.seconds = seconds;
    }

    public void showTitle() {
        addPlayer(player);
        setVisible(true);
        setProgress(0);
        ZMusic.runTask.runAsync(() -> {
            double step = 1F / seconds;
            double prog = getProgress();
            while (prog >= 0 || prog <= 1) {
                prog += step;
                if (prog > 1) break;
                setProgress(prog);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setVisible(false);
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        this.compiledTitle = ComponentSerializer.toString(new TextComponent(this.title));

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 3);
            packet.setTitle(this.compiledTitle);
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    @Override
    public BarColor getBarColor() {
        return barColor;
    }

    @Override
    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 4);
            packet.setColor(this.barColor.ordinal());
            packet.setDivision(this.barStyle.ordinal());
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    @Override
    public BarStyle getBarStyle() {
        return barStyle;
    }

    @Override
    public void setBarStyle(BarStyle barStyle) {
        this.barStyle = barStyle;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 4);
            packet.setColor(this.barColor.ordinal());
            packet.setDivision(this.barStyle.ordinal());
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public void setProgress(double progress) {
        this.progress = progress;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 2);
            packet.setHealth((float) this.progress);
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    @Override
    public boolean hasPlayer(Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        return this.players.contains(player);
    }

    @Override
    public void addPlayer(Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        this.players.add(player);

        if (visible && player.isConnected()) {
            player.unsafe().sendPacket(getAddPacket());
        }
    }

    @Override
    public void removePlayer(Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        this.players.remove(player);

        if (visible && player.isConnected()) {
            player.unsafe().sendPacket(getRemovePacket());
        }
    }

    @Override
    public void removeAll() {
        this.players.stream().collect(Collectors.toSet()).forEach(this::removePlayer);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible == this.visible) {
            return;
        }

        this.visible = visible;

        net.md_5.bungee.protocol.packet.BossBar packet = visible ? getAddPacket() : getRemovePacket();
        this.players.forEach(player -> player.unsafe().sendPacket(packet));
    }

    private net.md_5.bungee.protocol.packet.BossBar getAddPacket() {
        net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 0);
        packet.setTitle(this.compiledTitle);
        packet.setColor(this.barColor.ordinal());
        packet.setDivision(this.barStyle.ordinal());
        packet.setHealth((float) this.progress);
        return packet;
    }

    private net.md_5.bungee.protocol.packet.BossBar getRemovePacket() {
        return new net.md_5.bungee.protocol.packet.BossBar(uuid, 1);
    }

}