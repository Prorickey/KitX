package xyz.prorickey.kitx.spigot;

import org.bukkit.Bukkit;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.api.KitManager;
import xyz.prorickey.kitx.spigot.database.Database;

import java.util.HashMap;
import java.util.Map;

public class KitManagerImpl implements KitManager {

    private Database database;
    Map<String, Kit> kits;

    public KitManagerImpl(Database database) {
        this.database = database;
        kits = database.getKits();
    }

    @Override
    public Map<String, Kit> getAllKits() { return kits; }

    @Override
    public void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
        database.saveKit(kit.getName(), kit);
    }

    @Override
    public void removeKit(Kit kit) {
        kits.remove(kit.getName());
        database.deleteKit(kit.getName());
    }

    @Override
    public Kit getKit(String name) {
        if(kits.containsKey(name)) return kits.get(name);
        else return database.getKit(name);
    }
}
