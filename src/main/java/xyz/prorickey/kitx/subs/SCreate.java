package xyz.prorickey.kitx.subs;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

public class SCreate extends SubCommand {
    public SCreate() { super("create", "To create a kit and save it", "kitx.subcommands.create", false); }

    @Override
    public void executor(String[] args, CommandSender sender) {
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.creatSubNeedArg")));
            return;
        }
        if(p.getInventory().isEmpty()) {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.createSubEmptyInv")));
            return;
        }
        Integer cooldown = 0;
        if(args.length >= 2) {
            try {
                cooldown = Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                cooldown = 0;
            }
        }
        String permission = null;
        if(args.length >= 3) {
            permission = args[2];
        }
        String kitName = args[0].toLowerCase();
        if(KitX.kitExists(kitName)) {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.createSubKitExists")));
            return;
        }
        new Kit(kitName, permission, p.getInventory(), cooldown);
        p.sendMessage(Utils.format(Config.getConfig().getString("messages.createSubSuccess")));
    }
}
