package ru.stonlex.api.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.stonlex.api.bukkit.board.manager.SidebarManager;
import ru.stonlex.api.bukkit.commands.CommandManager;
import ru.stonlex.api.bukkit.event.EventRegisterManager;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.hologram.HologramManager;
import ru.stonlex.api.bukkit.listeners.BungeeMessageListener;
import ru.stonlex.api.bukkit.listeners.PlayerListener;
import ru.stonlex.api.bukkit.menus.listener.InventoryListener;
import ru.stonlex.api.bukkit.menus.manager.InventoryManager;
import ru.stonlex.api.bukkit.messaging.MessagingManager;
import ru.stonlex.api.bukkit.modules.protocol.entity.listeners.FakeEntityClickListener;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;
import ru.stonlex.api.bukkit.tag.manager.TagManager;
import ru.stonlex.api.test.TestCommand;

public final class MoonAPI extends JavaPlugin {

    public static final String PLUGIN_MESSAGE_CHANNEL        = "MoonAPI";


    @Getter
    private static final CommandManager commandManager       = new CommandManager();

    @Getter
    private static final HologramManager hologramManager     = new HologramManager();

    @Getter
    private static final InventoryManager inventoryManager   = new InventoryManager();

    @Getter
    private static final GameAPI gameAPI                     = new GameAPI();

    @Getter
    private static final EventRegisterManager eventManager   = new EventRegisterManager();

    @Getter
    private static final SidebarManager sidebarManager       = new SidebarManager();

    @Getter
    private static final TagManager tagManager               = new TagManager();

    @Getter
    private static MessagingManager messagingManager         = null;

    @Getter
    private static VaultManager vaultManager                 = null;


    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "MoonAPI");
        getServer().getMessenger().registerIncomingPluginChannel(this, "MoonAPI", new BungeeMessageListener());

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new FakeEntityClickListener(this));

        messagingManager   = new MessagingManager(this);
        vaultManager       = new VaultManager();


        /* TEST */
        commandManager.registerCommand(this, new TestCommand(), "test", "тест");
        /* TEST */
    }

}
