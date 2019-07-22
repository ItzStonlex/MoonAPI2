package ru.stonlex.api.bukkit;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.stonlex.api.bukkit.commands.CommandManager;
import ru.stonlex.api.bukkit.event.EventRegisterManager;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.hologram.HologramManager;
import ru.stonlex.api.bukkit.listeners.PlayerListener;
import ru.stonlex.api.bukkit.menus.listener.InventoryListener;
import ru.stonlex.api.bukkit.menus.manager.InventoryManager;
import ru.stonlex.api.bukkit.messaging.MessagingManager;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;
import ru.stonlex.api.bukkit.schedulers.SchedulerManager;

public final class MoonAPI extends JavaPlugin {


    @Getter
    private static final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private static final CommandManager commandManager     = new CommandManager();

    @Getter
    private static final VaultManager vaultManager         = new VaultManager();

    @Getter
    private static final HologramManager hologramManager   = new HologramManager();

    @Getter
    private static final InventoryManager inventoryManager = new InventoryManager();

    @Getter
    private static final MessagingManager messagingManager = new MessagingManager(MoonAPI.getPlugin(MoonAPI.class));

    @Getter
    private static final GameAPI gameAPI                   = new GameAPI(MoonAPI.getPlugin(MoonAPI.class));

    @Getter
    private static final EventRegisterManager eventManager = new EventRegisterManager();


    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

}
