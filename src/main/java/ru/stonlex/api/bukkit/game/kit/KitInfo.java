package ru.stonlex.api.bukkit.game.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public final class KitInfo {

    private ItemStack helmetItem, chestplateItem, leggingsItem, bootsItem;

    private String permission;

    private int price;

    private List<ItemStack> itemList;

}
