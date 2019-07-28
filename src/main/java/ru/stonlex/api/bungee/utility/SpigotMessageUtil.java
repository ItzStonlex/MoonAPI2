package ru.stonlex.api.bungee.utility;

import com.google.common.io.ByteArrayDataOutput;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import ru.stonlex.api.java.interfaces.Applicable;

@UtilityClass
public class SpigotMessageUtil {

    public final String CHANNEL_NAME = "MoonAPI";


    /**
     * Отправка сообщения на Spigot сервер
     *
     * @param bytes - Отсылаемые данные в байтах
     * @param serverInfo - Сервер, на который отсылаются данные
     */
    public void sendMessage(byte[] bytes, ServerInfo serverInfo) {
        if (serverInfo == null) {
            throw new RuntimeException("server info is null");
        }

        serverInfo.sendData(CHANNEL_NAME, bytes);
    }

    /**
     * Отправка сообщения на Spigot сервер
     *
     * @param dataOutput - Отсылаемые данные
     * @param serverInfo - Сервер, на который отсылаются данные
     */
    public void sendMessage(ByteArrayDataOutput dataOutput, ServerInfo serverInfo) {
        sendMessage(dataOutput.toByteArray(), serverInfo);
    }

    /**
     * Отправка сообщения на Spigot сервер
     *
     * @param bytes - Отсылаемые данные в байтах
     * @param proxiedPlayer - Игрок, на чей сервер отправлять данные
     */
    public void sendMessage(byte[] bytes, ProxiedPlayer proxiedPlayer) {
        sendMessage(bytes, proxiedPlayer.getServer().getInfo());
    }

    /**
     * Отправка сообщения на Spigot сервер
     *
     * @param dataOutput - Отсылаемые данные
     * @param proxiedPlayer - Игрок, на чей сервер отправлять данные
     */
    public void sendMessage(ByteArrayDataOutput dataOutput, ProxiedPlayer proxiedPlayer) {
        sendMessage(dataOutput, proxiedPlayer.getServer().getInfo());
    }

    /**
     * Регистрация принятия данных
     */
    public void registerAcceptMessages(Plugin plugin, Applicable<PluginMessageEvent> eventApplicable) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Listener() {

            @EventHandler
            public void onPluginMessage(PluginMessageEvent event) {

                if (!event.getTag().equals(CHANNEL_NAME)) {
                    return;
                }

                eventApplicable.apply(event);
            }
        });
    }

}
