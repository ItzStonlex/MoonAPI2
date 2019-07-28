package ru.stonlex.api.bungee.inventory.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BungeeItemMeta {

    private String skullOwner;
    private String name;
    private List<String> lore;

    /**
     * Является ли предмет головой с текстурой
     */
    public boolean isSkullTexture() {
        return skullOwner.length() > 16;
    }

}
