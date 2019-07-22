package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.management.Management;

public class JoinListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        GameAPI gameAPI = Management.getGameAPI();

        event.setResult(gameAPI.getGameSettings().PLAYER_JOIN
                ? PlayerPreLoginEvent.Result.ALLOWED
                : PlayerPreLoginEvent.Result.KICK_OTHER);

        event.setKickMessage("§cНа данный момент невозможно подключиться к арене!");
    }

}
