package ru.stonlex.api.bukkit.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.kit.manager.KitManager;
import ru.stonlex.api.bukkit.game.listeners.BlockListener;
import ru.stonlex.api.bukkit.game.listeners.InteractListener;
import ru.stonlex.api.bukkit.game.listeners.JoinListener;
import ru.stonlex.api.bukkit.game.listeners.LeavesDecayListener;
import ru.stonlex.api.bukkit.game.perk.manager.PerkManager;
import ru.stonlex.api.bukkit.game.player.GamePlayer;
import ru.stonlex.api.bukkit.management.Management;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class GameAPI {

    private final MoonAPI moonAPI;

    @Getter
    private final PerkManager perkManager   = new PerkManager();

    @Getter
    private final KitManager kitManager     = new KitManager();

    @Getter
    private final GameSettings gameSettings = new GameSettings();

    @Getter
    private final Map<String, GamePlayer> playersInGameMap = new HashMap<>();

    /**
     * Регистрация игровых листенеров, которые отслеживают
     * данные из GameSettings
     */
    public void registerGameListeners() {
        moonAPI.getServer().getPluginManager().registerEvents(new BlockListener(),       moonAPI);
        moonAPI.getServer().getPluginManager().registerEvents(new InteractListener(),    moonAPI);
        moonAPI.getServer().getPluginManager().registerEvents(new LeavesDecayListener(), moonAPI);
        moonAPI.getServer().getPluginManager().registerEvents(new JoinListener(),        moonAPI);
    }

    /**
     * Получение игрока из кеша выживших по нику
     */
    public final GamePlayer getGamePlayer(String playerName) {
        return playersInGameMap.get(playerName.toLowerCase());
    }

    /**
     * Получение игрока из кеша выживших по объекту
     */
    public final GamePlayer getGamePlayer(Player player) {
        return getGamePlayer(player.getName());
    }

    /**
     * Получение списка выживших игроков
     */
    public final Collection<GamePlayer> getAlivePlayers() {
        return Collections.unmodifiableCollection(playersInGameMap.values());
    }

    /**
     * Получение списка наблюдателей за игрой
     */
    public final List<Player> getSpectatePlayers() {
        return Bukkit.getOnlinePlayers().stream()

                .filter(player -> getGamePlayer(player) == null)
                .collect(Collectors.toList());
    }



    @RequiredArgsConstructor
    public class GamePlayerImpl implements GamePlayer {

        private final String playerName;

        private double multiple;

        @Override
        public String getName() {
            return playerName;
        }

        @Override
        public Player getPlayer() {
            return moonAPI.getServer().getPlayer(playerName);
        }

        @Override
        public ItemStack getHead() {
            return ItemUtil.getPlayerSkull(playerName);
        }

        @Override
        public double getMultiple() {
            return multiple;
        }

        @Override
        public boolean isPlayer() {
            return playersInGameMap.containsKey(playerName.toLowerCase());
        }

        @Override
        public boolean isSpectator() {
            return !isPlayer();
        }

        @Override
        public void setSpectator() {
            if (isSpectator()) {
                throw new RuntimeException("player '" + playerName + "' is already has been spectator");
            }

            playersInGameMap.remove(playerName.toLowerCase());

            if (getPlayer() == null) {
                throw new RuntimeException("player is not online");
            }

            //Title
            getPlayer().sendTitle("§cВы умерли!",
                        "§fТеперь Вы наблюдаете за игрой");

            //Эффекты
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 3));
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 3));

            //Полет
            getPlayer().setAllowFlight(true);
            getPlayer().setFlying(true);

            //Предметы в инвентарь
            getPlayer().getInventory().clear();

            getPlayer().getInventory().setItem(0, gameSettings.SPECTATOR_TELEPORT_ITEM);
            getPlayer().getInventory().setItem(8, gameSettings.LEAVE_IN_GAME_ITEM);

            //Телепортация на нужную локацию
            getPlayer().teleport(gameSettings.SPECTATOR_SPAWN_LOCATION);

            //Скрытие игрока от всех, кроме наблюдателей
            playersInGameMap.values().forEach(gamePlayer -> {
                gamePlayer.getPlayer().hidePlayer(getPlayer());
            });

            getSpectatePlayers().forEach(player -> {
                player.showPlayer(getPlayer());
                getPlayer().showPlayer(player);
            });
        }

        @Override
        public void setMultiple(int multiple) {
            this.multiple = multiple;
        }

        @Override
        public void leave() {
            if (getPlayer() == null) {
                throw new RuntimeException("player is not online");
            }

            Management.getMessagingManager().redirectPlayer(getPlayer(), gameSettings.LOBBY_SERVER_NAME);
        }

    }

}
