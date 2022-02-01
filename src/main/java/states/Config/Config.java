package states.Config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import states.Main.AncapStates;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {

    private File cfgFile;

    private FileConfiguration cfg;

    private String fileName;

    public static Config MESSAGES_CONFIGURATION = new Config("Messages.yml");

    public static Config VALUES_CONFIGURATION = new Config("Values.yml");

    private Config(String fileName) {
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

    public String getString(String path) {
        return cfg.getString(path);
    }
}
