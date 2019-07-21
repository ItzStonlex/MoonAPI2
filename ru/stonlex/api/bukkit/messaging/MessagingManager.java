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

    public void redirect(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public List<String> getOnlinePlayers(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerList");
        out.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());

        String[] playerList = ByteStreams.newDataInput(out.toByteArray()).readUTF().split(", ");
        return new ArrayList<>(Arrays.asList(playerList));
    }

    public void sendMessage(String player, String message) {
        final Player playerExact = Bukkit.getPlayerExact(player);
        if (playerExact.isOnline()) {
            playerExact.sendMessage(message);
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(ChatColor.translateAlternateColorCodes('&', message));

        playerExact.sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public int getOnline(String server) {
        return getOnlinePlayers(server).size();
    }

    public void kickPlayer(String player, String reason) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("KickPlayer");
        out.writeUTF(player);
        out.writeUTF(ChatColor.translateAlternateColorCodes('&', reason));

        Bukkit.getPlayerExact(player).sendPluginMessage(moonAPI, "BungeeCord", out.toByteArray());
    }

    public ServerPing getServer(String address, int port) {
        return new ServerPing(address, port);
    }

}
