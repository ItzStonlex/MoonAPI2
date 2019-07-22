package ru.stonlex.api.bukkit.test;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.menus.MoonInventory;
import ru.stonlex.api.bukkit.utility.ItemUtil;

public class TestMenu extends MoonInventory {

    public TestMenu() {
        super("Test inventory", 3);
    }

    @Override
    public void generateInventory(Player player) {
        setItem(1, ItemUtil.getItemStack(Material.WOOD, "Test item", "Slot a item: 1"), player1 -> {
            player1.sendMessage("Ты кликнул по предмету на слоту 1");

            player1.closeInventory();
        });
    }

    @Override
    public void onOpen(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Inventory has been opened!");
    }

    @Override
    public void onClose(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Inventory has been closed!");
    }

}
