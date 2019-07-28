package ru.stonlex.api.bungee.inventory.item;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BungeeItemStack {

    private int durability, amount;

    private BungeeMaterial material;

    private BungeeItemMeta itemMeta;

    /**
     * Инициализация предмета с его материалом
     *
     * @param material - Материал предмета
     */
    public BungeeItemStack(BungeeMaterial material) {
        this.material = material;

        this.durability = 0;
        this.amount = 1;

        generateItemMeta();
    }

    /**
     * Инициализация предмета с его материлом и значением
     *
     * @param material - Материал предмета
     * @param durability - Значение предмета
     */
    public BungeeItemStack(BungeeMaterial material, int durability) {
        this.material = material;

        this.durability = durability;
        this.amount = 1;

        generateItemMeta();
    }

    /**
     * Инициализация предмета с его материалом, значением и количеством
     *
     * @param material - Материал предмета
     * @param durability - Значение предмета
     * @param amount - Количество предмета
     */
    public BungeeItemStack(BungeeMaterial material, int durability, int amount) {
        this.material = material;

        this.durability = durability;
        this.amount = amount;

        generateItemMeta();
    }

    /**
     * Генерация BungeeItemMeta и ее инициализация
     */
    private void generateItemMeta() {
        this.itemMeta = new BungeeItemMeta(null, material.name().toLowerCase()
                .replace("_", ""), new ArrayList<>());
    }

}
