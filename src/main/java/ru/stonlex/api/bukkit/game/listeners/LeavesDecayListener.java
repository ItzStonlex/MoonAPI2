package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.MoonAPI;

public class LeavesDecayListener implements Listener {

    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        GameAPI gameAPI = MoonAPI.getGameAPI();

        event.setCancelled(!gameAPI.getGameSettings().LEAVES_DECAY);
    }

}
