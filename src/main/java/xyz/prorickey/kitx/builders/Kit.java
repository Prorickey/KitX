package xyz.prorickey.kitx.builders;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.*;
import xyz.prorickey.kitx.*;

import java.io.*;
import java.util.*;

public class Kit {

    private final String name;
    private final String permission;
    private final Integer cooldown;
    private final List<ItemStack> items = new ArrayList<>();

    public Kit(String name, String permission, Inventory inv, Integer cooldown) {
        this.name = name;
        this.permission = permission;
        this.cooldown = cooldown;
        inv.forEach(i -> {
            if(i != null) {
                this.items.add(i);
            }
        });
    }
    public String getName() { return this.name; }
    public String getPermission() { return this.permission; }
    public List<ItemStack> getItems() { return this.items; }
    public Integer getCooldown() { return this.cooldown; }

}
