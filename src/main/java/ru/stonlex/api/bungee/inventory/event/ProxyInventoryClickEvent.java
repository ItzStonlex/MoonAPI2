package ru.stonlex.api.bungee.inventory.event;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;

@Getter
public class ProxyInventoryClickEvent extends ProxyInventoryEvent {

    private final int slot;

    private final BungeeItemStack currentItem;


    public ProxyInventoryClickEvent(BungeeMoonInventory inventory, ProxiedPlayer player,
                                    int slot, BungeeItemStack currentItem) {
        super(inventory, player);

        this.slot = slot;
        this.currentItem = currentItem;
    }

}
