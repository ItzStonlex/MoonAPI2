package ru.stonlex.api.bukkit.board;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.board.objective.SidebarObjective;
import ru.stonlex.api.bukkit.board.updater.SidebarUpdater;

import java.util.function.Consumer;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 15:25)
 */
public class MoonSidebarBuilder {

    private final MoonSidebar sidebar;

    private final SidebarObjective sidebarObjective;

    private final SidebarUpdater sidebarUpdater;


    public MoonSidebarBuilder(Player player) {
        this.sidebar = new MoonSidebar();
        this.sidebarObjective = new SidebarObjective(player.getName(), "§6§lMoonStudio");
        this.sidebarUpdater = new SidebarUpdater(sidebar);

        sidebar.setObjective(sidebarObjective);

        sidebarObjective.create(player);
    }

    public MoonSidebarBuilder setDisplayName(String displayName) {
        sidebarObjective.setDisplayName(displayName, sidebar);

        return this;
    }

    public MoonSidebarBuilder setLine(int index, String line) {
        sidebar.setLine(index, line);

        return this;
    }

    public MoonSidebarBuilder newUpdater(@NonNull Consumer<MoonSidebar> task, long delay) {
        sidebarUpdater.newTask(task, delay);

        return this;
    }

    public void showPlayer(Player player) {
        sidebar.send(player);

        sidebarUpdater.start();
    }

}
