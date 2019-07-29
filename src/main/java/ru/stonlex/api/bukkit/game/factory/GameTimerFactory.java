package ru.stonlex.api.bukkit.game.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import ru.stonlex.api.java.schedulers.MoonTask;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class GameTimerFactory {

    private int seconds;

    private MoonTask timerTask;

    private final AbstractGameFactory gameFactory;


    /**
     * Запуск таймера в лобби
     */
    public void start() {
        this.timerTask = new MoonTask() {

            @Override
            public void run() {
                seconds--;

                /**
                 * Если время таймера истекло, то мы устанавливаем
                 * ему стандартное значение и запускаем игру
                 */
                if (seconds <= 0) {

                    seconds = gameFactory.GAME_SETTINGS.LOBBY_TIMER_START_SECONDS;
                    gameFactory.onStartGame();

                    cancel();

                    return;
                }

                if (seconds % 5 == 0 || seconds < 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> {

                        player.sendMessage(gameFactory.GAME_SETTINGS.SUCCESSFULLY_PREFIX + "Игра начнется через §e" + seconds + " секунд");

                        player.sendTitle(String.format("§6§l%s %s", gameFactory.GAME_SETTINGS.GAME_NAME, gameFactory.GAME_SETTINGS.GAME_TYPE.getType()),
                                "Начало игры через §c" + seconds);
                    });

                }

            }
        };

        timerTask.scheduleTimer(0, 1, TimeUnit.SECONDS);
    }

    /**
     * Выключение таймера в лобби.
     */
    public void stop() {
        timerTask.cancel();

        this.seconds = gameFactory.GAME_SETTINGS.LOBBY_TIMER_START_SECONDS;
    }

    /**
     * Получение количества оставшихся секунд
     */
    public int getSecondsLeft() {
        return seconds;
    }

}
