package ru.stonlex.api.test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.enums.GameStatus;
import ru.stonlex.api.bukkit.game.enums.GameType;
import ru.stonlex.api.bukkit.game.factory.AbstractGameFactory;
import ru.stonlex.api.bukkit.game.kit.MoonKit;
import ru.stonlex.api.bukkit.game.kit.manager.KitManager;
import ru.stonlex.api.bukkit.game.player.GamePlayer;
import ru.stonlex.api.bukkit.modules.vault.VaultManager;
import ru.stonlex.api.bukkit.utility.ItemUtil;
import ru.stonlex.api.java.schedulers.MoonTask;

import java.util.concurrent.TimeUnit;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 29.07.2019 19:00)
 */
public class TestGame extends AbstractGameFactory {

    /**
     * Инициализация некоторых настроек игры
     */
    public TestGame() {
        super(GameType.SOLO, 30);

        GAME_SETTINGS.ARENA_WORLD_NAME = "Floris";
        GAME_SETTINGS.GAME_NAME = "SkyWars";
        GAME_SETTINGS.LOBBY_SERVER_NAME = "SWLobby-3";
    }

    @Override
    public void onStartGame() {
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

        MoonKit playerKit = KitManager.newBuilder("PlayerKit")
                .setHelmetItem(ItemUtil.getItemStack(Material.LEATHER_HELMET, "§eШапка"))
                .setChestplateItem(ItemUtil.getItemStack(Material.LEATHER_CHESTPLATE, "§eКуртка"))

                .setItemList(Lists.newArrayList(ItemUtil.getItemStack(Material.WOOD_SWORD, "§eДеревянный меч"),
                        ItemUtil.getItemStack(Material.WOOD_PICKAXE, "§eДеревянная кирка"),
                        ItemUtil.getItemStack(Material.WOOD_AXE, "§eДеревянный топор")))
                .build();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Игра началась!");

            playerKit.giveToPlayer(player);

            //TODO: Телепортация на локации
        });
    }

    @Override
    public void onStopGame(@NonNull Player... winnerPlayers) {
        GAME_SETTINGS.GAME_STATUS = GameStatus.RESTART;

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Игра окончена!");
            player.sendMessage(GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Победители: §7" + Joiner.on("§f, §7").join(winnerPlayers));

            player.teleport(GAME_SETTINGS.END_SPAWN_LOCATION);

            GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

            gamePlayer.setSpectator();
        });

        new MoonTask() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

                    gamePlayer.leave();
                });
            }
        }.scheduleLater(5, TimeUnit.SECONDS);
    }

    @Override
    public void onStopGame() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Игра окончена!");

            GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

            gamePlayer.leave();
        });
    }

    @Override
    public void onDeath(@NonNull Player player) {
        GamePlayer gamePlayer = GAME_API.getGamePlayer(player);

        gamePlayer.setSpectator();

        Player playerKiller = player.getKiller();

        VaultManager vaultManager = MoonAPI.getVaultManager();

        if (playerKiller != null) {
            broadcastToPlayers(vaultManager.getVaultPlayer(playerKiller).getPrefix() + playerKiller.getName()
                    + " §fубил " + vaultManager.getVaultPlayer(player).getPrefix() + player.getName());
        } else {
            broadcastToPlayers(vaultManager.getVaultPlayer(player).getPrefix() + player.getName()
                    + " §fпокончил жизнь самоубийством");
        }
    }

}