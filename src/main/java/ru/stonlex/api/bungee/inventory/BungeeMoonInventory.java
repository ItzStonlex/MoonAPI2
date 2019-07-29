package ru.stonlex.api.bungee.inventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.stonlex.api.bungee.BungeeMoonAPI;
import ru.stonlex.api.bungee.inventory.interfaces.BungeeInventoryButton;
import ru.stonlex.api.bungee.inventory.item.BungeeItemMeta;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;
import ru.stonlex.api.bungee.utility.SpigotMessageUtil;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BungeeMoonInventory {

    private final TIntObjectMap<BungeeInventoryButton> buttonMap = new TIntObjectHashMap<>();


    protected final String inventoryName, inventoryTitle;

    protected final int inventorySize, inventoryRows;


    public static final Map<ProxiedPlayer, BungeeMoonInventory> PLAYER_INVENTORY_MAP = new HashMap<>();

    /**
     * Инициализация инвентаря
     *
     * @param inventoryName  - Имя инвентаря
     * @param inventoryTitle - Заголовок инвентаря
     * @param inventoryRows  - Количество строк в инвентаре
     */
    public BungeeMoonInventory(String inventoryName, String inventoryTitle, int inventoryRows) {
        this.inventoryName = inventoryName;
        this.inventoryTitle = inventoryTitle;
        this.inventoryRows = inventoryRows;
        this.inventorySize = inventoryRows * 9;

        BungeeMoonAPI.getInventoryManager().cacheInventory(inventoryName, this);

        ProxyServer.getInstance().getServers().values().forEach(this::initializeInventory);
    }

    private void initializeInventory(ServerInfo serverInfo) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        dataOutput.writeUTF("InitInventory");
        dataOutput.writeUTF("bungee_" + inventoryName);
        dataOutput.writeUTF(inventoryTitle);
        dataOutput.writeInt(inventoryRows);

        SpigotMessageUtil.sendMessage(dataOutput, serverInfo);
    }

    /**
     * Вызывается при инициализации предметов
     * в инвентарь
     */
    public abstract void createInventory(ProxiedPlayer player);

    /**
     * Передающийся метод.
     *
     * Вызывается перед открытием инвентаря
     */
    public void onOpen(ProxiedPlayer player) { }

    /**
     * Передающийся метод.
     *
     * Вызывается после закрытия инвентаря
     */
    public void onClose(ProxiedPlayer player) { }


    /**
     * Добавить предмет в инвентарь
     */
    public void setItem(int slot, BungeeItemStack itemStack, Clickable<ProxiedPlayer> playerClickable) {
        BungeeInventoryButton inventoryButton = new InventoryButtonImpl(playerClickable, itemStack);

        buttonMap.put(slot, inventoryButton);
    }

    /**
     * Добавить предмет в инвентарь
     */
    public void setItem(int slot, BungeeItemStack itemStack) {
        BungeeInventoryButton inventoryButton = new InventoryButtonImpl(player -> {}, itemStack);

        buttonMap.put(slot, inventoryButton);
    }

    /**
     * Отправить сообщение о создании кнопок на сервер игрока
     */
    private void sendButtons(ProxiedPlayer proxiedPlayer) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        dataOutput.writeUTF("SendButtons");
        dataOutput.writeUTF(String.format("bungee_%s", inventoryName));
        dataOutput.writeInt(buttonMap.size());

        buttonMap.forEachEntry((slot, button) -> {
            dataOutput.writeInt(slot);

            BungeeItemStack itemStack = button.getItemStack();

            dataOutput.writeUTF(itemStack.getMaterial().name());

            dataOutput.writeInt(itemStack.getDurability());
            dataOutput.writeInt(itemStack.getAmount());

            BungeeItemMeta itemMeta = itemStack.getItemMeta();

            dataOutput.writeUTF(itemMeta.getName());

            dataOutput.writeInt(itemMeta.getLore().size());
            itemMeta.getLore().forEach(dataOutput::writeUTF);

            if (itemStack.getMaterial().name().contains("SKULL") && itemMeta.getSkullOwner() != null) {
                dataOutput.writeUTF(itemMeta.getSkullOwner());
            }

            return true;
        });

        SpigotMessageUtil.sendMessage(dataOutput, proxiedPlayer);
    }

    /**
     * Отправить сообщение об открытии инвентаря на сервер игрока
     */
    private void sendOpenInventory(ProxiedPlayer proxiedPlayer) {
        onOpen(proxiedPlayer);

        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        dataOutput.writeUTF("OpenInventory");
        dataOutput.writeUTF(proxiedPlayer.getName());
        dataOutput.writeUTF(String.format("bungee_%s", inventoryName));

        SpigotMessageUtil.sendMessage(dataOutput, proxiedPlayer);
    }

    /**
     * Отправить сообщение о закрытии инвентаря на сервер игрока
     */
    private void sendCloseInventory(ProxiedPlayer proxiedPlayer) {
        onOpen(proxiedPlayer);

        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        dataOutput.writeUTF("CloseInventory");
        dataOutput.writeUTF(proxiedPlayer.getName());

        SpigotMessageUtil.sendMessage(dataOutput, proxiedPlayer);
    }

    /**
     * Отправить сообщение об очистке инвентаря на определенный сервер
     */
    private void sendClearInventory(ServerInfo serverInfo) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        dataOutput.writeUTF("ClearInventory");
        dataOutput.writeUTF(String.format("bungee_%s", inventoryName));

        SpigotMessageUtil.sendMessage(dataOutput, serverInfo);
    }

    /**
     * Приватное открытие инвентаря игроку
     */
    private void openInventory(ProxiedPlayer player, boolean isOpen) {
        createInventory(player);

        sendButtons(player);

        if (isOpen) {
            sendOpenInventory(player);

            PLAYER_INVENTORY_MAP.put(player, this);
        }
    }

    /**
     * Очистка инвентаря на определенном сервере
     */
    public void clearInventory(ServerInfo serverInfo) {
        sendClearInventory(serverInfo);
    }

    /**
     * Открытие инвентаря игроку
     */
    public void openInventory(ProxiedPlayer player) {
        openInventory(player, true);
    }

    /**
     * Обновление инвентаря игроку
     */
    public void updateInventory(ProxiedPlayer player) {
        updateInventory(player, () -> {} );
    }

    protected void updateInventory(ProxiedPlayer player, Runnable command) {
        clearInventory(player.getServer().getInfo());

        command.run();

        openInventory(player, false);
    }

    /**
     * Закрыть инвентарь игроку
     */
    public void closeInventory(ProxiedPlayer player) {
        sendCloseInventory(player);
    }


    @RequiredArgsConstructor
    @Getter
    private class InventoryButtonImpl implements BungeeInventoryButton {

        private final Clickable<ProxiedPlayer> clickCommand;

        private final BungeeItemStack itemStack;
    }

}
