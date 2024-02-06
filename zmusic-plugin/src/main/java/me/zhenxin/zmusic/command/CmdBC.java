package me.zhenxin.zmusic.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CmdBC extends Command implements TabExecutor {

    public CmdBC() {
        super("zm", null, "zmusic", "music");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Cmd.cmd(sender, args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return Cmd.tab(sender, args);
    }
}
