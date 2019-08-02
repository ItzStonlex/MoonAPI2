package ru.stonlex.api.test;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.commands.MoonCommand;
import ru.stonlex.api.bukkit.game.cage.GameCage;
import ru.stonlex.api.bukkit.game.cage.impl.BlockCage;
import ru.stonlex.api.bukkit.game.cage.impl.BorderCage;
import ru.stonlex.api.bukkit.game.cage.manager.CageManager;
import ru.stonlex.api.java.schedulers.MoonTask;

import java.util.concurrent.TimeUnit;

public class TestCommand implements MoonCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!senderIsPlayer(sender)) {
            return;
        }

        Player player = (Player) sender;

        if (!hasArguments(args)) {
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Success!");

        //Test
        new TestPagedMenu().openInventory(player);
    }

}
