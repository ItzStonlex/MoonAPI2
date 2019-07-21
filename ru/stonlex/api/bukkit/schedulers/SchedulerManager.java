package ru.stonlex.api.bukkit.schedulers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.concurrent.Executors;

public final class SchedulerManager {

    private final TIntObjectMap<MoonTask> taskMap = new TIntObjectHashMap<>();

    /**
     * Получение шедулера по его ID
     */
    public MoonTask getTask(int schedulerId) {
        return taskMap.get(schedulerId);
    }

    /**
     * shutdown шедулера по его ID
     */
    public void cancelTask(int schedulerId) {
        getTask(schedulerId).cancel();
    }

    /**
     * Регистрация шедулера (добавление в мапу и его инициализация)
     */
    void registerScheduler(MoonTask scheduler) {
        if (taskMap.containsKey(scheduler.getSchedulerId())) {
            throw new RuntimeException("Task " + scheduler.getSchedulerId() + " has been registered.");
        }

        scheduler.setSchedulerId(taskMap.size());
        scheduler.setExecutorService(Executors.newSingleThreadScheduledExecutor());

        taskMap.put(scheduler.getSchedulerId(), scheduler);
    }



    void async(Runnable command) {
        Executors.newCachedThreadPool().submit(command);
    }

}
