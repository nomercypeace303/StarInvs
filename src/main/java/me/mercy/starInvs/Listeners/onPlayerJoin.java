package me.mercy.starInvs.Listeners;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class onPlayerJoin implements Listener {

    private final Plugin plugin = StarInvs.getPlugin();
    private final Yml config = StarInvs.getYmlConfig();

    @EventHandler
    public void event(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().toString().equals("SURVIVAL")||player.getGameMode().toString().equals("ADVENTURE")){
            String inventoryFilePath = plugin.getConfig().getString("Settings.Inventory");
            plugin.getLogger().info("Loading inventory from: " + config.getPath() + " ( " + inventoryFilePath + " )");
            Inventory.setInventory(player);
        }
    }
}
