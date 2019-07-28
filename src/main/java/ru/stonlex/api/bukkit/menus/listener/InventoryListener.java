package ru.stonlex.api.bukkit.menus.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.menus.MoonInventory;
import ru.stonlex.api.bukkit.menus.interfaces.InventoryButton;
import ru.stonlex.api.bungee.inventory.item.BungeeItemMeta;

@RequiredArgsConstructor
public class InventoryListener implements Listener {

    private final MoonAPI moonAPI;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MoonInventory inventory = MoonInventory.getInventories().get(player.getName().toLowerCase());

        int slot = e.getSlot();

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || inventory == null || !inventory.getButtons().containsKey(slot + 1)) {
            return;
        }

        e.setCancelled(true);

        String inventoryName = MoonAPI.getInventoryManager().getInventoryName(inventory);

        if (inventoryName != null && inventoryName.startsWith("bungee_")) {
            ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

            dataOutput.writeUTF("InventoryClickEvent");
            dataOutput.writeUTF(inventoryName);
            dataOutput.writeUTF(player.getName());
            dataOutput.writeInt(e.getSlot() + 1);

            dataOutput.writeUTF(e.getCurrentItem().getType().name());

            dataOutput.writeInt(e.getCurrentItem().getDurability());
            dataOutput.writeInt(e.getCurrentItem().getAmount());

            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();

            dataOutput.writeUTF(itemMeta.getDisplayName());

            dataOutput.writeInt(itemMeta.getLore().size());
            itemMeta.getLore().forEach(dataOutput::writeUTF);

            if (e.getCurrentItem().getType().name().contains("SKULL") && e.getCurrentItem().getDurability() == 3) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                dataOutput.writeUTF(skullMeta.getOwner());
            }

            moonAPI.getServer().sendPluginMessage(moonAPI, MoonAPI.PLUGIN_MESSAGE_CHANNEL, dataOutput.toByteArray());
            return;
        }

        InventoryButton button = inventory.getButtons().get(slot + 1);

        button.getCommand().onClick(player);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        MoonInventory inventory = MoonInventory.getInventories().get(player.getName().toLowerCase());

        if (inventory == null) {
            return;
        }

        MoonInventory.getInventories().remove(player.getName().toLowerCase());

        String inventoryName = MoonAPI.getInventoryManager().getInventoryName(inventory);

        if (inventoryName != null && inventoryName.startsWith("bungee_")) {
            ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

            dataOutput.writeUTF("InventoryCloseEvent");
            dataOutput.writeUTF(inventoryName);
            dataOutput.writeUTF(player.getName());

            moonAPI.getServer().sendPluginMessage(moonAPI, MoonAPI.PLUGIN_MESSAGE_CHANNEL, dataOutput.toByteArray());
            return;
        }

        inventory.onClose(player);
    }

}
