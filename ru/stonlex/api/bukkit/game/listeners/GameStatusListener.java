package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.management.Management;

public class GameStatusListener implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        GameAPI gameAPI = Management.getGameAPI();

        event.setMotd(gameAPI.getGameSettings().GAME_STATUS.getMotd());
    }

}
