package xyz.prorickey.kitx.spigot.builders;

import org.bukkit.command.*;
import xyz.prorickey.kitx.spigot.cmds.CmdKitX;
import xyz.prorickey.kitx.spigot.cmds.*;

import java.util.*;

public class SubCommand {

    private final String name;
    private final String description;
    private final String permission;
    private final Boolean console;

    public SubCommand(String name, String description, String permission, Boolean console) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.console = console;
        CmdKitX.subs.put(name, this);
    }

    public void executor(String[] args, CommandSender sender) {

    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public String getPermission() { return this.permission; }
    public Boolean getConsole() { return this.console; }

}
