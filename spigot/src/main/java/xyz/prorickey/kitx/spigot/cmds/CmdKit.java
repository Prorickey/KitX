package xyz.prorickey.kitx.spigot.cmds;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.chat.UTabComp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.spigot.Config;
import xyz.prorickey.kitx.spigot.KitX;

import java.util.*;
import java.util.List;

public class CmdKit {

    public static void registerCommand(PaperCommandManager<CommandSender> manager) {
        manager.command(
                manager.commandBuilder("kit", ArgumentDescription.of("To get a kit"))
                        .argument(
                                manager.argumentBuilder(String.class, "kitName")
                                        .asOptional()
                                        .withSuggestionsProvider((context, arg) -> {
                                            List<String> list = new ArrayList<>(KitX.getKitManager().getAllKits().keySet());
                                            return UTabComp.tabCompletionsSearch(arg, list);
                                        })
                        )
                        .handler(CmdKit::handle)
                        .build()
        );
    }

    private static void handle(@NonNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        if(!(sender instanceof Player player)) {
            sender.sendMessage(UChat.miniMessage(Config.getConfig().getString("messages.cannotExecuteFromConsole")));
            return;
        }
        Optional<String> kitNameOpt = context.getOptional("kitName");

        if(kitNameOpt.isEmpty()) {
            final Component[] comp = {UChat.miniMessage("\n<gold>Available kits\n")};
            KitX.getKitManager().getAllKits().forEach((name, kit) -> {
                if(kit.permission() == null || player.hasPermission(kit.permission())) {
                    comp[0] = comp[0]
                            .append(UChat.miniMessage("<yellow>" + kit.name() + " "))
                            .append(
                                    UChat.miniMessage("<gray>/kit " + kit.name())
                                            .hoverEvent(HoverEvent.showText(UChat.miniMessage("<yellow>Click to get kit")))
                                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.name()))
                            )
                            .append(UChat.miniMessage("\n"));
                }
            });
            player.sendMessage(comp[0]);
        } else {
            String kitName = kitNameOpt.get().toLowerCase();
            if(KitX.getKitManager().getKit(kitName) == null) {
                player.sendMessage(UChat.miniMessage("<red>That kit doesn't exist!"));
                return;
            }
            Kit kit = KitX.getKitManager().getKit(kitName);
            if(kit.permission() != null && !player.hasPermission(kit.permission())) {
                player.sendMessage(UChat.miniMessage("<red>You don't have permission to get that kit!"));
                return;
            }
            if(!player.hasPermission("kit.limit." + kitName + ".bypass") && kit.limit() != 0 && KitX.getKitManager().getPlayerLimit(player.getUniqueId(), kit) >= kit.limit()) {
                player.sendMessage(UChat.miniMessage("<red>You have reached the limit for that kit!"));
                return;
            }
            if(player.hasPermission("kit.cooldown." + kitName + ".bypass") || !KitX.getKitManager().checkPlayerOnCooldown(player.getUniqueId(), kit)) {
                kit.items().forEach(i -> player.getInventory().addItem(i));
                player.sendMessage(UChat.miniMessage("<yellow>Gave you the <gold>" + kit.name() + " <yellow>kit"));
            } else {
                player.sendMessage(UChat.miniMessage("<red>You are on cooldown for that kit!"));
                return;
            }
            if(!player.hasPermission("kit.cooldown." + kitName + ".bypass") && kit.cooldown() != 0)
                KitX.getKitManager().putPlayerOnCooldown(player.getUniqueId(), kit);
            if(!player.hasPermission("kit.limit." + kitName + ".bypass") && kit.limit() != 0)
                KitX.getKitManager().updatePlayerLimit(player.getUniqueId(), kit, 1);
        }
    }

}
