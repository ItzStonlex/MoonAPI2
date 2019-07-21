package ru.stonlex.api.bukkit.schedulers;

import lombok.Getter;
import lombok.Setter;
import ru.stonlex.api.bukkit.management.Management;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Setter
public abstract class MoonTask implements Runnable {

    @Getter
    private int schedulerId;

    private ScheduledExecutorService executorService;

    /**
     * Отмена шедулера
     */
    public void cancel() {
        Objects.requireNonNull(executorService, String.format("Task '%s' not a running!", schedulerId));

        executorService.shutdown();
    }

    /**
     * Запуск команды шедулера через определенное количество времени
     */
    public void scheduleLater(long delay, TimeUnit unit) {
        Management.getSchedulerManager().registerScheduler(this);

        executorService.schedule(this, delay, unit);
    }

    /**
     * Цикличный запуск команды шедулера через определенное количество времени
     */
    public void scheduleTimer(long delay, long period, TimeUnit unit) {
        Management.getSchedulerManager().registerScheduler(this);

        executorService.scheduleAtFixedRate(this, delay, period, unit);
    }

    /**
     * Асинхронный запуск команды шедулера через определенное количество времени
     */
    public void scheduleAsyncLater(long delay, TimeUnit unit) {
        Management.getSchedulerManager().async(() -> scheduleLater(delay, unit));
    }

    /**
     * Асинхронно-цикличный запуск команды шедулера через определенное количество времени
     */
    public void scheduleAsyncTimer(long delay, long period, TimeUnit unit) {
        Management.getSchedulerManager().async(() -> scheduleTimer(delay, period, unit));
    }

}
