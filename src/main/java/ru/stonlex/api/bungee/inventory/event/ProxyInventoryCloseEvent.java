package ru.stonlex.api.bungee.inventory.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;

public class ProxyInventoryCloseEvent extends ProxyInventoryEvent {

    public ProxyInventoryCloseEvent(BungeeMoonInventory inventory, ProxiedPlayer player) {
        super(inventory, player);
    }
}
