package me.mercy.starInvs.Inventory;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.StarInvs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final Plugin plugin = StarInvs.getPlugin();
    private static final Yml inventoryFile = StarInvs.getYmlInventory();

    // Lista degli slot bloccati (per es. locked slots/backpack)
    public static final List<Integer> blockedSlots = new ArrayList<>();
    // Lista degli item caricati nell'inventario
    public static final List<ItemStack> inventoryItems = new ArrayList<>();

    /**
     * Popola l'inventario del player con i componenti definiti in inventory.yml
     * Carica sia i Button che i Backpack
     */
    public static void setInventory(Player player) {
        if (!inventoryFile.isLoaded()) {
            plugin.getLogger().warning("Inventory file not loaded!");
            return;
        }

        plugin.getLogger().info("Loading inventory for: " + player.getName());

        // Carica i button
        Button.setButton(player, inventoryFile);

        // Carica i backpack

    }

    /**
     * Ricarica completamente l'inventario del player
     */
    public static void reloadInventory(Player player) {
        blockedSlots.clear();
        inventoryItems.clear();

        StarInvs.inventory.reload();
        setInventory(player);
    }
}
