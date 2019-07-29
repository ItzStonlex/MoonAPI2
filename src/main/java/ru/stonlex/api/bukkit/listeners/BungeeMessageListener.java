package ru.stonlex.api.bukkit.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.menus.MoonInventory;
import ru.stonlex.api.bukkit.utility.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class BungeeMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player1, byte[] bytes) {
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(bytes);

        if (!channel.equals("MoonAPI")) {
            return;
        }

        String subchannel = dataInput.readUTF();

        switch (subchannel) {
            /**
             * Инициализация инвентаря
             */
            case "InitInventory": {
                String inventoryName = dataInput.readUTF();
                String inventoryTitle = dataInput.readUTF();

                int inventoryRows = dataInput.readInt();

                MoonAPI.getInventoryManager().cacheInventory(inventoryName, new MoonInventory(inventoryTitle, inventoryRows) {
                    @Override
                    public void generateInventory(Player player) { }
                });

                break;
            }

            /**
             * Инициализация кнопок в инвентаре
             */
            case "SendButtons": {
                System.out.println("sendbuttons packet accept");
                String inventoryName = dataInput.readUTF();

                MoonInventory inventory = MoonAPI.getInventoryManager().getCachedInventory(inventoryName);

                if (inventory == null) {
                    break;
                }

                int buttonCount = dataInput.readInt();

                System.out.println("sendbuttons: buttons - " + buttonCount);
                System.out.println("sendbuttons: inventoryName - " + inventoryName);

                for (int i = 0 ; i < buttonCount ; i++) {
                    int slot = dataInput.readInt();

                    ItemStack itemStack = readItemStack(dataInput);

                    inventory.setItem(slot, itemStack);
                }

                break;
            }

            /**
             * Открытие инвентаря игроку
             */
            case "OpenInventory": {

                String playerName = dataInput.readUTF();
                String inventoryName = dataInput.readUTF();

                MoonInventory inventory = MoonAPI.getInventoryManager().getCachedInventory(inventoryName);

                if (inventory == null) {
                    break;
                }

                inventory.clear();

                Player player = Bukkit.getPlayerExact(playerName);

                if (player == null) {
                    break;
                }

                inventory.openInventory(player);

                break;
            }

            /**
             * Очистка инвентаря
             */
            case "ClearInventory": {
                String inventoryName = dataInput.readUTF();

                MoonInventory inventory = MoonAPI.getInventoryManager().getCachedInventory(inventoryName);

                if (inventory == null) {
                    break;
                }

                inventory.clear();

                break;
            }
        }
    }

    /**
     * Чтение предмета из InputStream
     */
    private ItemStack readItemStack(ByteArrayDataInput dataInput) {
        Material material = Material.matchMaterial(dataInput.readUTF());

        int durability = dataInput.readInt();
        int amount = dataInput.readInt();

        String displayName = dataInput.readUTF();

        int loreCount = dataInput.readInt();
        List<String> lore = new ArrayList<>();

        for (int loreIndex = 0; loreIndex < loreCount ; loreIndex++) {
            lore.add(dataInput.readUTF());
        }

        if (material.name().contains("SKULL") && durability == 3) {
            String skullOwner = dataInput.readUTF();

            if (skullOwner.length() > 16) {
                return ItemUtil.getTextureSkull(skullOwner, amount, displayName, lore.toArray(new String[0]));
            } else {
                return ItemUtil.getPlayerSkull(skullOwner, amount, displayName, lore.toArray(new String[0]));
            }
        }

        return ItemUtil.getItemStack(material, (byte) durability, amount, displayName, lore.toArray(new String[0]));
    }

}
