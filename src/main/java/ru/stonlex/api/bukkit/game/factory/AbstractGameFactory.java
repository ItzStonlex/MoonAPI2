package ru.stonlex.api.bukkit.game.factory;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.game.GameSettings;
import ru.stonlex.api.bukkit.game.enums.GameType;
import ru.stonlex.api.bukkit.MoonAPI;

public abstract class AbstractGameFactory implements Listener {

    protected final GameAPI GAME_API = MoonAPI.getGameAPI();

    protected final GameSettings GAME_SETTINGS = GAME_API.getGameSettings();

    protected final GameType GAME_TYPE = GAME_SETTINGS.GAME_TYPE;

    protected final GameTimerFactory GAME_LOBBY_TIMER = new GameTimerFactory(this);

    /**
     * Инициализация некоторых настроек игры
     */
    public AbstractGameFactory(@NonNull GameType gameType, @NonNull String gameName,
                               @NonNull String arenaWorldName, @NonNull String lobbyServerName,
                               int startSecondsTimer) {

        GAME_SETTINGS.ARENA_WORLD_NAME = arenaWorldName;

        GAME_SETTINGS.GAME_NAME = gameName;

        GAME_SETTINGS.GAME_TYPE = gameType;

        GAME_SETTINGS.SUCCESSFULLY_PREFIX = String.format("§6%s §8| §f", gameName);

        GAME_SETTINGS.PLAYERS_IN_TEAM_COUNT = gameType.getPlayersInTeamCount();

        GAME_SETTINGS.LOBBY_SERVER_NAME = lobbyServerName;

        GAME_SETTINGS.LOBBY_TIMER_START_SECONDS = startSecondsTimer;

        Bukkit.getPluginManager().registerEvents(this, MoonAPI.getPlugin(MoonAPI.class));
    }

    /**
     * Вызывается тогда, когда таймер в лобби заканчивает свою
     * работу и начинается сама игра
     */
    public abstract void onStartGame();

    /**
     * Вызывается при окончании игры с победителями
     */
    public abstract void onStopGame(@NonNull Player... winnerPlayers);

    /**
     * Вызывается тогда, когда необходимо выключить игру
     */
    public abstract void onStopGame();

    /**
     * Вызывается тогда, когда умирает игрок
     */
    public abstract void onDeath(@NonNull Player player);

}
