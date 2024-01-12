package xyz.prorickey.kitx.spigot;

import org.bukkit.Bukkit;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.api.KitManager;
import xyz.prorickey.kitx.spigot.database.Database;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitManagerImpl implements KitManager {

    private Database database;
    Map<String, Kit> kits;
    Map<UUID, Map<String, Long>> playerCooldowns = new HashMap<>();
    Map<UUID, Map<String, Integer>> playerLimits = new HashMap<>();

    public KitManagerImpl(Database database) {
        this.database = database;
        kits = database.getKits();
    }

    @Override
    public Map<String, Kit> getAllKits() { return kits; }

    @Override
    public void addKit(Kit kit) {
        kits.put(kit.name(), kit);
        database.saveKit(kit.name(), kit);
    }

    @Override
    public void removeKit(Kit kit) {
        kits.remove(kit.name());
        database.deleteKit(kit.name());
    }

    @Override
    public void updateKit(String name, Kit kit) {
        kits.put(name, kit);
        database.saveKit(name, kit);
    }

    @Override
    public Kit getKit(String name) {
        if(kits.containsKey(name)) return kits.get(name);
        else return database.getKit(name);
    }

    @Override
    public void putPlayerOnCooldown(UUID uuid, Kit kit) {
        playerCooldowns.get(uuid).put(kit.name(), Instant.now().getEpochSecond()+kit.cooldown());
        database.putCooldownForKit(kit.name(), uuid, Instant.now().getEpochSecond()+kit.cooldown());
    }

    @Override
    public void clearPlayerCooldown(UUID uuid, Kit kit) {
        if(playerCooldowns.containsKey(uuid)) playerCooldowns.get(uuid).remove(kit.name());
        database.putCooldownForKit(kit.name(), uuid, 0L);
    }

    @Override
    public void updatePlayerLimit(UUID uuid, Kit kit, int change) {
        playerLimits.get(uuid).put(kit.name(), playerLimits.get(uuid).getOrDefault(kit.name(), 0)+change);
        database.changeLimitForKit(kit.name(), uuid, change);
    }

    @Override
    public Boolean checkPlayerOnCooldown(UUID uuid, Kit kit) {
        if(!playerCooldowns.containsKey(uuid)) return true;
        else return playerCooldowns.get(uuid).get(kit.name()) != null &&
                playerCooldowns.get(uuid).get(kit.name()) >= Instant.now().getEpochSecond();
    }

    @Override
    public int getPlayerLimit(UUID uuid, Kit kit) {
        return playerLimits.get(uuid).getOrDefault(kit.name(), 0);
    }

    @Override
    public void loadPlayerData(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(KitX.getPlugin(), () -> {
            playerCooldowns.put(uuid, database.getCooldownsForPlayer(uuid));
            playerLimits.put(uuid, database.getLimitsForPlayer(uuid));
        });
    }

    @Override
    public void unloadPlayerData(UUID uuid) {
        playerCooldowns.remove(uuid);
        playerLimits.remove(uuid);
    }
}
