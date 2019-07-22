package ru.stonlex.api.bukkit.commands;

import org.bukkit.plugin.Plugin;
import ru.stonlex.api.bukkit.types.CacheManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager extends CacheManager<MoonCommand> {

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

        cacheData(aliases[0], command);
    }

    /**
     * Получение самой команды по ее имени
     */
    public MoonCommand getCommandByName(String commandName) {
        return getCache(commandName);
    }

}
