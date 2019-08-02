package ru.stonlex.api.bukkit.game.team;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.game.enums.TeamType;
import ru.stonlex.api.bukkit.game.player.GamePlayer;

import java.util.List;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 21:17)
 */
public interface GameTeam {

    int getId();

    TeamType getEnum();

    Location getSpawnLocation();

    Location getDefenseLocation();

    List<GamePlayer> getPlayersInTeam();

    boolean isPlayerInTeam(String playerName);

    void addPlayerToTeam(String playerName);

    void removePlayerInTeam(String playerName);

    void breakDefense();


    /**
     * Возвращает, есть ли игрок в команде
     *
     * @param player - Игрок
     */
    default boolean isPlayerInTeam(Player player) {
        return isPlayerInTeam(player.getName());
    }

    /**
     * Возвращает, есть ли игрок в команде
     *
     * @param gamePlayer - Игрок
     */
    default boolean isPlayerInTeam(GamePlayer gamePlayer) {
        return isPlayerInTeam(gamePlayer.getPlayer());
    }


    /**
     * Добавить игрока в команду
     *
     * @param player - Игрок
     */
    default void addPlayerToTeam(Player player) {
        addPlayerToTeam(player.getName());
    }

    /**
     * Добавить игрока в команду
     *
     * @param gamePlayer - Игрок
     */
    default void addPlayerToTeam(GamePlayer gamePlayer) {
        addPlayerToTeam(gamePlayer.getPlayer());
    }


    /**
     * Удалить игрока из команды
     *
     * @param player - Игрок
     */
    default void removePlayerInTeam(Player player) {
        removePlayerInTeam(player.getName());
    }

    /**
     * Удалить игрока из команды
     *
     * @param gamePlayer - Игрок
     */
    default void removePlayerInTeam(GamePlayer gamePlayer) {
        removePlayerInTeam(gamePlayer.getPlayer());
    }

}
