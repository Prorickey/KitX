package xyz.prorickey.kitx.spigot.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.kitx.spigot.KitX;
import xyz.prorickey.kitx.spigot.builders.SubCommand;
import xyz.prorickey.kitx.spigot.cmds.CmdKitX;


public class SHelp extends SubCommand {
    public SHelp() { super("help", "To send the help section of the /kit command", null, false); }
    @Override
    public void executor(String[] args, CommandSender sender) {
        Player p = (Player) sender;
        ComponentBuilder comp = new ComponentBuilder(KitX.format("\n&6KitX Help Section\n"));
        CmdKitX.subs.forEach((key, sub) -> {
            comp.append(KitX.format("&6/" + sub.getName() + " &7- &e" + sub.getDescription() + "\n"));
        });
        p.spigot().sendMessage(comp.create());
    }
}
