package ru.stonlex.api.bukkit.game.perk.manager;

import ru.stonlex.api.bukkit.game.perk.MoonPerk;
import ru.stonlex.api.bukkit.game.perk.PerkBuilder;
import ru.stonlex.api.bukkit.game.perk.PerkInfo;
import ru.stonlex.api.java.types.AbstractCacheManager;

public final class PerkManager extends AbstractCacheManager<MoonPerk> {

    /**
     * Кеширование перка в мапу по его имени.
     */
    public void cachePerk(String perkName, MoonPerk moonPerk) {
        cacheData(perkName.toLowerCase(), moonPerk);
    }

    /**
     * Получение перка из кеша по его имени.
     */
    public final MoonPerk getPerk(String perkName) {
        return getCache(perkName.toLowerCase());
    }


    /**
     * Создание нового Builder
     */
    public PerkBuilder newBuilder() {
        return newBuilder("moonapi");
    }

    /**
     * Создание нового Builder с именем перка
     */
    public PerkBuilder newBuilder(String perkName) {
        return new PerkBuilder(perkName, new PerkInfo(null, null, 0, 1, 0, null));
    }

}
