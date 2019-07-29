package ru.stonlex.api.java.schedulers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SchedulerManager {

    private final TIntObjectMap<MoonTask> taskMap = new TIntObjectHashMap<>();

    private static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();

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
    public void registerScheduler(MoonTask scheduler) {
        scheduler.setSchedulerId(taskMap.size());

        if (taskMap.containsKey(scheduler.getSchedulerId())) {
            throw new RuntimeException("Task " + scheduler.getSchedulerId() + " has been registered.");
        }

        scheduler.setExecutorService(Executors.newSingleThreadScheduledExecutor());

        taskMap.put(scheduler.getSchedulerId(), scheduler);
    }



    public void async(Runnable command) {
        CACHED_THREAD_POOL.submit(command);
    }

}
