package ru.stonlex.api.bukkit.test;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.management.Management;
import ru.stonlex.api.bukkit.particle.ParticleEffect;
import ru.stonlex.api.bukkit.schedulers.MoonTask;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TestOther {

    private final MoonAPI moonAPI;

    public void spawnHologram(Player player, Location location, boolean showForAll) {
        Management.getHologramManager().createHologram("testHologram", location, hologram -> {
            hologram.addLine("123");
            hologram.addLine("§c321");

            if (showForAll) {
                hologram.spawn();
                return;
            }

            hologram.addReceiver(player);
        });
    }

    public void openInventory(Player player) {
        Management.getInventoryManager().createInventory("testInventory", "Test Inventory", 3, inventory -> {

            inventory.setItem(1, ItemUtil.getItemStack(Material.STONE), player1 -> player1.sendMessage("Ты кликнул по камню на 1 слоту"));

            inventory.openInventory(player);
        });
    }

    public void redirectPlayer(Player player, String serverName) {
        Management.getMessagingManager().redirect(player, serverName);
    }

    public void registerCommands() {
                                                                                    //Главная команда
        Management.getCommandManager().registerCommand(moonAPI, new TestCommand(), "test", "testing", "moontest");
                                                                                                     //Команды подобные
    }

    public void spawnCircle(Player player, Location location) {
        new MoonTask() {
            double t = 0;

            @Override
            public void run() {
                t += Math.PI / 16;

                double x = 2 * Math.cos(t);
                double z = 2 * Math.sin(t);

                ParticleEffect.SPELL.display(0, 0, 0, 0, 3,
                        location.clone().add(x, 0, z), player);
            }
        }.scheduleTimer(0, 1, TimeUnit.SECONDS);
    }

}
