package ru.stonlex.api.bukkit.types;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class CacheManager<T> {

    private final Map<String, T> cacheMap = new HashMap<>();

    /**
     * Загрузка данных в кеш.
     */
    public void cacheData(String dataName, T cache) {
        cacheMap.put(dataName.toLowerCase(), cache);
    }

    /**
     * Получение данных из кеша.
     */
    public T getCache(String dataName) {
        return cacheMap.get(dataName.toLowerCase());
    }

    /**
     * Получение из кеша.
     *
     * Если в кеше нет, записываем в мапу новый объект
     *  и возвращаем его.
     */
    public T getComputeCache(String dataName, Function<? super String, ? extends T> mappingFunction) {
        return cacheMap.computeIfAbsent(dataName.toLowerCase(), mappingFunction);
    }

}
