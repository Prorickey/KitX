package xyz.prorickey.kitx.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import xyz.prorickey.api.chat.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

public class SList extends SubCommand {
    public SList() { super("list", "To list all the kits", "kitx.subcommands.list", false); }
    @Override
    public void executor(String[] args, CommandSender sender) {
        Player p = (Player) sender;
        TextComponent comp = new TextComponent(Chat.format("\n&6All kits\n"));
        KitX.getDataManager().getKits().forEach((name, kit) -> {
            comp.addExtra(Chat.format("&e" + kit.getName() + " "));
            TextComponent cmdComp = new TextComponent(Chat.format("&7/kit " + kit.getName()));
            cmdComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.format("&eClick to get kit")).create()));
            cmdComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.getName()));
            comp.addExtra(cmdComp);
            comp.addExtra("\n");
        });
        p.spigot().sendMessage(comp);
    }
}
