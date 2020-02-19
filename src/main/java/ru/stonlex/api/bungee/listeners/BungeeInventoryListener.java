package ru.stonlex.api.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;
import ru.stonlex.api.bungee.inventory.event.ProxyInventoryClickEvent;
import ru.stonlex.api.bungee.inventory.event.ProxyInventoryCloseEvent;
import ru.stonlex.api.bungee.inventory.interfaces.BungeeInventoryButton;
import ru.stonlex.api.bungee.inventory.item.BungeeMaterial;

public class BungeeInventoryListener implements Listener {

    @EventHandler
    public void onClick(ProxyInventoryClickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BungeeMoonInventory inventory = BungeeMoonInventory.PLAYER_INVENTORY_MAP.get(player);

        int slot = event.getSlot();

        if (event.getCurrentItem() == null || event.getCurrentItem().getMaterial() == BungeeMaterial.AIR || inventory == null || !inventory.getButtonMap().containsKey(slot)) {
            return;
        }

        BungeeInventoryButton button = inventory.getButtonMap().get(slot);

        button.getClickCommand().onClick(player);
    }

    @EventHandler
    public void onClose(ProxyInventoryCloseEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BungeeMoonInventory inventory = BungeeMoonInventory.PLAYER_INVENTORY_MAP.get(player);

        if (inventory == null) {
            return;
        }

        BungeeMoonInventory.PLAYER_INVENTORY_MAP.remove(player);

        inventory.onClose(player);
    }

}
