package xyz.prorickey.kitx.api;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final String name;
    private final String permission;
    private final Integer cooldown;
    private final Integer limit;
    private final List<String> items = new ArrayList<>();

    public Kit(String name, String permission, Integer limit, List<String> items, Integer cooldown) {
        this.name = name;
        this.permission = permission;
        this.cooldown = cooldown;
        this.limit = limit;
        this.items.addAll(items);
    }
    public String getName() { return this.name; }
    public String getPermission() { return this.permission; }
    public List<String> getItems() { return this.items; }
    public Integer getCooldown() { return this.cooldown; }
    public Integer getLimit() { return this.limit; }

}
