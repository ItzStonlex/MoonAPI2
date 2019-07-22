package ru.stonlex.api.bukkit.management;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.commands.CommandManager;
import ru.stonlex.api.bukkit.event.EventRegisterManager;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.hologram.HologramManager;
import ru.stonlex.api.bukkit.menus.manager.InventoryManager;
import ru.stonlex.api.bukkit.messaging.MessagingManager;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;
import ru.stonlex.api.bukkit.schedulers.SchedulerManager;

@UtilityClass
public class Management {

    @Getter
    private final SchedulerManager schedulerManager = new SchedulerManager();

    @Getter
    private final CommandManager commandManager     = new CommandManager();

    @Getter
    private final VaultManager vaultManager         = new VaultManager();

    @Getter
    private final HologramManager hologramManager   = new HologramManager();

    @Getter
    private final InventoryManager inventoryManager = new InventoryManager();

    @Getter
    private final MessagingManager messagingManager = new MessagingManager(MoonAPI.getPlugin(MoonAPI.class));

    @Getter
    private final GameAPI gameAPI                   = new GameAPI(MoonAPI.getPlugin(MoonAPI.class));

    @Getter
    private final EventRegisterManager eventManager = new EventRegisterManager();

}
