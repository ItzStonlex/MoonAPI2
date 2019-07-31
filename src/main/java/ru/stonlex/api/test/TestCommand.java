package ru.stonlex.api.test;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.stonlex.api.bukkit.MoonAPI;
import ru.stonlex.api.bukkit.commands.MoonCommand;

public class TestCommand implements MoonCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!senderIsPlayer(sender)) {
            return;
        }

        if (!hasArguments(args)) {
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Success!");
    }

}
