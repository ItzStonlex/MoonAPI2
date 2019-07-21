package ru.stonlex.api.bukkit.menus.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.stonlex.api.bukkit.menus.MoonInventory;
import ru.stonlex.api.bukkit.menus.interfaces.InventoryButton;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MoonInventory inventory = MoonInventory.getInventories().get(player.getName().toLowerCase());

        int slot = e.getSlot();

        if (e.getCurrentItem().getType() == Material.AIR || inventory == null || !inventory.getButtons().containsKey(slot + 1)) {
            return;
        }

        InventoryButton button = inventory.getButtons().get(slot + 1);

        button.getCommand().onClick(player);
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        MoonInventory inventory = MoonInventory.getInventories().get(player.getName().toLowerCase());

        if (inventory == null) {
            return;
        }

        MoonInventory.getInventories().remove(player.getName().toLowerCase());

        inventory.onClose(player);
    }

}
