package xyz.prorickey.kitx.subs;

import org.bukkit.command.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.util.*;

public class SDelete extends SubCommand {
    public SDelete() { super("delete", "To delete a kit", "kitx.subcommands.delete", true); }

    @Override
    public void executor(String[] args, CommandSender sender) {
        if(args.length == 0) {
            sender.sendMessage(Utils.format(Config.getConfig().getString("messages.deleteSubNeedArg")));
            return;
        }
        String kitName = args[0].toLowerCase();
        if(!KitX.kitExists(kitName)) {
            sender.sendMessage(Utils.format(Config.getConfig().getString("messages.deleteSubKitDoesntExist")));
            return;
        }
        KitX.getKit(kitName).deleteKit();
        sender.sendMessage(Utils.format(Config.getConfig().getString("messages.deleteSubSuccess")));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender ||  sender.hasPermission("kitx.subcommands.delete")) {
            List<String> list = new ArrayList<>();
            list.addAll(KitX.getKits().keySet());
            return Utils.tabCompletionsSearch(args[0], list);
        }
        return new ArrayList<>();
    }
}
