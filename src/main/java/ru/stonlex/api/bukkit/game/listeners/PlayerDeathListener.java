package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.game.factory.AbstractGameFactory;
import ru.stonlex.api.bukkit.game.player.GamePlayer;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 14.08.2019 8:44)
 */
public class PlayerDeathListener implements Listener {

    /**
     * Данный листенер не предусматривает смерть наблюдателя,
     * это должен делать сам разработчик, так как каждый это
     * может реализовать по своему.
     */

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        GamePlayer gamePlayer = MoonAPI.getGameAPI().getGamePlayer(event.getEntity());

        if (gamePlayer.isSpectator()) {
            return;
        }

        AbstractGameFactory gameFactory = MoonAPI.getGameAPI().getGameFactory();

        gameFactory.onDeath(gamePlayer.getPlayer());
    }

}
