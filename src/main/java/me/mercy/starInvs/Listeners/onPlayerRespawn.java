package me.mercy.starInvs.Listeners;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class onPlayerRespawn implements Listener {

    private final Plugin plugin = StarInvs.getPlugin();
    private final Yml config = StarInvs.getYmlConfig();

    @EventHandler
    public void event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Inventory.setInventory(player);
    }
}
