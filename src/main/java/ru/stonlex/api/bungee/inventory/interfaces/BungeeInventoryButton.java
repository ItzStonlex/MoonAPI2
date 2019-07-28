package ru.stonlex.api.bungee.inventory.interfaces;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;
import ru.stonlex.api.java.interfaces.Clickable;

public interface BungeeInventoryButton {

    Clickable<ProxiedPlayer> getClickCommand();
    BungeeItemStack getItemStack();
}
