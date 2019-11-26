package ru.stonlex.api.bukkit.tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PacketAccessor {
    private static List<String> legacyVersions = Arrays.asList("v1_7_R1", "v1_7_R2", "v1_7_R3", "v1_7_R4", "v1_8_R1", "v1_8_R2", "v1_8_R3", "v1_9_R1", "v1_9_R2", "v1_10_R1", "v1_11_R1", "v1_12_R1");
    private static boolean CAULDRON_SERVER = false;
    private static boolean LEGACY_SERVER = false;
    static Field MEMBERS;
    static Field PREFIX;
    static Field SUFFIX;
    static Field TEAM_NAME;
    static Field PARAM_INT;
    static Field PACK_OPTION;
    static Field DISPLAY_NAME;
    static Field TEAM_COLOR;
    static Field PUSH;
    static Field VISIBILITY;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> packetClass;

    public static boolean isLegacyVersion() {
        return LEGACY_SERVER;
    }

    private static boolean isPushVersion(String version) {
        return Integer.parseInt(version.split("_")[1]) >= 9;
    }

    private static boolean isVisibilityVersion(String version) {
        return Integer.parseInt(version.split("_")[1]) >= 8;
    }

    private static Field getNMS(String path) throws Exception {
        Field field = packetClass.getDeclaredField(path);
        field.setAccessible(true);
        return field;
    }

    static Object createPacket() {
        try {
            return packetClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void sendPacket(Collection<? extends Player> players, Object packet) {
        for (Player player : players) {
            PacketAccessor.sendPacket(player, packet);
        }
    }

    static void sendPacket(Player player, Object packet) {
        try {
            Object nmsPlayer = getHandle.invoke(player, new Object[0]);
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            Class.forName("cpw.mods.fml.common.Mod");
            CAULDRON_SERVER = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        try {
            Class<?> typeNMSPlayer;
            Object typePlayerConnection;
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (legacyVersions.contains(version)) {
                LEGACY_SERVER = true;
            }
            Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            getHandle = typeCraftPlayer.getMethod("getHandle", new Class[0]);
            if (CAULDRON_SERVER) {
                packetClass = Class.forName("net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam");
                typeNMSPlayer = Class.forName("net.minecraft.server.v1_7_R4.EntityPlayer");
                typePlayerConnection = Class.forName("net.minecraft.server.v1_7_R4.PlayerConnection");
                playerConnection = typeNMSPlayer.getField("field_71135_a");
                sendPacket = ((Class)typePlayerConnection).getMethod("func_147359_a", Class.forName("net.minecraft.server.v1_7_R4.Packet"));
            } else {
                packetClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutScoreboardTeam");
                typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
                typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
                playerConnection = typeNMSPlayer.getField("playerConnection");
                sendPacket = ((Class)typePlayerConnection).getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
            }
            PacketData currentVersion = null;
            for (PacketData packetData : PacketData.values()) {
                if (!version.contains(packetData.name())) continue;
                currentVersion = packetData;
            }
            if (CAULDRON_SERVER) {
                currentVersion = PacketData.cauldron;
            }
            if (currentVersion != null) {
                PREFIX = PacketAccessor.getNMS(currentVersion.getPrefix());
                SUFFIX = PacketAccessor.getNMS(currentVersion.getSuffix());
                MEMBERS = PacketAccessor.getNMS(currentVersion.getMembers());
                TEAM_NAME = PacketAccessor.getNMS(currentVersion.getTeamName());
                PARAM_INT = PacketAccessor.getNMS(currentVersion.getParamInt());
                PACK_OPTION = PacketAccessor.getNMS(currentVersion.getPackOption());
                DISPLAY_NAME = PacketAccessor.getNMS(currentVersion.getDisplayName());
                if (!PacketAccessor.isLegacyVersion()) {
                    TEAM_COLOR = PacketAccessor.getNMS(currentVersion.getColor());
                }
                if (PacketAccessor.isPushVersion(version)) {
                    PUSH = PacketAccessor.getNMS(currentVersion.getPush());
                }
                if (PacketAccessor.isVisibilityVersion(version)) {
                    VISIBILITY = PacketAccessor.getNMS(currentVersion.getVisibility());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

