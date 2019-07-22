package ru.stonlex.api.bukkit.game.players;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface GamePlayer {

    String getName();

    Player getPlayer();

    ItemStack getHead();

    double getMultiple();

    boolean isPlayer();
    boolean isSpectator();

    void setSpectator();
    void leave();

}
