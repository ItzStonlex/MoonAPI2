package ru.stonlex.api.bungee.utility;

import ru.stonlex.api.bungee.inventory.item.BungeeItemMeta;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;
import ru.stonlex.api.bungee.inventory.item.BungeeMaterial;

import java.util.Arrays;

public final class BungeeItemUtil {

    public static BungeeItemStack getPlayerSkull(String nick, String name, String... lore) {
        BungeeItemStack itemStack = new BungeeItemStack(BungeeMaterial.SKULL_ITEM, 3);
        BungeeItemMeta meta = itemStack.getItemMeta();

        meta.setSkullOwner(nick);
        meta.setName(name);
        meta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static BungeeItemStack getTextureSkull(String texture, String name, String... lore) {
        BungeeItemStack itemStack = new BungeeItemStack(BungeeMaterial.SKULL_ITEM, 3);
        BungeeItemMeta meta = itemStack.getItemMeta();

        meta.setSkullOwner(texture);
        meta.setName(name);
        meta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static BungeeItemStack getItemStack(BungeeMaterial material, int durability, int amount, String name, String... lore) {
        BungeeItemStack itemStack = new BungeeItemStack(material, durability, amount);
        BungeeItemMeta meta = itemStack.getItemMeta();

        meta.setName(name);
        meta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static BungeeItemStack getItemStack(BungeeMaterial material, String name, String... lore) {
        return getItemStack(material, 0, 1, name, lore);
    }

}
