package ru.stonlex.api.test.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.bukkit.menus.PagedMoonInventory;
import ru.stonlex.api.bukkit.utility.ItemUtil;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.*;

public class TestPagedMenu extends PagedMoonInventory {

    public TestPagedMenu() {
        super("Test paged inventory", 4);
    }

    @Override
    public LinkedHashMap<ItemStack, Clickable<Player>> initializeItems() {
        LinkedHashMap<ItemStack, Clickable<Player>> itemMap = new LinkedHashMap<>();

        for (int i = 0 ; i < 100 ; i++) {
            itemMap.put(ItemUtil.getItemStack(Material.STONE, String.format("%s%s", ChatColor.RED, i)), player -> {
                player.sendMessage("Ты кликнул по камню");

                player.closeInventory();
            });
        }

        return itemMap;
    }

    @Override
    public List<Integer> initializeSlots() {
        return Arrays.asList(11, 12, 13, 14, 15, 16, 17,
                             20, 21, 22, 23, 24, 25, 26);
    }

    @Override
    public void generateInventory(Player player) {
        setItem(5, ItemUtil.getItemStack(Material.SIGN, "Test item"));
    }

}
