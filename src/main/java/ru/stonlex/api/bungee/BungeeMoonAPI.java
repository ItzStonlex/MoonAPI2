package ru.stonlex.api.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import ru.stonlex.api.bungee.inventory.BungeeMoonInventory;
import ru.stonlex.api.bungee.inventory.event.ProxyInventoryClickEvent;
import ru.stonlex.api.bungee.inventory.event.ProxyInventoryCloseEvent;
import ru.stonlex.api.bungee.inventory.item.BungeeItemMeta;
import ru.stonlex.api.bungee.inventory.item.BungeeItemStack;
import ru.stonlex.api.bungee.inventory.item.BungeeMaterial;
import ru.stonlex.api.bungee.inventory.manager.BungeeInventoryManager;
import ru.stonlex.api.bungee.listeners.BungeeInventoryListener;
import ru.stonlex.api.bungee.listeners.BungeeTestListener;
import ru.stonlex.api.bungee.utility.SpigotMessageUtil;

import java.util.ArrayList;
import java.util.List;

public final class BungeeMoonAPI extends Plugin {

    @Getter
    private static final BungeeInventoryManager inventoryManager = new BungeeInventoryManager();


    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new BungeeTestListener());
        getProxy().getPluginManager().registerListener(this, new BungeeInventoryListener());


        /**
         * Регистрация принятия сообщений от
         * Spigot серверов
         */
        SpigotMessageUtil.registerAcceptMessages(this, event -> {
            ByteArrayDataInput dataInput = ByteStreams.newDataInput(event.getData());

            String subchannel = dataInput.readUTF();

            switch (subchannel) {

                /**
                 * Ивент, который вызывается, когда происходит
                 * закрытия инвентаря игроком
                 */
                case "InventoryCloseEvent": {
                    String inventoryName = dataInput.readUTF().replaceFirst("bungee_", "");
                    String playerName = dataInput.readUTF();

                    ProxiedPlayer proxiedPlayer = getProxy().getPlayer(playerName);
                    BungeeMoonInventory inventory = inventoryManager.getInventory(inventoryName);

                    if (inventory == null || proxiedPlayer == null) {
                        return;
                    }

                    ProxyInventoryCloseEvent inventoryCloseEvent = new ProxyInventoryCloseEvent(inventory, proxiedPlayer);

                    getProxy().getPluginManager().callEvent(inventoryCloseEvent);

                    break;
                }

                /**
                 * Ивент, который вызывается, когда происходит
                 * клик по предмету в инвентаре
                 */
                case "InventoryClickEvent": {
                    String inventoryName = dataInput.readUTF();
                    String playerName = dataInput.readUTF();

                    int slot = dataInput.readInt();

                    BungeeItemStack itemStack = readItemStack(dataInput);
                    ProxiedPlayer proxiedPlayer = getProxy().getPlayer(playerName);
                    BungeeMoonInventory inventory = inventoryManager.getInventory(inventoryName);

                    if (inventory == null || proxiedPlayer == null) {
                        return;
                    }

                    ProxyInventoryClickEvent inventoryClickEvent = new ProxyInventoryClickEvent(inventory, proxiedPlayer, slot, itemStack);

                    getProxy().getPluginManager().callEvent(inventoryClickEvent);

                    break;
                }
            }

        });
    }

    /**
     * Чтение предмета из InputStream
     */
    private BungeeItemStack readItemStack(ByteArrayDataInput dataInput) {
        BungeeMaterial bungeeMaterial = BungeeMaterial.matchBungeeMaterial(dataInput.readUTF());

        int durability = dataInput.readInt();
        int amount = dataInput.readInt();

        String displayName = dataInput.readUTF();

        int loreCount = dataInput.readInt();

        List<String> lore = new ArrayList<>();

        for (int loreIndex = 0 ; loreIndex < loreCount ; loreIndex++) {
            lore.add(dataInput.readUTF());
        }

        BungeeItemStack itemStack = new BungeeItemStack(bungeeMaterial, durability, amount);
        BungeeItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setName(displayName);
        itemMeta.setLore(lore);

        if (bungeeMaterial.name().contains("SKULL") && durability == 3) {
            itemMeta.setSkullOwner(dataInput.readUTF());
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
