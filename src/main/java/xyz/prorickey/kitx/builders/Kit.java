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
    private final File kitFile;
    private final FileConfiguration kit;

    public Kit(String name, String permission, Inventory inv, Integer cooldown) {
        this.name = name;
        this.permission = permission;
        this.cooldown = cooldown;
        inv.forEach(i -> {
            if(i != null && i.getType() != Material.AIR) {
                this.items.add(i);
            }
        });
        kitFile = new File(KitX.getPlugin().getDataFolder() + "/kits/" + this.name + ".yml");
        if(!kitFile.exists()) {
            kitFile.getParentFile().mkdir();
            try {
                kitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        kit = YamlConfiguration.loadConfiguration(kitFile);
        kit.set("name", this.name);
        kit.set("permission", this.permission);
        kit.set("cooldown", this.cooldown);
        kit.set("items", this.items);
        try {
            kit.save(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        KitX.addKit(this.name, this);
    }
    public String getName() { return this.name; }
    public String getPermission() { return this.permission; }
    public List<ItemStack> getItems() { return this.items; }
    public void saveKit() {
        try {
            this.kit.save(this.kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getKit() { return this.kit; }
    public void deleteKit() {
        this.kitFile.delete();
        KitX.removeKit(this.name);
    }
    public Integer getCooldown() { return this.cooldown; }

}
