package ru.stonlex.api.bukkit.game.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@AllArgsConstructor
@Getter
public class MoonKit {

    private final String kitName;

    @Setter
    private KitInfo kitInfo;


    /**
     * Выдать броню и предметы набора игроку
     */
    public void giveToPlayer(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        if (kitInfo.getHelmetItem() != null) playerInventory.setHelmet(kitInfo.getHelmetItem());
        if (kitInfo.getChestplateItem() != null) playerInventory.setChestplate(kitInfo.getChestplateItem());
        if (kitInfo.getLeggingsItem() != null) playerInventory.setLeggings(kitInfo.getLeggingsItem());
        if (kitInfo.getBootsItem() != null) playerInventory.setBoots(kitInfo.getBootsItem());

        playerInventory.addItem(kitInfo.getItemList().toArray(new ItemStack[0]));
    }

}