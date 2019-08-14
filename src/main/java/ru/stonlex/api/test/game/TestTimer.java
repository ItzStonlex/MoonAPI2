package ru.stonlex.api.test.game;

import org.bukkit.Bukkit;
import ru.stonlex.api.bukkit.game.factory.AbstractTimerFactory;
import ru.stonlex.api.java.utility.IntegerUtil;

/**
 * @Author ItzStonlex.
 * @VK https://vk.com/itzstonlex
 * <p>
 * (Created on 03.08.2019 10:28)
 */
public class TestTimer extends AbstractTimerFactory {

    @Override
    public void onTimerWork(int secondsLeft) {
        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(secondsLeft));

        //Если время делится на 10 или <= 5, то мы оповещаем всех об этом
        if (!(secondsLeft % 10 == 0 || secondsLeft <= 5)) {
            return;
        }

        //Процесс оповещения
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§eИгра начнется через " + IntegerUtil.formatting(secondsLeft, "секунда", "секунды", "секунд"));

            player.sendTitle("§6§lSkyWars Solo", "Старт игры через §e" + secondsLeft);
        });
    }

}
