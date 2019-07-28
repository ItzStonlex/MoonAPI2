package ru.stonlex.api.bukkit.menus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.bukkit.menus.interfaces.InventoryButton;
import ru.stonlex.api.bukkit.menus.interfaces.InventoryInfo;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MoonInventory {

    private Inventory inventory;
    private final InventoryInfo info;
    private final Map<Integer, InventoryButton> buttons;

    @Getter
    private static final Map<String, MoonInventory> inventories = new HashMap<>();

    public MoonInventory(String title, int rows) {
        this.info = new MoonInventoryInfo(title, rows * 9, rows);
        this.buttons = new HashMap<>();
        this.inventory = Bukkit.createInventory(null, info.getSize(), info.getTitle());
    }

    /**
     * Передающийся метод по наследству.
     *
     * Выполняется при открытии инвентаря игроку,
     * указанному в аргументах.
     */
    public void onOpen(Player player) { }

    /**
     * Передающийся метод по наследству.
     *
     * Выполняется при закрытии инвентаря
     */
    public void onClose(Player player) { }

    /**
     * Установка названия инвенетарю
     */
    protected void setTitle(String title) {
        this.inventory = Bukkit.createInventory(null, inventory.getSize(), title);
    }

    /**
     * Установка количества строк в инвентаре
     */
    protected void setRows(int rows) {
        setSize(rows * 9);
    }

    /**
     * Установка размера инвентаря (rows * 9)
     */
    protected void setSize(int size) {
        this.inventory = Bukkit.createInventory(null, size, inventory.getTitle());
    }

    /**
     * Процесс генерации инвентаря, выставление предметов в сам инвентарь
     */
    public abstract void generateInventory(Player player);

    /**
     * Установка предмета в инвентарь
     */
    public void setItem(int slot, InventoryButton button) {
        buttons.put(slot, button);

        inventory.setItem(slot - 1, button.getItem());
    }

    /**
     * Установка предмета в инвентарь
     */
    public void setItem(int slot, ItemStack itemStack, Clickable<Player> clickable) {
        InventoryButton button = new MoonInventoryButton(itemStack, clickable);

        setItem(slot, button);
    }

    /**
     * Установка предмета в инвентарь
     */
    public void setItem(int slot, ItemStack itemStack) {
        InventoryButton button = new MoonInventoryButton(itemStack, player -> {});

        setItem(slot, button);
    }

    /**
     * Очистка предметов в инвентаре
     */
    public void clear() {
        buttons.clear();
        inventory.clear();
    }

    /**
     * Открытие инвентаря игроку
     */
    public void openInventory(Player player) {
        openInventory(player, true);
    }

    /**
     * Приватное открытие инвентаря игроку
     */
    private void openInventory(Player player, boolean isOpen) {
        this.generateInventory(player);

        if (isOpen) {
            player.openInventory(inventory);

            inventories.put(player.getName().toLowerCase(), this);
        }
    }

    /**
     * Обновлени инвентаря игроку
     */
    public void updateInventory(Player player) {
        clear();

        openInventory(player, false);
    }

    protected void updateInventory(Player player, Runnable command) {
        clear();

        command.run();

        openInventory(player, false);
    }



    @RequiredArgsConstructor
    @Getter
    public static class MoonInventoryButton implements InventoryButton {

        private final ItemStack item;
        private final Clickable<Player> command;
    }

    @RequiredArgsConstructor
    @Getter
    public static class MoonInventoryInfo implements InventoryInfo {

        private final String title;
        private final int size;
        private final int rows;
    }

}
