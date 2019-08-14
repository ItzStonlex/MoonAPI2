package ru.stonlex.api.bukkit.game.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 05.08.2019 20:18)
 */
@RequiredArgsConstructor
@Getter
public enum GamePlayerType {

    PLAYER(ChatColor.BLUE, "Игрок"),
    SPECTATOR(ChatColor.GRAY, "Наблюдатель");


    private final ChatColor chatColor;

    private final String name;


    public String getDisplayName() {
        return chatColor + name;
    }
}
