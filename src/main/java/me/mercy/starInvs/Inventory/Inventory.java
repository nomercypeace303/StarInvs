package me.mercy.starInvs.Inventory;

import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.StarInvs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final Plugin plugin = StarInvs.getPlugin();
    private static final Yml inventoryFile = StarInvs.getYmlInventory();

    public static final List<Integer> blockedSlots = new ArrayList<>();
    public static final List<ItemStack> inventoryItems = new ArrayList<>();
    public static final List<Globe> onInteractionCommand = new ArrayList<>();

    public static void setInventory(Player player) {
        if (!inventoryFile.isLoaded()) {
            plugin.getLogger().warning("Inventory file not loaded!");
            return;
        }

        ConfigurationSection components = inventoryFile.getSection("Components");
        if (components == null) {
            plugin.getLogger().warning("No components found in inventory file!");
            return;
        }

        plugin.getLogger().info("Loading inventory for: " + player.getName());

        for (String key : components.getKeys(false)) {
            ConfigurationSection section = components.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().warning("Component '" + key + "' is null, skipping...");
                continue;
            }

            String type = section.getString("Type");
            if (!"button".equalsIgnoreCase(type)) continue;

            ConfigurationSection attr = section.getConfigurationSection("Attributes");
            if (attr == null) {
                plugin.getLogger().warning("Component '" + key + "' has no Attributes, skipping...");
                continue;
            }

            int slot = attr.getInt("Slot", -1);
            if (slot < 0) {
                plugin.getLogger().warning("Component '" + key + "' has invalid slot, skipping...");
                continue;
            }

            ConfigurationSection itemSection = attr.getConfigurationSection("Item");
            if (itemSection == null) {
                plugin.getLogger().warning("Component '" + key + "' has no Item section, skipping...");
                continue;
            }

            // Build and set the item
            ItemStack item = new ItemGenerator(itemSection, plugin).autoBuildItem();
            player.getInventory().setItem(slot, item);
            inventoryItems.add(item);

            // Mark as blocked if needed
            if (attr.getBoolean("Locked", true)) blockedSlots.add(slot);

            // Register interactions
            registerInteractions(attr, key, slot);
        }
    }

    private static void registerInteractions(ConfigurationSection attr, String key, int slot) {
        ConfigurationSection right = attr.getConfigurationSection("OnInteraction.Right");
        ConfigurationSection left = attr.getConfigurationSection("OnInteraction.Left");

        if (right != null) clickInteraction(right, true, key, slot);
        if (left != null) clickInteraction(left, false, key, slot);
    }

    private static void clickInteraction(ConfigurationSection interaction, boolean clickType, String key, int slot) {
        if (!interaction.contains("Commands")) return;

        List<String> commands = interaction.getStringList("Commands");
        if (commands.isEmpty()) {
            plugin.getLogger().severe("Empty command list in " + inventoryFile.getPath() + " component: " + key);
            return;
        }

        Globe existing = onInteractionCommand.stream()
                .filter(g -> g.getSlot() == slot && g.isType() == clickType)
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.getList().addAll(commands);
        } else {
            onInteractionCommand.add(new Globe(slot, clickType, new ArrayList<>(commands)));
        }
    }

    public static void reloadInventory(Player player) {
        blockedSlots.clear();
        inventoryItems.clear();
        onInteractionCommand.clear();
        StarInvs.inventory.reload();
        setInventory(player);
    }
}
