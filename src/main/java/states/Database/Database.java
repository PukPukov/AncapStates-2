package states.Database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import states.Main.AncapStates;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Database {

    private File cfgFile;

    private FileConfiguration cfg;

    private String fileName;

    public static Database STATES_DATABASE;

    public static Database IDLINK_DATABASE;

    public static void setUp() {
        STATES_DATABASE = new Database("Database.yml");
        IDLINK_DATABASE = new Database("IdLinkDatabase.yml");
    }

    public Database(String fileName) {
        this.cfgFile = new File(AncapStates.getInstance().getDataFolder(), fileName);
        this.fileName = fileName;
        this.cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
        if (!cfgFile.exists()) {
            try (final InputStream in = AncapStates.getInstance().getResource(fileName)) {
                Files.copy(in, cfgFile.toPath());
                this.cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
                Bukkit.getServer().getConsoleSender().sendMessage("[AncapStates] Файл "+fileName+" успешно создан.");
            }
            catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage("[AncapStates] Произошла ошибка при создании файла "+fileName);
            }
        }
    }

    public void reload() {
        this.cfgFile = new File(AncapStates.getInstance().getDataFolder(), this.fileName);
        this.cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public void write(String path, String str) {
        this.cfg.set(path, str);
        if (!AncapStates.getInstance().isTest()) {
            this.save();
        }
    }

    public String getString(String path) {
        return this.cfg.getString(path);
    }

    public boolean isSet(String path) {
        return this.cfg.isSet(path);
    }

    public void save() {
        try {
            cfg.save(cfgFile);
        }
        catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Ошибка в сохранении файла "+fileName);
        }
    }

    public String[] getStringList(String path) {
        ConfigurationSection configurationSection = this.cfg.getConfigurationSection(path);
        if (configurationSection == null) {
            return new String[0];
        }
        return this.cfg.getConfigurationSection(path).getKeys(false).toArray(new String[0]);
    }

    public void writeNoSave(String path, String str) {
        this.cfg.set(path, str);
    }
}
