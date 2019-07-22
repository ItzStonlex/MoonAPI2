package ru.stonlex.api.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.modules.vault.VaultPlayer;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VaultPlayer vaultPlayer = MoonAPI.getVaultManager().getVaultPlayer(player);

        player.setDisplayName(vaultPlayer.getPrefix().concat(player.getName()));
    }

}
