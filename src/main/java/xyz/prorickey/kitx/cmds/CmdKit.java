package xyz.prorickey.kitx.cmds;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.util.*;
import java.util.List;

public class CmdKit implements CommandExecutor, TabCompleter {

    public static Map<String, Map<String, Long>> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Utils.format(Config.getConfig().getString("messages.cannotExecuteFromConsole")));
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            TextComponent comp = new TextComponent(Utils.format("\n&6Available kits\n"));
            KitX.getKits().forEach((name, kit) -> {
                if(kit.getPermission() == null || p.hasPermission(kit.getPermission())) {
                    comp.addExtra(Utils.format("&e" + kit.getName() + " "));
                    TextComponent cmdComp = new TextComponent(Utils.format("&7/kit " + kit.getName()));
                    cmdComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.format("&eClick to get kit")).create()));
                    cmdComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.getName()));
                    comp.addExtra(cmdComp);
                    comp.addExtra("\n");
                }
            });
            p.spigot().sendMessage(comp);
            return true;
        }
        String kitName = args[0].toLowerCase();
        if(!KitX.kitExists(kitName)) {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.kitCmdDoesntExist")));
            return true;
        }
        Kit kit = KitX.getKit(kitName);
        if(kit.getPermission() != null && !p.hasPermission(kit.getPermission())) {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.kitCmdNoPerms")));
            return true;
        }
        if(!cooldown.containsKey(p.getUniqueId().toString()) ||
                !cooldown.get(p.getUniqueId().toString()).containsKey(kitName) ||
                !(cooldown.get(p.getUniqueId().toString()).get(kitName) > (Utils.getUnix() - (kit.getCooldown())))) {
            kit.getItems().forEach(i -> p.getInventory().addItem(i));
            p.sendMessage(Utils.format("&eGave you the &6" + kit.getName() + " &ekit"));
            if(!p.hasPermission("kit.cooldown." + kitName + ".bypass") && kit.getCooldown() != 0) {
                if(!cooldown.containsKey(p.getUniqueId().toString())) {
                    cooldown.put(p.getUniqueId().toString(), new HashMap<>());
                }
                cooldown.get(p.getUniqueId().toString()).put(kitName, Utils.getUnix());
            }
        } else {
            p.sendMessage(Utils.format(Config.getConfig().getString("messages.kitCmdCooldown")));
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1 && !(sender instanceof ConsoleCommandSender)) {
            Player p = (Player) sender;
            List<String> list = new ArrayList<>();
            KitX.getKits().forEach((name, kit) -> {
                if(kit.getPermission() == null || p.hasPermission(kit.getPermission())) {
                    if(!cooldown.containsKey(p.getUniqueId().toString()) || (!cooldown.get(p.getUniqueId().toString()).containsKey(name) || cooldown.get(p.getUniqueId().toString()).get(name) > (Utils.getUnix() - 1000 * kit.getCooldown()))) {
                        list.add(kit.getName());
                    }
                }
            });
            return Utils.tabCompletionsSearch(args[0], list);
        }
        return new ArrayList<>();
    }

}
