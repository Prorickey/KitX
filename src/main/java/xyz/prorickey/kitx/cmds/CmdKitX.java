package xyz.prorickey.kitx.cmds;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.*;
import xyz.prorickey.api.chat.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;
import xyz.prorickey.kitx.subs.*;

import java.util.*;

public class CmdKitX implements CommandExecutor, TabCompleter {

    private JavaPlugin plugin;
    public static Map<String, SubCommand> subs = new HashMap<>();

    public CmdKitX(JavaPlugin p) {
        this.plugin = p;
        p.getCommand("kitx").setExecutor(this);
        p.getCommand("kitx").setTabCompleter(this);

        new SHelp();
        new SCreate();
        new SDelete();
        new SList();

    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(args.length > 0) {
            String arg = args[0].toLowerCase();
            if(subs.containsKey(arg)) {
                SubCommand sub = subs.get(arg);
                if(sender instanceof ConsoleCommandSender && !sub.getConsole()) {
                    sender.sendMessage(Chat.format(Config.getConfig().getString("messages.cannotExecuteFromConsole")));
                } else if(sender instanceof Player && sub.getPermission() != null && !((Player) sender).hasPermission("kitx.subcommand." + sub.getName())) {
                    sender.sendMessage(Chat.format(Config.getConfig().getString("messages.noPermission")));
                } else {
                    String[] newArgs = new String[args.length - 1];
                    for(int i = 1; i < args.length; i++) { newArgs[i - 1] = args[i]; }
                    sub.executor(newArgs, sender);
                }
            } else {
                sender.sendMessage(Chat.format(Config.getConfig().getString("messages.subCommandDoesNotExist")));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            subs.forEach((key, sub) -> {
               if(sender instanceof ConsoleCommandSender || sub.getPermission() == null || sender.hasPermission(sub.getPermission())) {
                   list.add(sub.getName());
               }
            });
            return TabComplete.tabCompletionsSearch(args[0], list);
        } else if(args.length == 2) {
            if(subs.containsKey(args[0].toLowerCase())) {
                String[] newArgs = new String[args.length - 1];
                for(int i = 1; i < args.length; i++) { newArgs[i - 1] = args[i]; }
                return subs.get(args[0].toLowerCase()).tabComplete(sender, newArgs);
            }

        }
        return new ArrayList<>();
    }
}
