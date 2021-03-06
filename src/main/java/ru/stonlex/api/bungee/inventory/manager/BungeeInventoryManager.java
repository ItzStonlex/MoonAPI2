package ru.stonlex.api.bungee.inventory.manager;

import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;
import ru.stonlex.api.java.types.AbstractCacheManager;

public final class BungeeInventoryManager extends AbstractCacheManager<BungeeMoonInventory> {

    /**
     * Получение инвентаря из кеша
     */
    public BungeeMoonInventory getInventory(String inventoryName) {
        return getCache(inventoryName.toLowerCase());
    }

    /**
     * Занесение инвентаря в кеш
     */
    public void cacheInventory(String inventoryName, BungeeMoonInventory moonInventory) {
        cacheData(inventoryName.toLowerCase(), moonInventory);
    }

}
