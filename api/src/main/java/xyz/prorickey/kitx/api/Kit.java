package xyz.prorickey.kitx.api;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Kit(
        String name,
        String permission,
        Integer cooldown,
        Integer limit,
        List<ItemStack> items
) {



}
