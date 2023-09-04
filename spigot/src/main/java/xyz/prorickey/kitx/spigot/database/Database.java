package xyz.prorickey.kitx.spigot.database;

import xyz.prorickey.kitx.api.Kit;

import java.util.*;

public interface Database {

    Map<String, Kit> getKits();
    Kit getKit(String name);
    void saveKit(String name, Kit kit);
    void deleteKit(String name);

    Boolean onCooldownForKit(String name, UUID uuid);
    void putCooldownForKit(String name, UUID uuid);
    Boolean onLimitForKit(String name, UUID uuid);
    void addLimitForKit(String name, UUID uuid);

}
