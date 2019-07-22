package ru.stonlex.api.bukkit.modules.protocol.entity;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class FakeArmorStand extends AbstractFakeEntity {

    public FakeArmorStand(Location location) {
        super(EntityType.ARMOR_STAND, location);
    }

    /**
     * Установка флага 'Small'
     */
    public void setSmall(boolean flag) {
        Byte raw = getDataWatcher().getByte(10);
        byte value = (raw == null ? (byte)0 : raw);
        if (flag) {
            value = (byte)(value | 1);
        } else {
            value &= (byte)-2;
        }
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);

        getDataWatcher().setObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, serializer), value);
        updateWatcher();
    }

    /**
     * Проверка флага 'Small'
     */
    public boolean isSmall() {
        return (getDataWatcher().getByte(10) & 1) != 0;
    }

    /**
     * Установка флага 'Gravity'
     */
    public void setGravity(boolean flag) {
        Byte raw = getDataWatcher().getByte(10);
        byte value = (raw == null ? (byte)0 : raw);
        if (flag) {
            value = (byte)(value | 2);
        } else {
            value &= (byte)-3;
        }
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);

        getDataWatcher().setObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, serializer), value);
        updateWatcher();
    }

    /**
     * Проверка флага 'Gravity'
     */
    public boolean hasGravity() {
        return (getDataWatcher().getByte(10) & 2) != 0;
    }

    /**
     * Установка флага 'Arms'
     */
    public void setArms(boolean flag) {
        Byte raw = getDataWatcher().getByte(10);
        byte value = (raw == null ? (byte)0 : raw);
        if (flag) {
            value = (byte)(value | 4);
        } else {
            value &= (byte)-5;
        }
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);

        getDataWatcher().setObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, serializer), value);
        updateWatcher();
    }

    /**
     * Проверка флага 'Arms'
     */
    public boolean hasArms() {
        return (getDataWatcher().getByte(10) & 4) != 0;
    }

    /**
     * Установка флага 'BasePlate'
     */
    public void setBasePlate(boolean flag) {
        Byte raw = getDataWatcher().getByte(10);
        byte value = (raw == null ? (byte)0 : raw);
        if (flag) {
            value = (byte)(value | 8);
        } else {
            value &= (byte)-9;
        }
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);

        getDataWatcher().setObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, serializer), value);
        updateWatcher();
    }

    /**
     * Проверка флага 'BasePlate'
     */
    public boolean hasBasePlate() {
        return (getDataWatcher().getByte(10) & 8) != 0;
    }

    /**
     * Установка флага 'Marker'
     */
    public void setMarker(boolean flag) {
        Byte raw = getDataWatcher().getByte(10);
        byte value = (raw == null ? (byte)0 : raw);
        if (flag) {
            value = (byte)(value | 16);
        } else {
            value &= (byte)-17;
        }
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);

        getDataWatcher().setObject(new WrappedDataWatcher.WrappedDataWatcherObject(10, serializer), value);
        updateWatcher();
    }

    /**
     * Проверка флага 'Marker'
     */
    public boolean hasMarker() {
        return (getDataWatcher().getByte(10) & 16) != 0;
    }

}
