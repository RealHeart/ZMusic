package me.zhenxin.zmusic.bridge.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "AlibabaClassMustHaveAuthor", "AlibabaClassNamingShouldBeCamel"})
public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "zmusic";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ZhenXin";
    }

    @Override
    public @NotNull String getVersion() {
        return "3.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        // TODO: 暂未实现
        return null;
    }
}