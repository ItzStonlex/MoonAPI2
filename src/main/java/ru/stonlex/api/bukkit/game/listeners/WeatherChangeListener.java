package ru.stonlex.api.bukkit.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import ru.stonlex.api.bukkit.game.GameAPI;
import ru.stonlex.api.bukkit.MoonAPI;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        GameAPI gameAPI = MoonAPI.getGameAPI();

        event.setCancelled(!gameAPI.getGameSettings().WEATHER_CHANGE);
    }

}
