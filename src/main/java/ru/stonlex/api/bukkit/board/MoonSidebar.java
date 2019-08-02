package ru.stonlex.api.bukkit.board;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.board.line.SidebarLine;
import ru.stonlex.api.bukkit.board.objective.SidebarObjective;
import ru.stonlex.api.bukkit.modules.protocol.packet.AbstractPacket;
import ru.stonlex.api.bukkit.modules.protocol.packet.scoreboard.WrapperPlayServerScoreboardTeam;

import java.util.*;

public class MoonSidebar {

    private final Set<Player> players = new HashSet<>();
    private final Map<Integer, SidebarLine> lines = new HashMap<>();

    @Getter
    private SidebarObjective objective;

    public void setObjective(@NonNull final SidebarObjective objective) {
        this.objective = objective;

        for (Player player : unregisterForAll()) {
            send(player);
        }

        recreate();
    }

    public void setDisplayName(String displayName) {
        objective.setDisplayName(displayName, this);
    }

    public void setLine(int index, String text) {
        SidebarLine line = getLine(index);

        if (line == null) {
            lines.put(index, new SidebarLine(index, text, this));
        } else {
            line.setText(text);
        }
    }

    public SidebarLine getLine(int index) {
        return lines.get(index);
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public Map<Integer, SidebarLine> getLines() {
        return Collections.unmodifiableMap(lines);
    }

    public void broadcastPacket(@NonNull AbstractPacket packet) {
        players.forEach(packet::sendPacket);
    }

    public void unregister(@NonNull Player player) {
        Preconditions.checkState(players.contains(player),
                "Player %s is not receiving this sidebar.", player.getName());

        lines.values().forEach(line -> {
            line.getTeamPacket(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED).sendPacket(player);
            line.getScorePacket(EnumWrappers.ScoreboardAction.REMOVE).sendPacket(player);
        });

        objective.remove(player);
        players.remove(player);
    }

    public Collection<Player> unregisterForAll() {
        Set<Player> copied = new HashSet<>(players);
        players.forEach(this::unregister);

        return copied;
    }

    private void recreate() {
        lines.values().forEach(SidebarLine::show);
    }

    public void hide() {
        lines.values().forEach(SidebarLine::hide);
    }

    public void send(@NonNull Player... players) {
        for (Player player : players) {
            Preconditions.checkArgument(!this.players.contains(player),
                    "Player %s already receiving this sidebar.", player.getName());

            objective.create(player);

            lines.values().forEach(line -> {
                line.getTeamPacket(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED).sendPacket(player);
                line.getScorePacket(EnumWrappers.ScoreboardAction.CHANGE).sendPacket(player);
            });

            objective.show(player);
        }

        Collections.addAll(this.players, players);
    }
}
