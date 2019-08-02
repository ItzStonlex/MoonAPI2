package ru.stonlex.api.bukkit.board.manager;

import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.board.MoonSidebarBuilder;
import ru.stonlex.api.bukkit.board.MoonSidebar;
import ru.stonlex.api.java.types.CacheManager;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 15:25)
 */
public final class SidebarManager extends CacheManager<MoonSidebar> {

    /**
     * Вызов нового билдера
     *
     * @param player - Игрок
     */
    public MoonSidebarBuilder newBuilder(Player player) {
        return new MoonSidebarBuilder(player);
    }



    /**
     * Кеширование скорборда
     *
     * @param sidebarName - Кешированное имя скорборда.
     * @param moonSidebar - Скорборд, который нужно закешировать.
     */
    public void cacheSidebar(String sidebarName, MoonSidebar moonSidebar) {
        cacheData(sidebarName.toLowerCase(), moonSidebar);
    }

    /**
     * Получение скорборда из кеша
     *
     * @param sidebarName - Имя получаемого скорборда
     */
    public MoonSidebar getSidebar(String sidebarName) {
        return getCache(sidebarName.toLowerCase());
    }

}
