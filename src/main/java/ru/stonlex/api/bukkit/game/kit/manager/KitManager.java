package ru.stonlex.api.bukkit.game.kit.manager;

import ru.stonlex.api.bukkit.game.kit.KitBuilder;
import ru.stonlex.api.bukkit.game.kit.KitInfo;
import ru.stonlex.api.bukkit.game.kit.MoonKit;
import ru.stonlex.api.java.types.AbstractCacheManager;

public final class KitManager extends AbstractCacheManager<MoonKit> {

    /**
     * Кеширование набора в мапу по его имени.
     */
    public void cacheKit(String kitName, MoonKit moonKit) {
        cacheData(kitName.toLowerCase(), moonKit);
    }

    /**
     * Получение набора из кеша по его имени.
     */
    public final MoonKit getKit(String kitName) {
        return getCache(kitName.toLowerCase());
    }


    /**
     * Создание нового Builder
     */
    public KitBuilder newBuilder() {
        return newBuilder("moonapi");
    }

    /**
     * Создание нового Builder с именем набора
     */
    public KitBuilder newBuilder(String kitName) {
        return new KitBuilder(kitName, new KitInfo(null, null, null, null, 0, null));
    }

}
