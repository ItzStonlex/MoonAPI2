package ru.stonlex.api.bungee.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BungeeConfig {

    private final Plugin plugin;

    private Configuration configuration;

    public BungeeConfig(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void saveDefaultConfig() {
        saveResource("config.yml");
        reloadConfig();
    }

    public Configuration getResourceConfig(String resourceName) {
        try {
            return YamlConfiguration.getProvider(YamlConfiguration.class)
                    .load(plugin.getDataFolder().toPath().resolve(resourceName).toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveResource(String resourceName) {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }

            Path configPath = plugin.getDataFolder().toPath().resolve(resourceName);

            if (Files.exists(configPath)) {
                return;
            }

            Files.copy(plugin.getResourceAsStream(resourceName), configPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            configuration = YamlConfiguration.getProvider(YamlConfiguration.class)
                    .load(plugin.getDataFolder().toPath().resolve("config.yml").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return configuration;
    }

}
