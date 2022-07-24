package me.zhenxin.zmusic.bridge.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;


@SuppressWarnings({"unused", "AlibabaClassMustHaveAuthor", "AlibabaClassNamingShouldBeCamel", "NullableProblems"})
public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "zmusic";
    }

    @Override
    public String getAuthor() {
        return "ZhenXin";
    }

    @Override
    public String getVersion() {
        return "3.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        // TODO: 暂未实现
        return null;
    }
}