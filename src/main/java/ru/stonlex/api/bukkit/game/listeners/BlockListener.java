package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.MoonAPI;

public class BlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        GameAPI gameAPI = MoonAPI.getGameAPI();

        event.setCancelled(!gameAPI.getGameSettings().BLOCK_BREAK);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        GameAPI gameAPI = MoonAPI.getGameAPI();

        event.setCancelled(!gameAPI.getGameSettings().BLOCK_BREAK);
    }

}
