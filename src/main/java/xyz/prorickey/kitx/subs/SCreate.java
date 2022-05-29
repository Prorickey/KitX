package xyz.prorickey.kitx.subs;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import xyz.prorickey.api.chat.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class SCreate extends SubCommand {
    public SCreate() { super("create", "To create a kit and save it", "kitx.subcommands.create", false); }

    @Override
    public void executor(String[] args, CommandSender sender) {
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.creatSubNeedArg")));
            return;
        } /*else if(args.length == 1) {
            createCreateKitGUI(p, args[0]);
            return;
        }*/
        AtomicReference<Boolean> empty = new AtomicReference<>(true);
        p.getInventory().forEach(i -> {
            if(i != null) { empty.set(false); }
        });
        if(empty.get()) {
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubEmptyInv")));
            return;
        }
        int cooldown = 0;
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
        String permission = null;
        if(args.length >= 3) {
            permission = args[2];
        }
        String kitName = args[0].toLowerCase();
        if(KitX.getDataManager().getKit(kitName) != null) {
            p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubKitExists")));
            return;
        }
        KitX.getDataManager().saveKit(kitName, new Kit(kitName, permission, p.getInventory(), cooldown));
        p.sendMessage(Chat.format(Config.getConfig().getString("messages.createSubSuccess")));
    }

    public static void createCreateKitGUI(Player player, String name) {
        Inventory gui = Bukkit.createInventory(player, InventoryType.CHEST);
        for(int i = 0; i < 54; i++) {
            gui.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }
        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemMeta emeta = emerald.getItemMeta();
        emeta.setDisplayName(Chat.format("&a" + name));
        emeta.setLore(Arrays.asList(Chat.format("&eIcon: Diamond Sword"), Chat.format("&eCooldown: 0")));
        emerald.setItemMeta(emeta);
        gui.setItem(0, emerald);
        ItemStack icon = new ItemStack(Material.IRON_SWORD);
        ItemMeta iconmeta = icon.getItemMeta();
        iconmeta.setDisplayName(Chat.format("&eIcon"));
        iconmeta.setLore(Arrays.asList("&aClick to change icon"));
        icon.setItemMeta(iconmeta);
        gui.setItem(13, icon);
        ItemStack cooldown = new ItemStack(Material.WATCH);
        ItemMeta cooldownmeta = cooldown.getItemMeta();
        cooldownmeta.setDisplayName(Chat.format("&eCooldown"));
        cooldownmeta.setLore(Arrays.asList(Chat.format("&aClick to change the cooldown")));
        cooldown.setItemMeta(cooldownmeta);
        gui.setItem(29, cooldown);
        player.openInventory(gui);
    }

}
