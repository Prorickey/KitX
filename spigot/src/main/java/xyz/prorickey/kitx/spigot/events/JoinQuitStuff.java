package xyz.prorickey.kitx.spigot.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.prorickey.kitx.spigot.KitX;

public class JoinQuitStuff implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        KitX.getKitManager().loadPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        KitX.getKitManager().unloadPlayerData(event.getPlayer().getUniqueId());
    }

}
