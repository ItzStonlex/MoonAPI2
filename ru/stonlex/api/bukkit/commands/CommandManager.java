package ru.stonlex.api.bukkit.commands;

import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager {

    private final Map<String, MoonCommand> commandMap = new HashMap<>();

    /**
     * Регистрация команды, добавление в org.bukkit.command.CommandMap
     */
    public void registerCommand(Plugin plugin, MoonCommand command, String... aliases) {
        CommandRegister.reg(plugin, (sender, command1, s, args) -> {
            try {
                command.execute(sender, args);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }, Arrays.asList(aliases));

        commandMap.put(aliases[0].toLowerCase(), command);
    }

    /**
     * Получение самой команды по ее имени
     */
    public MoonCommand getCommandByName(String commandName) {
        return commandMap.get(commandName.toLowerCase());
    }

}
