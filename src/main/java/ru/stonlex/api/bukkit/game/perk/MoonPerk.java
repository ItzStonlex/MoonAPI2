package ru.stonlex.api.bukkit.game.perk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@AllArgsConstructor
@Getter
public class MoonPerk {

    private final String perkName;

    @Setter
    private PerkInfo perkInfo;


    /**
     * Применить перк игроку
     */
    public void applyToPlayer(Player player) {
        perkInfo.getPlayerApplicable().apply(player);
    }

}