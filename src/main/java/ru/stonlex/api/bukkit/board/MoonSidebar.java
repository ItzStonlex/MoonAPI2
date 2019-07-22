package ru.stonlex.api.bukkit.board;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.stonlex.api.bukkit.board.line.SidebarLine;
import ru.stonlex.api.bukkit.board.updater.SidebarUpdater;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MoonSidebar {

    @Getter
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    @Getter
    private Objective objective = scoreboard.registerNewObjective("board", "dummy");

    private final Updater updater = new Updater();

    private final Map<Integer, SidebarLine> LINES = new HashMap<>();
    private final Map<Long, SidebarUpdater> UPDATERS = new HashMap<>();



    /**
     * Создать новый Builder скорборда
     */
    public static MoonSidebarBuilder newBuilder() {
        return new MoonSidebarBuilder();
    }



    public MoonSidebar() {
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Показать скорборд игроку
     */
    public void show(Player player) {
        player.setScoreboard(this.scoreboard);

        if (!UPDATERS.isEmpty()) updater.start();
    }

    void startCommonUpdater() {
        updater.start();
    }

    /**
     * Установить DisplayName для скорборда
     */
    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }

    /**
     * Добавить строку в скорборд
     */
    public void addLine(int index, String text) {
        SidebarLine boardLine = LINES.get(index);

        if (boardLine != null) {
            boardLine.setText(text);
        } else LINES.put(index, new SidebarLine(this, index, text));
    }

    /**
     * Удалить строку из скорборд
     */
    public void removeLine(int index) {
        LINES.remove(index).hide();
    }

    /**
     * Добавить обновление в скорборд
     */
    void addUpdater(long time, SidebarUpdater boardUpdater) {
        UPDATERS.put(time, boardUpdater);
    }

    /**
     * Удалить скорборд у игрока
     */
    public void remove(Player player) {
        player.setScoreboard(null);
    }

    /**
     * Очистить скорборд
     */
    public void rejectBoard() {
        LINES.values().forEach(SidebarLine::hide);
        LINES.clear();
    }


    /**
     * Обновление скорборда
     */
    public class Updater extends Thread {

        private final AtomicLong time = new AtomicLong();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException ignored) {
                }

                if (time.incrementAndGet() == Long.MAX_VALUE) time.set(0);
                UPDATERS.entrySet()
                        .stream()
                        .filter((entry) -> time.get() % entry.getKey() == 0)
                        .forEach(entry -> {
                            if (entry.getValue() != null)
                                entry.getValue().update(MoonSidebar.this);
                        });
            }
        }
    }
}
