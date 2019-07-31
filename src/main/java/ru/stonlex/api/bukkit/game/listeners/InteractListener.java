package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.GameSettings;

public class InteractListener implements Listener {

    private final GameSettings GAME_SETTINGS = MoonAPI.getGameAPI().getGameSettings();

    @EventHandler
    public void onEntitySpawn(PlayerInteractEvent event) {
        event.setCancelled(!GAME_SETTINGS.PLAYER_INTERACT);
    }

    @EventHandler
    public void onEntitySpawn(PlayerInteractAtEntityEvent event) {
        event.setCancelled(!GAME_SETTINGS.PLAYER_INTERACT_AT_ENTITY);
    }

}
