package ru.stonlex.api.bukkit.board.manager;

import ru.stonlex.api.bukkit.board.MoonSidebar;
import ru.stonlex.api.bukkit.board.MoonSidebarBuilder;
import ru.stonlex.api.bukkit.types.CacheManager;

public final class SidebarManager extends CacheManager<MoonSidebar> {

    /**
     * Создать новый Builder скорборда
     */
    public MoonSidebarBuilder newBuilder() {
        return new MoonSidebarBuilder();
    }

    /**
     * Сохранение скорборда в кеш по его имени
     */
    public void cacheSidebar(String sidebarName, MoonSidebar moonSidebar) {
        cacheData(sidebarName.toLowerCase(), moonSidebar);
    }

    /**
     * Получение скорборда из кеша по его имени
     */
    public MoonSidebar getSidebar(String sidebarName) {
        return getCache(sidebarName.toLowerCase());
    }


}
