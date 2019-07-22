package ru.stonlex.api.bukkit.utility;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class WrapperUtil {

    /**
     * Опять же, этот код старый, и переписывать его мне было
     * попросту лень, да и тем более, он прекрасно работает.
     *
     * Если кому-то он неудобен, то система как бы не особо сложная, 
     * поэтому можно и самому ее написать
     */
    
    public final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public Class<?> getNmsClass(String className) {
        try {

            return Class.forName("net.minecraft.server." + VERSION + "." + className);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getCraftbukkitClass(String packageName, String className) {
        try {

            return Class.forName("org.bukkit.craftbukkit." + VERSION + "." + (packageName != null && !packageName.isEmpty() ? packageName + "." : "") + className);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }

}
