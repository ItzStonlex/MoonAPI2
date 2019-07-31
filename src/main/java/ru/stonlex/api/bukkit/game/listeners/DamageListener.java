package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.GameSettings;

public class DamageListener implements Listener {

    private final GameSettings GAME_SETTINGS = MoonAPI.getGameAPI().getGameSettings();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof Player) {
            event.setCancelled(!GAME_SETTINGS.PLAYER_DAMAGE);
            return;
        }

        event.setCancelled(!GAME_SETTINGS.ENTITY_DAMAGE);
    }

}
