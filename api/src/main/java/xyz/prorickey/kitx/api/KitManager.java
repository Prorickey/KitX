package xyz.prorickey.kitx.api;

import java.util.Map;
import java.util.UUID;

public interface KitManager {

    Map<String, Kit> getAllKits();
    void addKit(Kit kit);
    void removeKit(Kit kit);
    Kit getKit(String name);

    void putPlayerOnCooldown(UUID uuid, Kit kit);
    void clearPlayerCooldown(UUID uuid, Kit kit);
    void updatePlayerLimit(UUID uuid, Kit kit, int change);

    Boolean checkPlayerOnCooldown(UUID uuid, Kit kit);
    int getPlayerLimit(UUID uuid, Kit kit);

    void loadPlayerData(UUID uuid);
    void unloadPlayerData(UUID uuid);

}
