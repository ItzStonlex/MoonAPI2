package ru.stonlex.api.bukkit.menus.manager;

import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.menus.MoonInventory;
import ru.stonlex.api.bukkit.types.CacheManager;
import ru.stonlex.api.java.interfaces.Applicable;

public final class InventoryManager extends CacheManager<MoonInventory> {

    /**
     * Создание инвентаря без использования абстракции.
     *
     * Все действия можно проводить через специальный для этого
     * Applicable, что указан в аргументах.
     */
    public void createInventory(String inventoryName, String title, int rows, Applicable<MoonInventory> inventoryApplicable) {
        MoonInventory inventory = new MoonInventory(title, rows) {
            @Override
            public void generateInventory(Player player) { }
        };

        cacheInventory(inventoryName, inventory);

        inventoryApplicable.apply(inventory);
    }

    /**
     * Кеширование инвентаря в мапу по его имени.
     */
    public void cacheInventory(String inventoryName, MoonInventory inventory) {
        cacheData(inventoryName.toLowerCase(), inventory);
    }

    /**
     * Получение инвентаря из кеша по его имени.
     */
    public MoonInventory getCachedInventory(String inventoryName) {
        return getCache(inventoryName.toLowerCase());
    }

}