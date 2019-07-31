package ru.stonlex.api.test;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;
import ru.stonlex.api.bungee.inventory.item.BungeeMaterial;
import ru.stonlex.api.bungee.utility.BungeeItemUtil;

public class BungeeTestMenu extends BungeeMoonInventory {

    public BungeeTestMenu() {
        super("test", "Тестовый инвентарь на BungeeCord", 3);
    }

    @Override
    public void createInventory(ProxiedPlayer player) {
        setItem(14, BungeeItemUtil.getItemStack(BungeeMaterial.WOOD, "§eТестовый предмет",
                "§7Тестовая строка 1",
                "§8Тестовая строка 2",
                "§9Тестовая строка 3"), player1 -> {
            player.sendMessage("ого, ты че, кликнул? ну все пока я закрываю тебе инвентарь лошара");

            closeInventory(player);
        });
    }

}
