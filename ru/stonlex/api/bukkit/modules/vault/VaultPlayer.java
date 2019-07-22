package ru.stonlex.api.bukkit.modules.vault;

public interface VaultPlayer {

    String getName();

    /**
     * Раздел VaultChat
     */
    String getPrefix();

    String getSuffix();

    String getGroupPrefix();

    String getGroupSuffix();

    /**
     * Раздел VaultEconomy
     */
    double getBalance();

    void setBalance(double balance);

    void giveMoney(double moneyCount);

    void takeMoney(double moneyCount);

    /**
     * Раздел VaultPermission
     * @return
     */

    String[] getGroups();

    String getPrimaryGroup();

    void addPermission(String permission);

    void removePermission(String permission);

    void addGroup(String group);

    void removeGroup(String group);

    boolean hasGroup(String group);

    boolean hasPermission(String permission);
}
