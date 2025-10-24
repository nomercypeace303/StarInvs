package me.mercy.starInvs.Listeners;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class onPlayerGameModeChange implements Listener {

    private final Plugin plugin = StarInvs.getPlugin();
    private final Yml config = StarInvs.getYmlConfig();

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().toString().equalsIgnoreCase("CREATIVE") || player.getGameMode().toString().equalsIgnoreCase("SPECTATOR")) {
            Inventory.setInventory(player);
        }
        if (player.getGameMode().toString().equalsIgnoreCase("SURVIVAL") || player.getGameMode().toString().equalsIgnoreCase("ADVENTURE")) {
            for (ItemStack item : Inventory.inventoryItems) {
                player.getInventory().remove(item);
            }
            Inventory.blockedSlots.clear();
        }
    }
}
