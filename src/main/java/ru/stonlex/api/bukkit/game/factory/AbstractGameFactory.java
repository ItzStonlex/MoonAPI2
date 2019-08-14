package ru.stonlex.api.bukkit.game.factory;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.game.GameSettings;
import ru.stonlex.api.bukkit.game.enums.GameType;
import ru.stonlex.api.bukkit.game.player.GamePlayer;

import java.util.Collection;

public abstract class AbstractGameFactory implements Listener {

//---------------------------------------------------------------//
    protected final GameAPI GAME_API = MoonAPI.getGameAPI();

    protected final GameSettings GAME_SETTINGS = GAME_API.getGameSettings();
//---------------------------------------------------------------//

    /**
     * Инициализация некоторых настроек игры
     */
    public AbstractGameFactory(@NonNull GameType gameType, int startSecondsTimer) {

        GAME_SETTINGS.GAME_TYPE = gameType;

        GAME_SETTINGS.PLAYERS_IN_TEAM_COUNT = gameType.getPlayersInTeamCount();

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


    /**
     * Вызывается тогда, когда нужно оповестить всех
     * игроков онлайн о чем-либо
     *
     * @param text - Текст оповещения
     */
    protected void broadcastToAll(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            String message = GAME_SETTINGS.SUCCESSFULLY_PREFIX.concat(text);

            player.sendMessage(message);
        });
    }

    /**
     * Вызывается тогда, когда нужно оповестить каких-то
     * именно игроков о чем-либо
     *
     * @param players - Игроки, которых нужно оповестить
     * @param text - Текст оповещения
     */
    protected void broadcast(Collection<GamePlayer> players, String text) {
        players.forEach(gamePlayer -> {
            String message = GAME_SETTINGS.SUCCESSFULLY_PREFIX.concat(text);

            gamePlayer.getPlayer().sendMessage(message);
        });
    }

    /**
     * Вызывается тогда, когда нужно оповестить
     * наблюдателей в о чем-либо
     *
     * @param text - Текст оповещения
     */
    protected void broadcastToSpectators(String text) {
        broadcast(GAME_API.getAlivePlayers(), text);
    }

    /**
     * Вызывается тогда, когда нужно оповестить
     * игроков, что находятся в игре в о чем-либо
     *
     * @param text - Текст оповещения
     */
    protected void broadcastToPlayers(String text) {
        broadcast(GAME_API.getAlivePlayers(), text);
    }

}
