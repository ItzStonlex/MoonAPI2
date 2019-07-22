package ru.stonlex.api.bungee.listeners;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class BungeeAbstractMoonListener implements Listener {

    @Getter
    private final Plugin plugin;

    /**
     * При вызове этого класса, он автоматически регистрируется
     * как листенер в ядре.
     */
    public BungeeAbstractMoonListener(Plugin plugin) {
        this.plugin = plugin;

        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

}
