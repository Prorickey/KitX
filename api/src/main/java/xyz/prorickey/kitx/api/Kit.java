package xyz.prorickey.kitx.api;

import java.util.List;

public record Kit(
        String name,
        String permission,
        Integer cooldown,
        Integer limit,
        List<String> items
) {



}
