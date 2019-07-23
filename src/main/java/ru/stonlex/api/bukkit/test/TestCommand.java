package ru.stonlex.api.bukkit.test;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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