package ru.stonlex.api.bungee.inventory.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;

@RequiredArgsConstructor
@Getter
public abstract class ProxyInventoryEvent extends Event {

    private final BungeeMoonInventory inventory;

    private final ProxiedPlayer player;

}
