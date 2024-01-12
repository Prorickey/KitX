package xyz.prorickey.kitx.spigot.database;

import xyz.prorickey.kitx.api.Kit;

import java.util.*;

public interface Database {

    Map<String, Kit> getKits();
    Kit getKit(String name);
    void saveKit(String name, Kit kit);
    void updateKit(String name, Kit kit);
    void deleteKit(String name);

    void putCooldownForKit(String name, UUID uuid, Long nextUseTime);
    void changeLimitForKit(String name, UUID uuid, int change);

    Map<String, Long> getCooldownsForPlayer(UUID uuid);
    Map<String, Integer> getLimitsForPlayer(UUID uuid);

}
