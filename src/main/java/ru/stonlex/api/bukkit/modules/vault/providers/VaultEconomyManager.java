package ru.stonlex.api.bukkit.modules.vault.providers;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultEconomyManager {

    @Getter
    private final Economy vaultEconomy;

    /**
     * Инициализация vaultEconomy
     *
     * Если инициализация прошла неудачно, то в консоль выводит
     * сообщение о том, что vaultEconomy == null
     */
    public VaultEconomyManager() {
        RegisteredServiceProvider<Economy> serviceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        this.vaultEconomy = serviceProvider.getProvider();

        if (vaultEconomy == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Не удалось инициализировать VaultEconomy.");
        }

    }

}
