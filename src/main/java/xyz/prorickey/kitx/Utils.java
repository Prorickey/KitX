package xyz.prorickey.kitx;

import org.bukkit.*;

import java.time.*;
import java.util.*;

public class Utils {
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static long getUnix() { return Instant.now().getEpochSecond(); }
    public enum TabCompleteType {
        CONTAINS, SEARCH
    }
    public static List<String> tabCompletions(TabCompleteType type, String currentArg, List<String> completions) {
        if (currentArg.length() <= 0) { return completions; }
        currentArg = currentArg.toLowerCase(Locale.ROOT);
        List<String> returnedCompletions = new ArrayList<>();
        if (type == TabCompleteType.CONTAINS) {
            for (String str : completions) {
                if (str.toLowerCase(Locale.ROOT).contains(currentArg)) {
                    returnedCompletions.add(str);
                }
            }
        }else if (type == TabCompleteType.SEARCH) {
            for (String str : completions) {
                if (str.toLowerCase(Locale.ROOT).startsWith(currentArg)) {
                    returnedCompletions.add(str);
                }
            }
        }
        return returnedCompletions;
    }
    public static List<String> tabCompletionsContains(String currentArg, List<String> completions) { return tabCompletions(TabCompleteType.CONTAINS, currentArg, completions); }
    public static List<String> tabCompletionsSearch(String currentArg, List<String> completions) { return tabCompletions(TabCompleteType.SEARCH, currentArg, completions); }
    public static ArrayList<String> emptyList = new ArrayList<>();
    public static ArrayList<String> emptyList() { return new ArrayList<>(); }
}
