package ru.stonlex.api.bukkit.modules.vault.providers;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultChatManager {

    @Getter
    private final Chat vaultChat;

    /**
     * Инициализация vaultChat
     *
     * Если инициализация прошла неудачно, то в консоль выводит
     * сообщение о том, что vaultChat == null
     */
    public VaultChatManager() {
        RegisteredServiceProvider<Chat> serviceProvider = Bukkit.getServicesManager().getRegistration(Chat.class);

        if (serviceProvider.getProvider() == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Не удалось инициализировать VaultChat.");
        }

        this.vaultChat = serviceProvider.getProvider();

        if (vaultChat == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Не удалось инициализировать VaultChat.");
        }

    }

}
