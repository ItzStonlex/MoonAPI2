package ru.stonlex.api.bukkit.messaging;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.MoonAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public final class MessagingManager {
    
    private final MoonAPI moonAPI;

    public void redirectPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public void sendMessage(String playerName, String message) {
        Player playerExact = Bukkit.getPlayerExact(playerName);

        if (playerExact != null && playerExact.isOnline()) {
            playerExact.sendMessage(message);
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF(playerName);
        out.writeUTF(message);

        moonAPI.getServer().sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public void kickProxyPlayer(String playerName, String reason) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("KickPlayer");
        out.writeUTF(playerName);
        out.writeUTF(ChatColor.translateAlternateColorCodes('&', reason));

        moonAPI.getServer().sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public ServerPing getServer(String address, int port) {
        return new ServerPing(address, port);
    }

}
