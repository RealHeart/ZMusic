package cn.iqianye.mc.zmusic.command;

import cn.iqianye.mc.zmusic.ZMusic;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class CmdBC extends Command implements TabExecutor {

    public CmdBC() {
        super("zm", null, "zmusic", "music");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            Cmd.cmd(commandSender, strings);
        } else {
            ZMusic.log.sendErrorMessage("控制台不支持使用任何指令");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            return Cmd.tab(commandSender, strings);
        } else {
            return new ArrayList<>();
        }
    }
}
