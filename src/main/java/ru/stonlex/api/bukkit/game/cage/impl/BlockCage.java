package ru.stonlex.api.bukkit.game.cage.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.game.cage.GameCage;
import ru.stonlex.api.bukkit.types.CuboidRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 01.08.2019 21:12)
 */
public class BlockCage implements GameCage {

    private final Map<String, List<Block>> cageBlockMap = new HashMap<>();

    @Setter
    @Getter
    private Material cageType = Material.STAINED_GLASS;


    @Override
    public void create(Player player, Location location, int size) {
        final List<Block> cageLocations = new ArrayList<>();

        final CuboidRegion cuboidRegion = new CuboidRegion(
                location.clone().add(size, 3, size),
                location.clone().subtract(size, 1, size));

        final CuboidRegion inCageRegion = new CuboidRegion(
                cuboidRegion.getUpperSW().clone().subtract(1, 1, 1),
                cuboidRegion.getLowerNE().clone().add(1, 1, 1));

        cuboidRegion.getBlocks().forEach(block -> {
            if (cuboidRegion.getEndLines().contains(block)) {
                return;
            }

            if (inCageRegion.getBlocks().contains(block)) {
                return;
            }
            block.setType(cageType);

            cageLocations.add(block);
        });

        cageBlockMap.put(player.getName().toLowerCase(), cageLocations);
    }

    @Override
    public void remove(Player player) {
        final List<Block> cageLocations = cageBlockMap.get(player.getName().toLowerCase());

        cageLocations.forEach(block -> block.setType(Material.AIR));
    }

    @Override
    public boolean isActive(Player player) {
        return cageBlockMap.containsKey(player.getName().toLowerCase())
                && !cageBlockMap.get(player.getName().toLowerCase()).isEmpty();
    }
}
