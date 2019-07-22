package ru.stonlex.api.bukkit.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.bukkit.utility.ItemUtil;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PagedMoonInventory extends MoonInventory {

    private Map<ItemStack, Clickable<Player>> itemStacks;
    private List<Integer> slots;
    private int backSlot;
    private int nextSlot;
    private int page;
    private int pages;
    private String title;

    public PagedMoonInventory(String title) {
        this(title, 3);
    }

    public PagedMoonInventory(String title, int rows) {
        this(title, rows, rows * 9 - 5, rows * 9 - 3);
    }

    public PagedMoonInventory(int rows) {
        this("", rows);
    }

    public PagedMoonInventory(String title, int rows, int backSlot, int nextSlot) {
        super(title, rows);
        this.title = title;
        this.itemStacks = this.loadItems();
        this.slots = this.loadSlots();
        this.backSlot = backSlot;
        this.nextSlot = nextSlot;
        this.page = 0;
        this.pages = this.itemStacks.size() / this.slots.size();
    }

    private void backward(Player player) {
        this.page--;

        updateInventory(player);
    }

    private void forward(Player player) {
        this.page++;

        updateInventory(player);
    }

    /**
     * Обновление инвентаря
     */
    @Override
    public void updateInventory(Player player) {
        this.generate(player);
        super.updateInventory(player);
    }

    private List<ItemStack> list;

    /**
     * Генерация страничного инвентаря и процесс его заполнения
     */
    private void generate(Player player) {

        if (list == null) {
            list = new ArrayList<>(itemStacks.keySet());
        }

        this.setTitle(title + " | Страница " + (page + 1));

        if (page != this.pages) {
            this.setItem(this.nextSlot, ItemUtil.getItemStack(Material.ARROW, "§eВперед"), player1 -> this.forward(player));
        } else if (page != 0) {
            this.setItem(this.backSlot, ItemUtil.getItemStack(Material.ARROW, "§eНазад"), player1 -> this.backward(player));
        }

        for (int i = 0; i < this.slots.size(); ++i) {
            int index = page * this.slots.size() + i;

            if (list.size() <= index) {
                return;
            }

            ItemStack itemStack = list.get(index);
            int slot = this.slots.get(i);

            this.setItem(slot, itemStack, this.itemStacks.get(itemStack));
        }
    }

    @Override
    public void openInventory(Player player) {
        this.generate(player);
        super.openInventory(player);
    }

    public abstract Map<ItemStack, Clickable<Player>> loadItems();

    public abstract List<Integer> loadSlots();
}
