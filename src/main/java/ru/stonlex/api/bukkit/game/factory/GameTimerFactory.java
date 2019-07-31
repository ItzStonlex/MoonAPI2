package ru.stonlex.api.bukkit.game.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import ru.stonlex.api.java.schedulers.MoonTask;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class GameTimerFactory {

    @Getter
    private int secondsLeft;

    @Getter
    private MoonTask timerTask;

    @Getter
    private boolean timerWorked;

    private final AbstractGameFactory gameFactory;


    /**
     * Запуск таймера в лобби
     */
    public void start() {
        this.timerWorked = true;

        this.timerTask = new MoonTask() {

            @Override
            public void run() {
                secondsLeft--;

                /**
                 * Если время таймера истекло, то мы устанавливаем
                 * ему стандартное значение и запускаем игру
                 */
                if (secondsLeft <= 0) {

                    secondsLeft = gameFactory.GAME_SETTINGS.LOBBY_TIMER_START_SECONDS;
                    gameFactory.onStartGame();

                    cancel();

                    return;
                }

                if (secondsLeft % 5 == 0 || secondsLeft < 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> {

                        gameFactory.broadcastToAll("Игра начнется через §e" + secondsLeft + " секунд");

                        player.sendTitle(String.format("§6§l%s %s", gameFactory.GAME_SETTINGS.GAME_NAME, gameFactory.GAME_SETTINGS.GAME_TYPE.getType()),
                                "Начало игры через §c" + secondsLeft);
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
        this.timerWorked = false;

        timerTask.cancel();

        this.secondsLeft = gameFactory.GAME_SETTINGS.LOBBY_TIMER_START_SECONDS;
    }

}
