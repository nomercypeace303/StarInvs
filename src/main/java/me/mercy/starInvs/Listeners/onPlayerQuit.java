package me.mercy.starInvs.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        onInventoryClick.lastClick.remove(event.getPlayer().getUniqueId());
    }

}
