package ru.stonlex.api.bukkit.modules.vault;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.modules.vault.providers.VaultChatManager;
import ru.stonlex.api.bukkit.modules.vault.providers.VaultEconomyManager;
import ru.stonlex.api.bukkit.modules.vault.providers.VaultPermissionManager;
import ru.stonlex.api.bukkit.types.CacheManager;

@Getter
public final class VaultManager extends CacheManager<VaultPlayer> {

    private final VaultEconomyManager economyManager       = new VaultEconomyManager();
    private final VaultPermissionManager permissionManager = new VaultPermissionManager();
    private final VaultChatManager chatManager             = new VaultChatManager();

    /**
     * Получение кешированного VaultPlayer'а по нику игрока
     *
     * Если его нет в мапе, то он автоматически туда добавляется
     */
    public VaultPlayer getVaultPlayer(String playerName) {
        return getComputeCache(playerName.toLowerCase(), VaultPlayerImpl::new);
    }

    /**
     * Получение кешированного VaultPlayer'а по игроку
     *
     * Если его нет в мапе, то он автоматически туда добавляется
     */
    public VaultPlayer getVaultPlayer(Player player) {
        return getVaultPlayer(player.getName());
    }

    /**
     * Класс, наследующий VaultPlayer
     *
     * Через него проходят все получения и операции с Vault'ом
     */
    private class VaultPlayerImpl implements VaultPlayer {

        private final String playerName;

        private final OfflinePlayer offlinePlayer;

        public VaultPlayerImpl(String playerName) {
            this.playerName = playerName;
            this.offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        }

        @Override
        public String getName() {
            return playerName;
        }

        @Override
        public String getPrefix() {
            return chatManager.getVaultChat().getPlayerPrefix((String) null, playerName);
        }

        @Override
        public String getSuffix() {
            return chatManager.getVaultChat().getPlayerSuffix((String) null, playerName);
        }

        @Override
        public String getGroupPrefix() {
            return chatManager.getVaultChat().getGroupPrefix((String) null, getPrimaryGroup());
        }

        @Override
        public String getGroupSuffix() {
            return chatManager.getVaultChat().getGroupSuffix((String) null, getPrimaryGroup());
        }

        @Override
        public String getPrimaryGroup() {
            return chatManager.getVaultChat().getPrimaryGroup((String) null, playerName);
        }

        @Override
        public double getBalance() {
            return economyManager.getVaultEconomy().getBalance(offlinePlayer);
        }

        @Override
        public void setBalance(double balance) {
            if (balance > getBalance()) {
                giveMoney(balance - getBalance());
            } else if (balance < getBalance()) {
                takeMoney(getBalance() - balance);
            }
        }

        @Override
        public void giveMoney(double moneyCount) {
            economyManager.getVaultEconomy().depositPlayer(offlinePlayer, moneyCount);
        }

        @Override
        public void takeMoney(double moneyCount) {
            economyManager.getVaultEconomy().withdrawPlayer(offlinePlayer, moneyCount);
        }

        @Override
        public String[] getGroups() {
            return chatManager.getVaultChat().getPlayerGroups(null, offlinePlayer);
        }

        @Override
        public void addPermission(String permission) {
            permissionManager.getVaultPermission().playerAdd(null, offlinePlayer, permission);
        }

        @Override
        public void removePermission(String permission) {
            permissionManager.getVaultPermission().playerRemove(null, offlinePlayer, permission);
        }

        @Override
        public void addGroup(String group) {
            permissionManager.getVaultPermission().groupAdd((String) null, playerName, group);
        }

        @Override
        public void removeGroup(String group) {
            permissionManager.getVaultPermission().groupRemove((String) null, playerName, group);
        }

        @Override
        public boolean hasGroup(String group) {
            return permissionManager.getVaultPermission().groupHas((String) null, playerName, group);
        }

        @Override
        public boolean hasPermission(String permission) {
            return permissionManager.getVaultPermission().playerHas(null, offlinePlayer, permission);
        }
    }

}
