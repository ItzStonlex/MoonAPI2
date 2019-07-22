package ru.stonlex.api.bukkit.game.factory;

import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.game.GameSettings;
import ru.stonlex.api.bukkit.game.enums.GameType;
import ru.stonlex.api.bukkit.management.Management;

public abstract class AbstractGameFactory {

    protected final GameAPI GAME_API = Management.getGameAPI();

    protected final GameSettings GAME_SETTINGS = GAME_API.getGameSettings();

    protected final GameType GAME_TYPE = GAME_SETTINGS.GAME_TYPE;

    protected final GameTimer GAME_LOBBY_TIMER = new GameTimer(this);

    /**
     * Инициализация некоторых настроек игры
     */
    public AbstractGameFactory(GameType gameType, String gameName, String arenaWorldName, String lobbyServerName, int startSecondsTimer) {
        GAME_SETTINGS.ARENA_WORLD_NAME = arenaWorldName;

        GAME_SETTINGS.GAME_NAME = gameName;

        GAME_SETTINGS.GAME_TYPE = gameType;

        GAME_SETTINGS.SUCCESSFULLY_PREFIX = String.format("§6%s §8| §f", gameName);

        GAME_SETTINGS.PLAYERS_IN_TEAM_COUNT = gameType.getPlayersInTeamCount();

        GAME_SETTINGS.LOBBY_SERVER_NAME = lobbyServerName;

        GAME_SETTINGS.LOBBY_TIMER_START_SECONDS = startSecondsTimer;
    }

    /**
     * Вызывается тогда, когда таймер в лобби заканчивает свою
     * работу и начинается сама игра
     */
    public abstract void onStartGame();

    /**
     * Вызывается при окончании игры с победителями
     */
    public abstract void onStopGame(Player... winnerPlayers);

    /**
     * Вызывается тогда, когда необходимо выключить игру
     */
    public abstract void onStopGame();

}
