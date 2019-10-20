package ru.stonlex.api.test.game;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.enums.GameStatus;
import ru.stonlex.api.bukkit.game.enums.GameType;
import ru.stonlex.api.bukkit.game.factory.AbstractGameFactory;
import ru.stonlex.api.bukkit.game.kit.MoonKit;
import ru.stonlex.api.bukkit.game.kit.manager.KitManager;
import ru.stonlex.api.bukkit.game.player.GamePlayer;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 29.07.2019 19:00)
 */
public class TestGame extends AbstractGameFactory {

    private final MoonKit PLAYER_DEFAULT_KIT = GAME_API.getKitManager().newBuilder("PlayerKit")

            .setHelmetItem(ItemUtil.getItemStack(Material.LEATHER_HELMET, "§eШапка"))
            .setChestplateItem(ItemUtil.getItemStack(Material.LEATHER_CHESTPLATE, "§eКуртка"))

            .setItemList(Lists.newArrayList(ItemUtil.getItemStack(Material.WOOD_SWORD, "§eДеревянный меч"),
                    ItemUtil.getItemStack(Material.WOOD_PICKAXE, "§eДеревянная кирка"),
                    ItemUtil.getItemStack(Material.WOOD_AXE, "§eДеревянный топор")))
            .build();


    public TestGame() {
        super(GameType.SOLO, 30);

        GAME_SETTINGS.ARENA_WORLD_NAME = "Floris";
        GAME_SETTINGS.GAME_NAME = "SkyWars";
        GAME_SETTINGS.LOBBY_SERVER_NAME = "SWLobby-3";
        GAME_SETTINGS.SUCCESSFULLY_PREFIX = "§f[§cSkyWars§f] ";
        GAME_SETTINGS.ERROR_PREFIX = GAME_SETTINGS.SUCCESSFULLY_PREFIX.concat("§c");

        GAME_API.setGameFactory(this);
        GAME_API.setTimerFactory(new TestTimer());
    }

    private void setupStartSettings() {
        GAME_SETTINGS.PLAYER_JOIN = false;
        GAME_SETTINGS.LEAVES_DECAY = false;
        GAME_SETTINGS.BLOCK_BREAK = true;
        GAME_SETTINGS.BLOCK_PLACE = true;
        GAME_SETTINGS.PLAYER_DAMAGE_BY_ENTITY = true;
        GAME_SETTINGS.PLAYER_DAMAGE_FALL = true;
        GAME_SETTINGS.PLAYER_FOOD_CHANGE = true;
        GAME_SETTINGS.PLAYER_DROP_ITEM = true;
        GAME_SETTINGS.PLAYER_PICKUP_ITEM = true;

        GAME_SETTINGS.GAME_STATUS = GameStatus.GAME_STARTED;
    }

    private void setupStopSettings() {
        GAME_SETTINGS.PLAYER_JOIN = false;
        GAME_SETTINGS.LEAVES_DECAY = false;
        GAME_SETTINGS.BLOCK_BREAK = false;
        GAME_SETTINGS.BLOCK_PLACE = false;
        GAME_SETTINGS.PLAYER_DAMAGE_BY_ENTITY = false;
        GAME_SETTINGS.PLAYER_DAMAGE_FALL = false;
        GAME_SETTINGS.PLAYER_FOOD_CHANGE = false;
        GAME_SETTINGS.PLAYER_DROP_ITEM = false;
        GAME_SETTINGS.PLAYER_PICKUP_ITEM = false;

        GAME_SETTINGS.GAME_STATUS = GameStatus.RESTART;
    }

    @Override
    public void onStartGame() {
        setupStartSettings();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Игра началась!");

            PLAYER_DEFAULT_KIT.giveToPlayer(player);

            //TODO: Телепортация на локации
        });
    }

    @Override
    public void onStopGame(@NonNull Player... winnerPlayers) {
        onStopGame();

        broadcastToAll("Победители: §7" + Joiner.on("§f, §7").join(winnerPlayers));

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(GAME_SETTINGS.END_SPAWN_LOCATION);

            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        });
    }

    @Override
    public void onStopGame() {
        setupStopSettings();

        broadcastToAll("Игра окончена!");

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

                    gamePlayer.leave();
                });
            }
        }.runTaskLater(MoonAPI.getPlugin(MoonAPI.class), 500);
    }

    @Override
    public void onDeath(@NonNull Player player) {
        Player playerKiller = player.getKiller();
        VaultManager vaultManager = MoonAPI.getVaultManager();

        GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

        gamePlayer.setSpectator();

        if (playerKiller != null) {
            broadcastToPlayers(vaultManager.getVaultPlayer(playerKiller).getPrefix() + playerKiller.getName()
                    + " §fубил " + vaultManager.getVaultPlayer(player).getPrefix() + player.getName());
        } else {
            broadcastToPlayers(vaultManager.getVaultPlayer(player).getPrefix() + player.getName()
                    + " §fпокончил жизнь самоубийством");
        }
    }

}
