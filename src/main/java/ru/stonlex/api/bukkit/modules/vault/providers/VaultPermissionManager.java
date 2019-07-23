package ru.stonlex.api.bukkit.modules.vault.providers;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultPermissionManager {

    @Getter
    private final Permission vaultPermission;

    /**
     * Инициализация vaultPermission
     *
     * Если инициализация прошла неудачно, то в консоль выводит
     * сообщение о том, что vaultPermission == null
     */
    public VaultPermissionManager() {
        RegisteredServiceProvider<Permission> serviceProvider = Bukkit.getServicesManager().getRegistration(Permission.class);

        if (serviceProvider.getProvider() == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Не удалось инициализировать VaultPermission.");
        }
        this.vaultPermission = serviceProvider.getProvider();

        if (vaultPermission == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Не удалось инициализировать VaultPermission.");
        }

    }

}
