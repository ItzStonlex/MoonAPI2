package ru.stonlex.api.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import ru.stonlex.api.bukkit.listeners.PlayerListener;
import ru.stonlex.api.bukkit.menus.listener.InventoryListener;

public class MoonAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

}
