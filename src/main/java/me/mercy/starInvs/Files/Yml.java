package me.mercy.starInvs.Files;

import me.mercy.starInvs.StarInvs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Yml {

    private final Plugin plugin = StarInvs.getPlugin();
    private File rawFile;
    private YamlConfiguration ymlFile;
    private String path = "";
    private String name = "";
    private String usedSpace = "";
    private boolean loaded = false;

    /**
     * Constructor
     */
    public Yml(String filePath) {
        this.rawFile = new File(plugin.getDataFolder().toString() + "/" + filePath );
        try {
            loadAll();
            loaded = true;
        } catch (IOException | InvalidConfigurationException e) {
            loaded = false;
            throw new RuntimeException(e);
        }


    }

    public Yml(String filePath, boolean firstLoad){
        this.rawFile = new File(plugin.getDataFolder().toString() + "/" + filePath );
        try {
            if (firstLoad) {
                firstLoad(filePath);
            } else {
                loadAll();
            }
            loaded = true;
        } catch (IOException | InvalidConfigurationException e) {
            loaded = false;
            throw new RuntimeException(e);
        }
    }


    /**
     * Primo caricamento: crea o forza i file di default dal jar.
     */
    public void firstLoad(String filePath) throws IOException, InvalidConfigurationException {
        if (!rawFile.exists()) {
            // se non esiste, copialo prima dal jar
            loadDefaults(filePath);
        }

        // ora il file esiste (copiato o già presente)
        loadDirectories();
        forceLoadDefaults(filePath);
        loadAll();
    }


    /**
     * Carica normalmente il file senza toccare i default.
     */
    public void loadAll() throws IOException, InvalidConfigurationException {
        if (rawFile.exists()) {
            loadFile();
            loadSpecifics();
        } else {
            plugin.getLogger().severe("Couldn't find file: " + rawFile.getPath());
        }
    }

    /**
     * Carica il file YML nel sistema di configurazione Bukkit.
     */
    public void loadFile() throws IOException, InvalidConfigurationException {
        this.ymlFile = new YamlConfiguration();
        this.ymlFile.load(rawFile);
    }

    /**
     * Aggiorna info sul file (path, nome, dimensione).
     */
    public void loadSpecifics() {
        path = rawFile.getPath();
        name = rawFile.getName();
        usedSpace = rawFile.length() + " bytes";
    }

    /**
     * Copia il file di default dal jar solo se non esiste.
     */
    public void loadDefaults(String jarPath) {
        if (!rawFile.exists()) {
            plugin.getLogger().info("Copying default config from jar: " + jarPath);
            plugin.saveResource(jarPath, false);
        }
    }

    /**
     * Forza la copia del file dal jar, anche se già esiste.
     */
    public void forceLoadDefaults(String jarPath) {
        plugin.getLogger().warning("Overwriting default file from jar: " + jarPath);
        plugin.saveResource(jarPath, true);
    }

    /**
     * Crea le directory mancanti.
     */
    public void loadDirectories() {
        File parent = rawFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            plugin.getLogger().warning("Failed to create directories for: " + parent.getPath());
        }
    }

    /**
     * Ricarica il file YAML da disco.
     */
    public boolean reload() {
        if (path == null || path.isEmpty()) {
            plugin.getLogger().severe("Cannot reload: path is empty.");
            return false;
        }

        rawFile = new File(path);
        if (rawFile.exists()) {
            ymlFile = YamlConfiguration.loadConfiguration(rawFile);
            loadSpecifics();
            plugin.getLogger().info("Reloaded file: " + name);
            return true;
        } else {
            plugin.getLogger().severe("Error reloading file: " + path);
            return false;
        }
    }

    // --- Getters ---
    public YamlConfiguration getYmlFile() {
        return ymlFile;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getUsedSpace() {
        return usedSpace;
    }

    public boolean isLoaded() {return loaded;}

    /**
     * Ottiene una sezione del file, con controllo null.
     */
    public ConfigurationSection getSection(String inFilePath) {
        ConfigurationSection section = ymlFile.getConfigurationSection(inFilePath);
        if (section == null) {
            plugin.getLogger().severe("The section '" + inFilePath + "' does not exist in file: " + name);
        }
        return section;
    }

    /**
     * Ottiene un intero dal file con log di errore se mancante.
     */
    public int getInt(String inFilePath) {
        if (ymlFile.isInt(inFilePath)) {
            return ymlFile.getInt(inFilePath);
        } else {
            plugin.getLogger().severe("The integer '" + inFilePath + "' does not exist in file: " + name);
            return 0;
        }
    }

    /**
     * Ottiene una stringa dal file con log di errore se mancante.
     */
    public String getString(String inFilePath) {
        String value = ymlFile.getString(inFilePath);
        if (value != null) {
            return value;
        } else {
            plugin.getLogger().severe("The String '" + inFilePath + "' does not exist in file: " + name);
            return "";
        }
    }

    /**
     * Ottiene un boolean dal file con log di errore se mancante.
     */
    public boolean getBoolean(String inFilePath) {
        if (ymlFile.isBoolean(inFilePath)) {
            return ymlFile.getBoolean(inFilePath);
        } else {
            plugin.getLogger().severe("The Boolean '" + inFilePath + "' does not exist in file: " + name);
            return false;
        }
    }

    /**
     * Ottiene un intero dal file con log di errore e valore di default se mancante.
     */
    public int getInt(String inFilePath, int defaultValue) {
        if (ymlFile.isInt(inFilePath)) {
            return ymlFile.getInt(inFilePath);
        } else {
            plugin.getLogger().warning("The integer '" + inFilePath + "' does not exist in file: " + name + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Ottiene una stringa dal file con log di errore e valore di default se mancante.
     */
    public String getString(String inFilePath, String defaultValue) {
        String value = ymlFile.getString(inFilePath);
        if (value != null) {
            return value;
        } else {
            plugin.getLogger().severe("The String '" + inFilePath + "' does not exist in file: " + name + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Ottiene un boolean dal file con log di errore e valore di default se mancante.
     */
    public boolean getBoolean(String inFilePath, boolean defaultValue) {
        if (ymlFile.isBoolean(inFilePath)) {
            return ymlFile.getBoolean(inFilePath);
        } else {
            plugin.getLogger().severe("The Boolean '" + inFilePath + "' does not exist in file: " + name + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
}
