package ru.stonlex.api.bukkit.game.enums;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 21:13)
 */
public enum CageType {

    /**
     * С данным типом клетка примет образ
     * обычного WorldBorder`а, который для каждого
     * игрока будет виден по своему.
     */
    WORLD_BORDER,

    /**
     * Данный тип клетки создает ее
     * при помощи блоков вокруг игрока
     * или игроков, находяшихся в ней.
     */
    TYPE_BLOCKS
}
