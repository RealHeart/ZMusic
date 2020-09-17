package cn.iqianye.mc.zmusic.command;

import cn.iqianye.mc.zmusic.ZMusic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdBukkit implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //指令输出
        if (sender instanceof Player) {
            return Cmd.cmd(sender, args);
        } else {
            ZMusic.log.sendErrorMessage("控制台不支持使用任何指令");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            return Cmd.tab(sender, args);
        } else {
            return new ArrayList<>();
        }
    }
}
