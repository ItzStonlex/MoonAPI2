package ru.stonlex.api.bukkit.game.perk;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.java.interfaces.Applicable;
import ru.stonlex.api.java.interfaces.Builder;

@AllArgsConstructor
public final class PerkBuilder implements Builder<MoonPerk> {

    private String perkName;

    private PerkInfo perkInfo;


    /**
     * Установка имени перку
     */
    public PerkBuilder setPerkName(String perkName) {
        this.perkName = perkName;

        return this;
    }

    /**
     * Установка базового предмета перку
     */
    public PerkBuilder setBaseItem(ItemStack baseItem) {
        perkInfo.setBaseItem(baseItem);

        return this;
    }

    /**
     * Установка минимального уровня перка
     */
    public PerkBuilder setMinimumLevel(int minLevel) {
        perkInfo.setMinLevel(minLevel);

        return this;
    }

    /**
     * Установка максимального уровня перка
     */
    public PerkBuilder setMaximumLevel(int maxLevel) {
        perkInfo.setMaxLevel(maxLevel);

        return this;
    }

    /**
     * Установка права для перка
     */
    public PerkBuilder setPermission(String permission) {
        perkInfo.setPermission(permission);

        return this;
    }

    /**
     * Установка цены перка
     */
    public PerkBuilder setPrice(int price) {
        perkInfo.setPrice(price);

        return this;
    }

    /**
     * Установка действия применения перка игроку
     */
    public PerkBuilder setPlayerApplicable(Applicable<Player> playerApplicable) {
        perkInfo.setPlayerApplicable(playerApplicable);

        return this;
    }


    /**
     * Построение и создание перка
     */
    @Override
    public MoonPerk build() {
        MoonPerk moonPerk = new MoonPerk(perkName, perkInfo);

        MoonAPI.getGameAPI().getPerkManager()
                .cachePerk(perkName.toLowerCase(), moonPerk);

        return moonPerk;
    }

}
