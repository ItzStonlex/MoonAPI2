package ru.stonlex.api.bukkit.game.cage.impl;

import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.game.cage.GameCage;
import ru.stonlex.api.bukkit.modules.protocol.packet.world.WrapperPlayServerWorldBorder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 21:12)
 */
public class BorderCage implements GameCage {

    @Getter
    private final Map<String, WrapperPlayServerWorldBorder> worldBorderPacketMap = new HashMap<>();


    @Override
    public void create(Player player, Location location, int size) {
        WrapperPlayServerWorldBorder worldBorderPacket = new WrapperPlayServerWorldBorder();

        worldBorderPacket.setCenterX(location.getX());
        worldBorderPacket.setCenterZ(location.getZ());
        worldBorderPacket.setRadius(size);
        worldBorderPacket.setAction(EnumWrappers.WorldBorderAction.INITIALIZE);

        worldBorderPacket.sendPacket(player);

        worldBorderPacketMap.put(player.getName().toLowerCase(), worldBorderPacket);
    }

    @Override
    public void remove(Player player) {
        WorldBorder worldBorder = player.getWorld().getWorldBorder();
        WrapperPlayServerWorldBorder worldBorderPacket = worldBorderPacketMap.get(player.getName().toLowerCase());

        worldBorderPacket.setRadius(worldBorderPacket.getOldRadius());
        worldBorderPacket.setCenterX(worldBorder.getCenter().getX());
        worldBorderPacket.setCenterZ(worldBorder.getCenter().getZ());
        worldBorderPacket.setAction(EnumWrappers.WorldBorderAction.INITIALIZE);

        worldBorderPacket.sendPacket(player);

        worldBorderPacketMap.put(player.getName().toLowerCase(), null);
    }

    @Override
    public boolean isActive(Player player) {
        return worldBorderPacketMap.containsKey(player.getName().toLowerCase())
                && worldBorderPacketMap.get(player.getName().toLowerCase()) != null;
    }
}
