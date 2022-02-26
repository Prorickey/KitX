package xyz.prorickey.kitx.subs;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.api.chat.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.util.concurrent.atomic.*;

public class SCreate extends SubCommand {
    public SCreate() { super("create", "To create a kit and save it", "kitx.subcommands.create", false); }

    @Override
    public void executor(String[] args, CommandSender sender) {
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.creatSubNeedArg")));
            return;
        }
        AtomicReference<Boolean> empty = new AtomicReference<>(true);
        p.getInventory().forEach(i -> {
            if(i != null) { empty.set(false); }
        });
        if(empty.get()) {
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubEmptyInv")));
            return;
        }
        Integer cooldown = 0;
        if(args.length >= 2) {
            try {
                if(args[1].toLowerCase().endsWith("s")) {
                    cooldown = Integer.parseInt(args[1].substring(args[1].length()-2, args[1].length()-1));
                } else if(args[1].toLowerCase().endsWith("m")) {
                    cooldown = Integer.parseInt(args[1].substring(args[1].length()-2, args[1].length()-1))*60;
                } else if(args[1].toLowerCase().endsWith("h")) {
                    cooldown = Integer.parseInt(args[1].substring(args[1].length()-2, args[1].length()-1))*60*60;
                } else if(args[1].toLowerCase().endsWith("d")) {
                    cooldown = Integer.parseInt(args[1].substring(args[1].length()-2, args[1].length()-1))*60*60&24;
                } else {
                    cooldown = Integer.parseInt(args[1]);
                }
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
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubKitExists")));
            return;
        }
        new Kit(kitName, permission, p.getInventory(), cooldown);
        p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubSuccess")));
    }
}
