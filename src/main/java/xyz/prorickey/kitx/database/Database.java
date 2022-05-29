package xyz.prorickey.kitx.database;

import xyz.prorickey.kitx.builders.*;

import java.util.*;

public interface Database {

    void Database();

    Map<String, Kit> getKits();
    Kit getKit(String name);
    void saveKit(String name, Kit kit);
    void deleteKit(String name);

    Boolean onCooldownForKit(String name, UUID uuid);
    void putCooldownForKit(String name, UUID uuid);

}
