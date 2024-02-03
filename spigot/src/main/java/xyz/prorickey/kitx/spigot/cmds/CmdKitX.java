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
import org.bukkit.plugin.java.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.spigot.KitX;
import xyz.prorickey.kitx.spigot.builders.SubCommand;

import java.time.Duration;
import java.util.*;

public class CmdKitX {

    private JavaPlugin plugin;
    public static Map<String, SubCommand> subs = new HashMap<>();

    public static void registerCommand(PaperCommandManager<CommandSender> manager) {
        manager.command(
                manager.commandBuilder("kitx", ArgumentDescription.of("The admin command for KitX"))
                        .permission("kitx.admin")
                        .argument(
                                manager.argumentBuilder(String.class, "sub")
                                        .asOptional()
                                        .withSuggestionsProvider((context, arg) ->
                                                UTabComp.tabCompletionsSearch(arg, List.of("help", "create", "delete", "edit", "list")))
                                        .build()
                        )
                        .argument(
                                manager.argumentBuilder(String.class, "kitName")
                                        .asOptional()
                                        .withSuggestionsProvider((context, arg) -> {
                                            if(context.get("sub").equals("create")) return new ArrayList<>();
                                            List<String> list = new ArrayList<>(KitX.getKitManager().getAllKits().keySet());
                                            return UTabComp.tabCompletionsSearch(arg, list);
                                        })
                                        .build()
                        )
                        .argument(
                                manager.argumentBuilder(String.class, "property")
                                        .asOptional()
                                        .withSuggestionsProvider((context, arg) -> {
                                            if(!context.get("sub").equals("edit")) return new ArrayList<>();
                                            List<String> list = new ArrayList<>(List.of("permission", "cooldown", "limit"));
                                            return UTabComp.tabCompletionsSearch(arg, list);
                                        })
                                        .build()
                        )
                        .argument(StringArgument.optional("value"))
                        .handler(CmdKitX::handle)
                        .build()
        );
    }

