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
            KitX.getDataManager().getKits().forEach((name, kit) -> {
                if(kit.getPermission() == null || p.hasPermission(kit.getPermission())) {
                    comp.addExtra(KitX.format("&e" + kit.getName() + " "));
                    TextComponent cmdComp = new TextComponent(KitX.format("&7/kit " + kit.getName()));
                    cmdComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(KitX.format("&eClick to get kit")).create()));
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
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdDoesntExist")));
            return true;
        }
        Kit kit = KitX.getDataManager().getKit(kitName);
        if(kit.getPermission() != null && !p.hasPermission(kit.getPermission())) {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdNoPerms")));
            return true;
        }
        if(!p.hasPermission("kit.limit." + kitName + ".bypass") && KitX.getDataManager().onLimitForKit(kitName, p.getUniqueId())) {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdLimit")));
            return true;
        }
        if(p.hasPermission("kit.cooldown." + kitName + ".bypass") || !KitX.getDataManager().onCooldownForKit(kitName, p.getUniqueId())) {
            kit.getItems().forEach(i -> p.getInventory().addItem((ItemStack) ((Object) i)));
            p.sendMessage(KitX.format("&eGave you the &6" + kit.getName() + " &ekit"));
        } else {
            p.sendMessage(KitX.format(Config.getConfig().getString("messages.kitCmdCooldown")));
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
            return KitX.tabCompletions(args[0], list);
        }
        return new ArrayList<>();
    }

}
