package me.mercy.starInvs;

import co.aikar.commands.PaperCommandManager;
import me.mercy.starInvs.Commands.Commands;
import me.mercy.starInvs.Commands.MyMessages;
import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class StarInvs extends JavaPlugin {

    public static Plugin plugin;
    public static Yml config = null;
    public static Yml inventory = null;
    public static Yml language = null;
    public static Yml backpack = null;

    private void firstLoad(){
        // copia il file dal jar solo se non esiste
        config = new Yml("config.yml", true);
        inventory = new Yml(getConfig().getString("Settings.Inventory"), true);
        language = new Yml(getConfig().getString("Settings.Language"), true);

        // aggiorna il flag e salva il config di Bukkit
        getConfig().set("Info.Firstload", false);
        saveConfig();

        // messaggio di primo avvio
        getLogger().info(" ");
        getLogger().info("----------------------------------------------------");
        getLogger().info("Welcome to StarInvs");
        getLogger().info("plugin made by: NoMercyPeace (ds)");
        getLogger().info("version: 1.0 beta");
        getLogger().info("check out the wiki: https://www.gofugyourself.com/");
        getLogger().info("----------------------------------------------------");
        getLogger().info(" ");

    }

    private void load(){
        // caricamento normale
        config = new Yml("config.yml");
        inventory = new Yml(getConfig().getString("Settings.Inventory"));
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Primo avvio?
        if (getConfig().getBoolean("Info.Firstload", true)) {
            firstLoad();
        } else {
            load();
        }

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Commands());

        // Messaggi personalizzati (opzionali)
        MyMessages.init();


        // setta messaggi globali ACF
        manager.getLocales().addMessage(Locale.ENGLISH, MyMessages.PERMISSIONDENIED.getMessageKey(), "<red>Non hai il permesso!</red>");

        Bukkit.getPluginManager().registerEvents(new onInventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerGameModeChange(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerRespawn(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Yml getYmlConfig() {
        return config;
    }

    public static Yml getYmlInventory() {return inventory;}
}
