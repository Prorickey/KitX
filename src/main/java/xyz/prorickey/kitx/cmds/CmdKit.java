package xyz.prorickey.kitx.cmds;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;
import xyz.prorickey.proutils.ChatFormat;
import xyz.prorickey.proutils.TabComplete;

import java.util.*;
import java.util.List;

public class CmdKit implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatFormat.format(Config.getConfig().getString("messages.cannotExecuteFromConsole")));
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            TextComponent comp = new TextComponent(ChatFormat.format("\n&6Available kits\n"));
            KitX.getDataManager().getKits().forEach((name, kit) -> {
                if(kit.getPermission() == null || p.hasPermission(kit.getPermission())) {
                    comp.addExtra(ChatFormat.format("&e" + kit.getName() + " "));
                    TextComponent cmdComp = new TextComponent(ChatFormat.format("&7/kit " + kit.getName()));
                    cmdComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatFormat.format("&eClick to get kit")).create()));
                    cmdComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.getName()));
                    comp.addExtra(cmdComp);
                    comp.addExtra("\n");
                }
            });
            p.spigot().sendMessage(comp);
            return true;
        }
        String kitName = args[0].toLowerCase();
        if(KitX.getDataManager().getKit(kitName) == null) {
            p.sendMessage(ChatFormat.format(Config.getConfig().getString("messages.kitCmdDoesntExist")));
            return true;
        }
        Kit kit = KitX.getDataManager().getKit(kitName);
        if(kit.getPermission() != null && !p.hasPermission(kit.getPermission())) {
            p.sendMessage(ChatFormat.format(Config.getConfig().getString("messages.kitCmdNoPerms")));
            return true;
        }
        if(!p.hasPermission("kit.limit." + kitName + ".bypass") && KitX.getDataManager().onLimitForKit(kitName, p.getUniqueId())) {
            p.sendMessage(ChatFormat.format(Config.getConfig().getString("messages.kitCmdLimit")));
            return true;
        }
        if(p.hasPermission("kit.cooldown." + kitName + ".bypass") || !KitX.getDataManager().onCooldownForKit(kitName, p.getUniqueId())) {
            kit.getItems().forEach(i -> p.getInventory().addItem(i));
            p.sendMessage(ChatFormat.format("&eGave you the &6" + kit.getName() + " &ekit"));
        } else {
            p.sendMessage(ChatFormat.format(Config.getConfig().getString("messages.kitCmdCooldown")));
            return true;
        }
        if(!p.hasPermission("kit.cooldown." + kitName + ".bypass") && kit.getCooldown() != 0) KitX.getDataManager().putCooldownForKit(kitName, p.getUniqueId());
        if(!p.hasPermission("kit.limit." + kitName + ".bypass") && kit.getLimit() != 0) KitX.getDataManager().addLimitForKit(kitName, p.getUniqueId());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1 && !(sender instanceof ConsoleCommandSender)) {
            Player p = (Player) sender;
            List<String> list = new ArrayList<>();
            KitX.getDataManager().getKits().forEach((name, kit) -> {
                if(kit.getPermission() == null || p.hasPermission(kit.getPermission())) {
                    if(!KitX.getDataManager().onCooldownForKit(kit.getName(), p.getUniqueId())) {
                        list.add(kit.getName());
                    }
                }
            });
            return TabComplete.tabCompletionsSearch(args[0], list);
        }
        return new ArrayList<>();
    }

}
