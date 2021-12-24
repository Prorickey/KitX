package xyz.prorickey.kitx.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;
import xyz.prorickey.kitx.cmds.*;


public class SHelp extends SubCommand {
    public SHelp() { super("help", "To send the help section of the /kit command", null, true); }
    @Override
    public void executor(String[] args, CommandSender sender) {
        ComponentBuilder comp = new ComponentBuilder();
        comp.append(Utils.format("\n&6KitX Help Section\n"));
        CmdKitX.subs.forEach((key, sub) -> {
            comp.append(Utils.format("&6/" + sub.getName() + " &7- &e" + sub.getDescription() + "\n"));
        });
        sender.spigot().sendMessage(comp.create());
    }
}
