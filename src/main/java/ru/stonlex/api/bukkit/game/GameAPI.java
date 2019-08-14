package ru.stonlex.api.bukkit.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.cage.manager.CageManager;
import ru.stonlex.api.bukkit.game.enums.GamePlayerType;
import ru.stonlex.api.bukkit.game.factory.AbstractGameFactory;
import ru.stonlex.api.bukkit.game.factory.AbstractTimerFactory;
import ru.stonlex.api.bukkit.game.kit.manager.KitManager;
import ru.stonlex.api.bukkit.game.listeners.*;
import ru.stonlex.api.bukkit.game.perk.manager.PerkManager;
import ru.stonlex.api.bukkit.game.player.GamePlayer;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.*;
import java.util.stream.Collectors;

public final class GameAPI {

    @Getter
    private final CageManager cageManager        = new CageManager();

    @Getter
    private final PerkManager perkManager        = new PerkManager();

    @Getter
    private final KitManager kitManager          = new KitManager();

    @Getter
    private final GameSettings gameSettings      = new GameSettings();


    @Setter
    @Getter
    private AbstractGameFactory gameFactory = null;

    @Setter
    @Getter
    private AbstractTimerFactory timerFactory = null;


    private final Map<String, GamePlayer> gamePlayerMap = new HashMap<>();


    /**
     * Регистрация игровых листенеров, которые отслеживают
     * данные из GameSettings
     */
    public void registerGameListeners(Plugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager  .registerEvents  (new BlockListener(),           plugin);
        pluginManager  .registerEvents  (new DamageListener(),          plugin);
        pluginManager  .registerEvents  (new EntitySpawnListener(),     plugin);
        pluginManager  .registerEvents  (new FoodChangeListener(),      plugin);
        pluginManager  .registerEvents  (new GameStatusListener(),      plugin);
        pluginManager  .registerEvents  (new InteractListener(),        plugin);
        pluginManager  .registerEvents  (new ItemListener(),            plugin);
        pluginManager  .registerEvents  (new JoinListener(),            plugin);
        pluginManager  .registerEvents  (new LeavesDecayListener(),     plugin);
        pluginManager  .registerEvents  (new MoveListener(),            plugin);
        pluginManager  .registerEvents  (new WeatherChangeListener(),   plugin);
        pluginManager  .registerEvents  (new PlayerDeathListener(),     plugin);
    }

    /**
     * Получение игрока из кеша выживших по нику
     */
    public final GamePlayer getGamePlayer(String playerName) {
        return gamePlayerMap.get(playerName.toLowerCase());
    }

    /**
     * Получение игрока из кеша выживших по объекту
     */
    public final GamePlayer getGamePlayer(Player player) {
        return getGamePlayer(player.getName());
    }

    /**
     * Добавить игрока в игру
     */
    public void addPlayerInGame(String playerName) {
        gamePlayerMap.put(playerName.toLowerCase(), new GamePlayerImpl(playerName));
    }

    /**
     * Удалить игрока из игры
     */
    public void removePlayerInGame(String playerName) {
        GamePlayer gamePlayer = gamePlayerMap.get(playerName.toLowerCase());

        gamePlayer.setSpectator();
    }

    /**
     * Получение списка выживших игроков
     */
    public final Collection<GamePlayer> getAlivePlayers() {
        return Collections.unmodifiableCollection(gamePlayerMap.values());
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
    @Getter
    public class GamePlayerImpl implements GamePlayer {

        private final String name;

        private GamePlayerType type = GamePlayerType.PLAYER;

        @Setter
        private double multiple;

        @Override
        public Player getPlayer() {
            return Bukkit.getPlayer(name);
        }

        @Override
        public ItemStack getHead() {
            return ItemUtil.getPlayerSkull(name);
        }

        @Override
        public boolean isPlayer() {
            return type == GamePlayerType.PLAYER;
        }

        @Override
        public boolean isSpectator() {
            return !isPlayer();
        }

        @Override
        public void setSpectator() {
            if (isSpectator()) {
                throw new RuntimeException("player '" + name + "' is already a spectator");
            }

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
            getAlivePlayers().forEach(gamePlayer -> gamePlayer.getPlayer().hidePlayer(getPlayer()));

            getSpectatePlayers().forEach(player -> {
                getPlayer().showPlayer(player);
            });

            type = GamePlayerType.SPECTATOR;
        }

        @Override
        public void leave() {
            if (getPlayer() == null) {
                throw new RuntimeException("player is not online");
            }

            MoonAPI.getMessagingManager().redirectPlayer(getPlayer(), gameSettings.LOBBY_SERVER_NAME);
        }

    }

}
