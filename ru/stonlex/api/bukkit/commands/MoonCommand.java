package ru.stonlex.api.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface MoonCommand {

    void execute(CommandSender sender, String[] args) throws Exception;

    default boolean senderIsPlayer(CommandSender commandSender) {
        return commandSender instanceof Player;
    }

    default boolean hasArguments(String... args) {
        return args.length == 0 || (args.length < 2 && args[0].isEmpty());
    }

}
