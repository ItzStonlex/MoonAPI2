package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.GameSettings;

public class FoodChangeListener implements Listener {

    private final GameSettings GAME_SETTINGS = MoonAPI.getGameAPI().getGameSettings();

    @EventHandler
    public void onEntitySpawn(FoodLevelChangeEvent event) {
        event.setCancelled(!GAME_SETTINGS.PLAYER_FOOD_CHANGE);
    }

}
