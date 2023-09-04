package xyz.prorickey.kitx.api;

import java.util.List;
import java.util.Map;

public interface KitManager {

    Map<String, Kit> getAllKits();
    void addKit(Kit kit);
    void removeKit(Kit kit);
    Kit getKit(String name);


}
