package ru.stonlex.api.bukkit.board.updater;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.stonlex.api.bukkit.board.MoonSidebar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SidebarUpdater {

    private final Multimap<Long, Consumer<MoonSidebar>> tasks
            = Multimaps.synchronizedSetMultimap(Multimaps.newSetMultimap(new HashMap<>(), HashSet::new));;

    @Getter
    private final MoonSidebar sidebar;

    private Thread executionThread;

    @Getter
    private volatile boolean started;

    public Multimap<Long, Consumer<MoonSidebar>> getTasks() {
        return Multimaps.unmodifiableMultimap(tasks);
    }

    public void clearTasks() {
        this.tasks.clear();
    }

    public void stop() {
        Preconditions.checkState(isStarted(), "Updating is not started.");

        executionThread.interrupt();
        if (!executionThread.isInterrupted()) executionThread.stop();
    }

    public SidebarUpdater newTask(@NonNull Consumer<MoonSidebar> task, long delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Delay value must be positive");
        }

        tasks.put(delay, task);

        return this;
    }

    public void start() {
        Preconditions.checkState(!isStarted(), "Update task is already started.");

        startTaskExecution();

        started = true;
    }

    private void startTaskExecution() {
        AtomicLong time = new AtomicLong();

        Runnable updater = () -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ignored) {

                }

                tasks.asMap().entrySet()
                        .stream()
                        .filter(entry -> time.get() % entry.getKey() == 0)
                        .forEach(entry -> entry.getValue().forEach(consumer -> consumer.accept(sidebar)));

                time.incrementAndGet();
            }
        };

        executionThread = new Thread(updater, String.format("%s-Updater", sidebar.getObjective().getName()));
        executionThread.start();
    }

}
