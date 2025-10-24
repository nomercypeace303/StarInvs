package me.mercy.starInvs.Commands;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import me.mercy.starInvs.Files.Yml;
import me.mercy.starInvs.StarInvs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public enum MyMessages implements MessageKeyProvider {
    HELP,
    PERMISSIONDENIED,
    UNKNOWNCOMMAND,
    RELOADSUCCESS,
    RELOADFAIL,
    MESSAGENOTFOUND; // esempio messaggio d’errore

    private final MessageKey key;
    private static final MiniMessage mini = MiniMessage.miniMessage();
    private static FileConfiguration messagesConfig;
    private static final Plugin plugin = StarInvs.getPlugin();

    // Costruttore enum
    MyMessages() {
        this.key = MessageKey.of(this.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public MessageKey getMessageKey() {
        return key;
    }

    /**
     * Inizializza il file dei messaggi. Va chiamato nel onEnable() della main class.
     */
    public static void init() {

        messagesConfig = new Yml(plugin.getConfig().getString("Settings.Language")).getYmlFile();
    }

    /**
     * Restituisce il messaggio come Component MiniMessage, sostituendo eventuali placeholder.
     */
    public Component resolve(Component defaultComponent, Object... placeholders) {
        if (messagesConfig == null) {
            return defaultComponent; // fallback se non inizializzato
        }

        String path = this.name().toLowerCase(Locale.ROOT);
        String msg = messagesConfig.getString(path, null);

        if (msg == null) {
            msg = defaultComponent != null ? defaultComponent.toString() : "<red>Message not found!";
        }

        // Sostituisci i placeholder tipo %player%
        if (placeholders != null && placeholders.length % 2 == 0) {
            for (int i = 0; i < placeholders.length; i += 2) {
                String key = placeholders[i].toString();
                String value = placeholders[i + 1].toString();
                msg = msg.replace("%" + key + "%", value);
            }
        }

        return mini.deserialize(msg);
    }

    /**
     * Invia il messaggio a un CommandSender
     */
    public void send(CommandSender sender, Object... placeholders) {
        sender.sendMessage(resolve(null, placeholders));
    }

    /**
     * Invia una lista di messaggi (List<String>) a un CommandSender
     */
    public void sendList(CommandSender sender, Object... placeholders) {
        if (messagesConfig == null) return;

        String path = this.name().toLowerCase(Locale.ROOT);
        if (!messagesConfig.isList(path)) {
            sender.sendMessage(mini.deserialize("<red>Message list not found: " + path));
            return;
        }

        for (String line : messagesConfig.getStringList(path)) {
            if (placeholders != null && placeholders.length % 2 == 0) {
                for (int i = 0; i < placeholders.length; i += 2) {
                    String key = placeholders[i].toString();
                    String value = placeholders[i + 1].toString();
                    line = line.replace("%" + key + "%", value);
                }
            }
            sender.sendMessage(mini.deserialize(line));
        }
    }

    /**
     * Restituisce il messaggio come stringa per log / console
     */
    public String asString(Object... placeholders) {
        return resolve(null, placeholders).toString();
    }

    public static boolean reloadMessagesConfig() {
        try {
            messagesConfig = new Yml(plugin.getConfig().getString("Settings.Language")).getYmlFile();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("[StarInvs] Failed to reload messages: " + e.getMessage());
            return false;
        }
    }


}
