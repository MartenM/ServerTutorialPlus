package nl.martenm.servertutorialplus.helpers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config extends YamlConfiguration {

    private JavaPlugin plugin;
    private String fileName;

    public Config(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
        createFile();
    }

    private void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()) {
                if (plugin.getResource(fileName) != null) {
                    plugin.saveResource(fileName, false);
                } else {
                    save(file);
                }
            } else {
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            save(new File(plugin.getDataFolder(), fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
