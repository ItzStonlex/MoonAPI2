package ru.stonlex.api.bukkit.menus.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.java.interfaces.Clickable;

public interface InventoryButton {

    Clickable<Player> getCommand();
    ItemStack getItem();
}
