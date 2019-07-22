package ru.stonlex.api.bukkit.board;

import ru.stonlex.api.bukkit.board.updater.SidebarUpdater;
import ru.stonlex.api.java.interfaces.Builder;

import java.util.HashMap;
import java.util.Map;

public final class MoonSidebarBuilder implements Builder<MoonSidebar> {

    private MoonSidebar board = null;
    private String displayName;

    private final Map<Integer, String> cache = new HashMap<>();

    /**
     * Установить DisplayName для скорборда
     */
    public MoonSidebarBuilder setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    /**
     * Добавить строку в скорборд
     */
    public MoonSidebarBuilder addLine(int index, String line) {
        cache.put(index, line);

        return this;
    }

    /**
     * Добавить обновление в скорборд
     */
    public MoonSidebarBuilder addUpdater(long time, SidebarUpdater boardUpdater) {
        if (this.board == null) this.board = new MoonSidebar();

        this.board.addUpdater(time, boardUpdater);

        return this;
    }

    /**
     * Построить и создать скорборд
     */
    @Override
    public MoonSidebar build() {
        if (this.board == null) this.board = new MoonSidebar();

        cache.forEach((index, line) -> this.board.addLine(index, line));
        this.board.setDisplayName(this.displayName);
        this.board.startCommonUpdater();

        return this.board;
    }
}
