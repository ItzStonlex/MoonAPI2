package ru.stonlex.api.test;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.commands.MoonCommand;
import ru.stonlex.api.test.menu.TestPagedMenu;

public class TestCommand implements MoonCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!senderIsPlayer(sender)) {
            return;
        }

        Player player = (Player) sender;
        player.sendMessage(ChatColor.GREEN + "Success!");


        /* TEST */

        new TestPagedMenu().openInventory(player);

        /* TEST */
    }

}
