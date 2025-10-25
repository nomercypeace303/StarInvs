package me.mercy.starInvs.Listeners;

import me.mercy.starInvs.Inventory.Button;
import me.mercy.starInvs.Inventory.Globe;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class onInventoryClick implements Listener {

    public static final Map<UUID, Map<Integer, Long>> lastClick = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        int slot = event.getSlot();
        int spamThreshold = StarInvs.getPlugin().getConfig().getInt("Settings.SpamThreshold", 1000);

        if (player.getGameMode().toString().equals("SURVIVAL") || player.getGameMode().toString().equals("ADVENTURE") ) {
            if (Inventory.blockedSlots.contains(slot)) {
                event.setCancelled(true);
            }

            boolean isRightClick = event.getClick().isRightClick();
            boolean isLeftClick = event.getClick().isLeftClick();

            Globe globe = Button.onInteractionCommand.stream()
                    .filter(g -> g.getSlot() == slot && (
                            (g.isType() && isRightClick) ||
                                    (!g.isType() && isLeftClick)
                    ))
                    .findFirst()
                    .orElse(null);

            if (globe == null) return;

            long now = System.currentTimeMillis();
            Map<Integer, Long> playerClicks = lastClick.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
            long last = playerClicks.getOrDefault(slot, 0L);
            if (now - last < spamThreshold) {
                event.setCancelled(true);
                return;
            }
            playerClicks.put(slot, now);

            event.setCancelled(true); // importante per bloccare il click nativo

            for (String cmd : globe.getList()) {
                player.performCommand(cmd.replace("%player%", player.getName()));
            }
        }
    }
}
