package ru.stonlex.api.bukkit.board.manager;

import ru.stonlex.api.bukkit.board.MoonSidebarBuilder;
import ru.stonlex.api.bukkit.board.MoonSidebar;
import ru.stonlex.api.java.types.AbstractCacheManager;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 15:25)
 */
public final class SidebarManager extends AbstractCacheManager<MoonSidebar> {

    /**
     * Вызов нового билдера
     */
    public MoonSidebarBuilder newBuilder() {
        return new MoonSidebarBuilder();
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