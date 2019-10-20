package ru.stonlex.api.bukkit.game.perk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.java.interfaces.Applicable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public final class PerkInfo {

    private ItemStack baseItem;

    private String permission;

    private int minLevel, maxLevel, price;

    private Applicable<Player> playerApplicable;

}
