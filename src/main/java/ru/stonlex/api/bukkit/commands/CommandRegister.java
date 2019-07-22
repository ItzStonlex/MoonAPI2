package ru.stonlex.api.bukkit.commands;

import lombok.Getter;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;

public class CommandRegister extends Command implements PluginIdentifiableCommand {

    @Getter
    private final Plugin plugin;

    private final CommandExecutor owner;

    private static CommandMap commandMap;

    public CommandRegister(List<String> aliases, String desc, String usage, CommandExecutor owner, Plugin plugin) {
        super(aliases.get(0), desc, usage, aliases);
        this.owner = owner;
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        return owner.onCommand(sender, this, label, args);
    }

    /**
     * Регистрация комманд при помощи org.bukkit.command.CommandMap
     *
     *  (Код старый, переписывать его было лень, так как он и так
     *   стабильно и правильно работает. Сделал его только чуток красивее)
     */
    static void reg(Plugin plugin, CommandExecutor executor, List<String> aliases) {
        try {
            CommandRegister commandRegister = new CommandRegister(aliases, "Command by MoonAPI", "vk.com/moonstudio_mc", executor, plugin);

            if (commandMap == null) {
                String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];

                Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                Object craftServerObject = craftServerClass.cast((Object)plugin.getServer());
                Field commandMapField = craftServerClass.getDeclaredField("commandMap");

                commandMapField.setAccessible(true);

                commandMap = (SimpleCommandMap)commandMapField.get(craftServerObject);
            }

            commandMap.register(plugin.getDescription().getName(), commandRegister);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

