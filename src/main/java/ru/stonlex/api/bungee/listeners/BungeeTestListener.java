package ru.stonlex.api.bungee.listeners;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.stonlex.api.java.schedulers.MoonTask;
import ru.stonlex.api.test.BungeeTestMenu;

import java.util.concurrent.TimeUnit;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 29.07.2019 23:54)
 */
public class BungeeTestListener implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        new MoonTask() {
            @Override
            public void run() {
                new BungeeTestMenu().openInventory(event.getPlayer());
            }
        }.scheduleLater(5, TimeUnit.SECONDS);
    }

}
