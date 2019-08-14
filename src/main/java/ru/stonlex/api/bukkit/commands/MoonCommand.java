package ru.stonlex.api.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface MoonCommand {

    /**
     * Выполнение команды и ее процесс
     *
     * @param sender - Отправитель команды.
     * @param args - Аргументы отправителя команды.
     *
     * @throws Exception - Нужен в случае того, чтобы не делать в коде
     *                      лишних try/catch, тем самым сделав его красивее.
     */
    void execute(CommandSender sender, String[] args) throws Exception;


    /**
     * Возвращает, является ли отправитель игроком
     *
     * @param commandSender - Отправитель команды.
     */
    default boolean senderIsPlayer(CommandSender commandSender) {
        return commandSender instanceof Player;
    }

}
