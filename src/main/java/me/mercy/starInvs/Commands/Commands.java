package me.mercy.starInvs.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.mercy.starInvs.Inventory.Inventory;
import me.mercy.starInvs.StarInvs;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("invs")
public class Commands extends BaseCommand {

    private final Plugin plugin = StarInvs.getPlugin();

    MiniMessage mini = MiniMessage.miniMessage();


    @Default
    public void onDefault(Player player) {
        MyMessages.HELP.sendList(player,"player", player.getName(),
                "version", plugin.getConfig().getString("Info.Version"));
    }

    @CatchUnknown
    public void unknown(CommandSender sender, String command, String[] args) {
        MyMessages.UNKNOWNCOMMAND.send(sender);
    }


    // Subcomando principale /invs reload
    @Subcommand("reload")
    @CommandPermission("starinvs.reload")
    public void onReload(Player player) {
        MyMessages.HELP.sendList(player,"player", player.getName(),
                "version", plugin.getConfig().getString("Info.Version"));
    }

    // Sub-subcomando /invs reload language
    @Subcommand("reload inventory")
    @CommandPermission("starinvs.reload.inventory")
    public void onReloadInventory(Player player) {
        if (StarInvs.inventory.reload()){
            for (Player p : Bukkit.getOnlinePlayers()){
                Inventory.reloadInventory(p);
            }
            MyMessages.RELOADSUCCESS.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        } else {
            MyMessages.RELOADFAIL.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        }

    }

    // Sub-subcomando /invs reload language
    @Subcommand("reload inventory self")
    @CommandPermission("starinvs.reload.inventory")
    public void onReloadInventorySelf(Player player) {
        if (StarInvs.inventory.reload()){
            Inventory.reloadInventory(player);
            MyMessages.RELOADSUCCESS.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        } else {
            MyMessages.RELOADFAIL.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        }

    }

    @Subcommand("reload inventory all")
    @CommandPermission("starinvs.reload.inventory")
    public void onReloadInventoryAll(Player player) {
        if (StarInvs.inventory.reload()){
            for (Player p : Bukkit.getOnlinePlayers()){
                Inventory.reloadInventory(p);
            }
            MyMessages.RELOADSUCCESS.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        } else {
            MyMessages.RELOADFAIL.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        }

    }

    // Sub-subcomando /invs reload language
    @Subcommand("reload language")
    @CommandPermission("starinvs.reload.language")
    public void onReloadLanguage(Player player) {
        if (MyMessages.reloadMessagesConfig()){
            MyMessages.RELOADSUCCESS.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        } else {
            MyMessages.RELOADFAIL.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        }

    }

    // Sub-subcomando /invs reload config
    @Subcommand("reload config")
    @CommandPermission("starinvs.reload.config")
    public void onReloadConfig(Player player) {
        player.sendMessage("§bConfigurazione ricaricata con successo!");
        if(StarInvs.config.reload()){
            MyMessages.RELOADSUCCESS.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        } else {
            MyMessages.RELOADFAIL.send(player, "player", player.getName(),
                    "version", plugin.getConfig().getString("Info.Version"),
                    "reloadedfile", plugin.getConfig().getString("Settings.Language"));
        }
    }

    @Subcommand("reload all")
    @CommandPermission("starinvs.reload.all")
    public void onReloadAll(Player player){
        onReloadConfig(player);
        onReloadInventory(player);
        onReloadLanguage(player);
    }


}
