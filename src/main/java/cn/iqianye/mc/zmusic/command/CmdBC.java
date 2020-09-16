package cn.iqianye.mc.zmusic.command;

import cn.iqianye.mc.zmusic.ZMusic;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdBC extends Command {

    public CmdBC() {
        super("zm");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            Cmd.cmd(commandSender, strings);
        } else {
            ZMusic.log.sendErrorMessage("控制台不支持使用任何指令");
        }
    }
}
