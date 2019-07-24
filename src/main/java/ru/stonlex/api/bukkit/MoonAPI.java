package ru.stonlex.api.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.stonlex.api.bukkit.board.manager.SidebarManager;
import ru.stonlex.api.bukkit.commands.CommandManager;
import ru.stonlex.api.bukkit.event.EventRegisterManager;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.hologram.HologramManager;
import ru.stonlex.api.bukkit.listeners.PlayerListener;
import ru.stonlex.api.bukkit.menus.listener.InventoryListener;
import ru.stonlex.api.bukkit.menus.manager.InventoryManager;
import ru.stonlex.api.bukkit.messaging.MessagingManager;
import ru.stonlex.api.bukkit.modules.protocol.entity.MoonFakeEntity;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;

import java.util.HashMap;
import java.util.Map;

public final class MoonAPI extends JavaPlugin {



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

    @Getter
    private static final SidebarManager sidebarManager     = new SidebarManager();


    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        registerProtocolListener();
    }

    /**
     * Регистрация пакетного листенера для прослушки клика
     * по MoonFakeEntity
     */
    private void registerProtocolListener() {
        Map<String, Long> cooldowns = new HashMap<>();

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                Player player = e.getPlayer();
                int entityId = e.getPacket().getIntegers().read(0);

                if (cooldowns.containsKey(player.getName())
                        && cooldowns.get(player.getName()) > System.currentTimeMillis()) {
                    return;
                }

                MoonFakeEntity fakeEntity = MoonFakeEntity.getEntityById(entityId);

                if (fakeEntity == null || fakeEntity.getClickable() == null) {
                    return;
                }

                fakeEntity.getClickable().onClick(player);

                cooldowns.put(player.getName(), System.currentTimeMillis() + 1000);
            }
        });
    }

}
