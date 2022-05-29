package xyz.prorickey.kitx.database;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import xyz.prorickey.api.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.io.*;
import java.util.*;

public class YAML implements Database {

    private static final Map<String, Kit> kits = new HashMap<>();
    private static final File dir = new File(KitX.getPlugin().getDataFolder() + "/yaml/");

    @Override
    public void Database() {
        File dir = new File(KitX.getPlugin().getDataFolder() + "/yaml/");
        dir.mkdirs();
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                if(file.getName().startsWith("kit_")) {
                    FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "yamlInventory");
                    yaml.getList("items").forEach(i -> {
                        ItemStack item = (ItemStack) i;
                        inv.addItem(item);
                    });
                    String permission = null;
                    if (yaml.get("permission") != null) {
                        permission = yaml.getString("permission");
                    }
                    Kit kit = new Kit(yaml.getString("name").toLowerCase(), permission, inv, yaml.getInt("cooldown"));
                    kits.put(yaml.getString("name").toLowerCase(), kit);
                }
            }
        }
    }

    @Override
    public Map<String, Kit> getKits() {
        return kits;
    }

    @Override
    public Kit getKit(String name) {
        if(kits.containsKey(name)) return kits.get(name);
        else {
            File file = new File(dir, "kit_" + name + ".yml");
            if(file.exists()) {
                FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "yamlInventory");
                yaml.getList("items").forEach(i -> {
                    ItemStack item = (ItemStack) i;
                    inv.addItem(item);
                });
                String permission = null;
                if (yaml.get("permission") != null) {
                    permission = yaml.getString("permission");
                }
                Kit kit = new Kit(yaml.getString("name").toLowerCase(), permission, inv, yaml.getInt("cooldown"));
                kits.put(yaml.getString("name").toLowerCase(), kit);
                return kit;
            } else return null;
        }
    }

    @Override
    public void saveKit(String name, Kit kit) {
        File file = new File(dir, "kit_" + name + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("name", kit.getName());
        yaml.set("permission", kit.getPermission());
        yaml.set("items", kit.getItems());
        yaml.set("cooldown", kit.getCooldown());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kits.put(name, kit);
    }

    @Override
    public void deleteKit(String name) {
        kits.remove(name);
        File file = new File(dir, "kit_" + name + ".yml");
        file.delete();
    }

    @Override
    public Boolean onCooldownForKit(String name, UUID uuid) {
        File file = new File(dir, "player_" + uuid.toString() + ".yml");
        if(file.exists()) {
            YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
            long time = player.getLong("cooldown." + name);
            return time > Time.getUnix();
        } else return false;
    }

    @Override
    public void putCooldownForKit(String name, UUID uuid) {
        File file = new File(dir, "player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        Kit kit = kits.get(name);
        if(kit != null) player.set("cooldown." + name, kit.getCooldown() + Time.getUnix());
    }

}
