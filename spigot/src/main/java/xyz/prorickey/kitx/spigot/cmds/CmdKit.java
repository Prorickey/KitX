package xyz.prorickey.kitx.spigot.cmds;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.spigot.Config;
import xyz.prorickey.kitx.spigot.KitX;

import java.util.*;
import java.util.List;

public class CmdKit implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(KitX.format(Config.getConfig().getString("messages.cannotExecuteFromConsole")));
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            TextComponent comp = new TextComponent(KitX.format("\n&6Available kits\n"));
            KitX.getKitManager().getAllKits().forEach((name, kit) -> {
                if(kit.permission() == null || p.hasPermission(kit.permission())) {
                    comp.addExtra(KitX.format("&e" + kit.name() + " "));
                    TextComponent cmdComp = new TextComponent(KitX.format("&7/kit " + kit.name()));
                    cmdComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(KitX.format("&eClick to get kit")).create()));
                    cmdComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.name()));
                    comp.addExtra(cmdComp);
                    comp.addExtra("\n");
                }
            });
            p.spigot().sendMessage(comp);
            return true;
        }
        String kitName = args[0].toLowerCase();
        if(KitX.getKitManager().getKit(kitName) == null) {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdDoesntExist")));
            return true;
        }
        Kit kit = KitX.getKitManager().getKit(kitName);
        if(kit.permission() != null && !p.hasPermission(kit.permission())) {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdNoPerms")));
            return true;
        }
        if(!p.hasPermission("kit.limit." + kitName + ".bypass") && KitX.getKitManager().getPlayerLimit(p.getUniqueId(), kit) <= kit.limit()) {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdLimit")));
            return true;
        }
        if(p.hasPermission("kit.cooldown." + kitName + ".bypass") || !KitX.getKitManager().checkPlayerOnCooldown(p.getUniqueId(), kit)) {
            kit.items().forEach(i -> p.getInventory().addItem((ItemStack) ((Object) i)));
            p.sendMessage(KitX.format("&eGave you the &6" + kit.name() + " &ekit"));
        } else {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdCooldown")));
            return true;
        }
        if(!p.hasPermission("kit.cooldown." + kitName + ".bypass") && kit.cooldown() != 0) KitX.getKitManager().putPlayerOnCooldown(p.getUniqueId(), kit);
        if(!p.hasPermission("kit.limit." + kitName + ".bypass") && kit.limit() != 0) KitX.getKitManager().updatePlayerLimit(p.getUniqueId(), kit, 1);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1 && !(sender instanceof ConsoleCommandSender)) {
            Player p = (Player) sender;
            List<String> list = new ArrayList<>();
            KitX.getKitManager().getAllKits().forEach((name, kit) -> {
                if(kit.permission() == null || p.hasPermission(kit.permission())) {
                    if(!KitX.getKitManager().checkPlayerOnCooldown(p.getUniqueId(), kit)) {
                        list.add(kit.name());
                    }
                }
            });
            return KitX.tabCompletions(args[0], list);
        }
        return new ArrayList<>();
    }

}
