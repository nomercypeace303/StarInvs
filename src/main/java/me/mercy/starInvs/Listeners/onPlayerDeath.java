package me.mercy.starInvs.Listeners;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class onPlayerDeath implements Listener {

    private final Plugin plugin = StarInvs.getPlugin();
    private final Yml config = StarInvs.getYmlConfig();


    @EventHandler
    public void event(PlayerDeathEvent event) {
        event.getDrops().removeIf(item ->
                Inventory.inventoryItems.stream().anyMatch(i -> i.isSimilar(item))
        );
    }
}
