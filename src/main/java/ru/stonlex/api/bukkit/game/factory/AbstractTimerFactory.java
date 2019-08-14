package ru.stonlex.api.bukkit.game.factory;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.game.GameSettings;

@Getter
public abstract class AbstractTimerFactory {

    private boolean timerWorked;

    private BukkitRunnable timerTask;


//---------------------------------------------------------------//
    protected final GameAPI GAME_API = MoonAPI.getGameAPI();

    protected final GameSettings GAME_SETTINGS = GAME_API.getGameSettings();
//---------------------------------------------------------------//


    /**
     * Вызывается, когда таймер в лобби тикает
     *
     * @param secondsLeft - Оставшееся время
     */
    public abstract void onTimerWork(int secondsLeft);


    /**
     * Запуск таймера в лобби
     */
    public void start() {
        this.timerWorked = true;

        this.timerTask = new BukkitRunnable() {

            int secondsLeft = MoonAPI.getGameAPI().getGameSettings().LOBBY_TIMER_START_SECONDS;

            @Override
            public void run() {
                AbstractGameFactory gameFactory = MoonAPI.getGameAPI().getGameFactory();
                GameSettings gameSettings = gameFactory.GAME_SETTINGS;

                secondsLeft--;

                /**
                 * Если время таймера истекло, то мы устанавливаем
                 * ему стандартное значение и запускаем игру
                 */
                if (secondsLeft <= 0) {
                    gameFactory.onStartGame();

                    secondsLeft = gameSettings.LOBBY_TIMER_START_SECONDS;

                    cancel();

                    return;
                }

                onTimerWork(secondsLeft);
            }
        };

        timerTask.runTaskTimer(MoonAPI.getPlugin(MoonAPI.class), 0, 20);
    }

    /**
     * Выключение таймера в лобби.
     */
    public void stop() {
        this.timerWorked = false;

        timerTask.cancel();
    }

}