    private static void handle(@NonNull CommandContext<CommandSender> context) {
        Optional<String> subOpt = context.getOptional("sub");
        if(subOpt.isEmpty()) {
            Component comp = UChat.miniMessage("<br><gold>KitX <gray>- <aqua><em>v" + KitX.getPlugin().getPluginMeta().getVersion() + "<reset><br>")
                    .append(UChat.miniMessage("<yellow>Created by <click:copy_to_clipboard:https://github.com/Prorickey><aqua>Prorickey</click><reset><br>"))
                    .append(UChat.miniMessage("<yellow>Get support in the <click:copy_to_clipboard:https://discord.gg/RNQTnXESNb><aqua>Discord</click><reset><br>"));

            context.getSender().sendMessage(comp);
            return;
        }
        switch (subOpt.get()) {
            case "help" -> {
                Component comp = UChat.miniMessage("<br><gold>KitX Help Section<reset><br>")
                        .append(UChat.miniMessage("<gray>  - <yellow>/kitx help <gray>- <aqua>Shows this help section<reset><br>"))
                        .append(UChat.miniMessage("<gray>  - <yellow>/kitx list <gray>- <aqua>Lists all the kits<reset><br>"))
                        .append(UChat.miniMessage("<gray>  - <yellow>/kitx create [kitName] <gray>- <aqua>Creates a kit<reset><br>"))
                        .append(UChat.miniMessage("<gray>  - <yellow>/kitx edit [kitName] [property] [value] <gray>- <aqua>Edit a property in a kit<reset><br>"))
                        .append(UChat.miniMessage("<gray>  - <yellow>/kitx delete [kitname] <gray>- <aqua>Deletes a kit<reset><br>"));
                context.getSender().sendMessage(comp);
            }
            case "list" -> {
                final Component[] comp = {UChat.miniMessage("<br><gold>All Kits<reset><br>")
                        .append(UChat.miniMessage("<yellow>Click on a kit to get it<reset><br>"))};
                KitX.getKitManager().getAllKits().forEach((name, kit) -> comp[0] = comp[0].append(UChat.miniMessage("<yellow>" + kit.name() + " "))
                        .append(UChat.miniMessage("<gray>/kit " + kit.name()))
                        .hoverEvent(HoverEvent.showText(UChat.miniMessage("<yellow>Click to get kit")))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.name()))
                        .append(UChat.miniMessage("<reset><br>")));
                context.getSender().sendMessage(comp[0]);
            }
            case "create" -> {
                if (!(context.getSender() instanceof Player player)) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>You cannot execute this command from console!"));
                    return;
                }
                Optional<String> kitNameOpt = context.getOptional("kitName");
                if (kitNameOpt.isEmpty()) {
                    player.sendMessage(UChat.miniMessage("<red>You need to specify a kit name!"));
                    return;
                }
                String kitName = kitNameOpt.get();
                if (player.getInventory().isEmpty()) {
                    player.sendMessage(UChat.miniMessage("<red>You cannot create a kit with an empty inventory!"));
                    return;
                }
                if (KitX.getKitManager().getKit(kitName) != null) {
                    player.sendMessage(UChat.miniMessage("<red>A kit with that name already exists!"));
                    return;
                }
                List<ItemStack> items = new ArrayList<>();
                player.getInventory().forEach(item -> {
                    if (item != null) items.add(item);
                });
                KitX.getKitManager().addKit(new Kit(kitName, null, 0, 0, items));
                player.sendMessage(UChat.miniMessage("<green>Successfully created kit <yellow>" + kitName + "<green>!"));
            }
            case "edit" -> {
                Optional<String> kitNameOpt = context.getOptional("kitName");
                if (kitNameOpt.isEmpty()) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>You need to specify a kit name!"));
                    return;
                }
                String kitName = kitNameOpt.get();
                if (KitX.getKitManager().getKit(kitName) == null) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>That kit doesn't exist!"));
                    return;
                }
                Kit kit = KitX.getKitManager().getKit(kitName);
                Optional<String> propertyOpt = context.getOptional("property");
                if (propertyOpt.isEmpty()) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>You need to specify a property!"));
                    return;
                }
                String property = propertyOpt.get();
                Optional<String> valueOpt = context.getOptional("value");
                if (valueOpt.isEmpty()) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>You need to specify a value!"));
                    return;
                }
                String value = valueOpt.get();
                switch (property) {
                    case "permission": {
                        KitX.getKitManager().updateKit(kitName, new Kit(
                                kit.name(),
                                value,
                                kit.limit(),
                                kit.cooldown(),
                                kit.items()
                        ));
                        context.getSender().sendMessage(UChat.miniMessage("<green>Successfully set permission for kit <yellow>" + kitName + "<green> to <yellow>" + value + "<green>!"));
                        break;
                    }
                    case "cooldown": {
                        int cooldown = getCooldown(value);
                        KitX.getKitManager().updateKit(kitName, new Kit(
                                kit.name(),
                                kit.permission(),
                                cooldown,
                                kit.limit(),
                                kit.items()
                        ));
                        context.getSender().sendMessage(UChat.miniMessage("<green>Successfully set cooldown for kit <yellow>" + kitName + "<green> to <yellow>" +
                                Duration.ofSeconds(cooldown).toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase() +
                                "<green>!"));
                        break;
                    }
                    case "limit": {
                        int limit = 0;
                        try {
                            limit = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            context.getSender().sendMessage(UChat.miniMessage("<red>Invalid limit!"));
                            return;
                        }
                        KitX.getKitManager().updateKit(kitName, new Kit(
                                kit.name(),
                                kit.permission(),
                                kit.cooldown(),
                                limit,
                                kit.items()
                        ));
                        context.getSender().sendMessage(UChat.miniMessage("<green>Successfully set limit for kit <yellow>" + kitName + "<green> to <yellow>" + limit + "<green>!"));
                        break;
                    }
                }
            }
            case "delete" -> {
                Optional<String> kitNameOpt = context.getOptional("kitName");
                if (kitNameOpt.isEmpty()) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>You need to specify a kit name!"));
                    return;
                }
                String kitName = kitNameOpt.get();
                if (KitX.getKitManager().getKit(kitName) == null) {
                    context.getSender().sendMessage(UChat.miniMessage("<red>That kit doesn't exist!"));
                    return;
                }
                KitX.getKitManager().removeKit(KitX.getKitManager().getKit(kitName));
                context.getSender().sendMessage(UChat.miniMessage("<green>Successfully deleted kit <yellow>" + kitName + "<green>!"));
            }
        }
    }

    private static int getCooldown(String value) {
        int cooldown = 0;
        try {
            if (value.toLowerCase().endsWith("s")) {
                cooldown = Integer.parseInt(value.substring(0, value.length() - 1));
            } else if (value.toLowerCase().endsWith("m")) {
                cooldown = Integer.parseInt(value.substring(0, value.length() - 1)) * 60;
            } else if (value.toLowerCase().endsWith("h")) {
                cooldown = Integer.parseInt(value.substring(0, value.length() - 1)) * 60 * 60;
            } else if (value.toLowerCase().endsWith("d")) {
                cooldown = Integer.parseInt(value.substring(0, value.length() - 1)) * 60 * 60 * 24;
            } else {
                cooldown = Integer.parseInt(value);
            }
        } catch (NumberFormatException ignored) {
        }
        return cooldown;
    }
}
