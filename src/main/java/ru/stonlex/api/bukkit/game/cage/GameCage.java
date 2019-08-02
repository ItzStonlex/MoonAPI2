package ru.stonlex.api.bukkit.game.cage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 21:12)
 */
public interface GameCage {

    /**
     * Создание клетки вокруг игрока
     *
     * @param player - Игрок
     * @param location - Локация, на кот
     * @param size - Размер клетки
     */
    void create(Player player, Location location, int size);

    /**
     * Удаление клетки
     *
     * @param player - Игрок
     */
    void remove(Player player);

    /**
     * Возвращает, сузествует ли клетка у игрока
     *
     * @param player - Игрок
     */
    boolean isActive(Player player);

}
