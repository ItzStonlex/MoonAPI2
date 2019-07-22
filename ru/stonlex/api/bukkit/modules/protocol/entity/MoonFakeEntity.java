package ru.stonlex.api.bukkit.modules.protocol.entity;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.stonlex.api.bukkit.modules.protocol.packet.impl.*;
import ru.stonlex.api.java.interfaces.Clickable;

import java.util.*;

@Getter
public abstract class MoonFakeEntity {

    private static final TIntObjectMap<MoonFakeEntity> entities = new TIntObjectHashMap<>();

    public static Collection<MoonFakeEntity> getEntities() {
        return entities.valueCollection();
    }

    public static MoonFakeEntity getEntityById(int id) {
        return entities.get(id);
    }

    private static int getNextId() {
        try {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> entity = Class.forName("net.minecraft.server." + version + ".Entity");

            FieldAccessor fieldAccessor = Accessors.getFieldAccessor(entity, "entityCount", true);
            final int result = (int) (fieldAccessor.get(null));

            fieldAccessor.set(null, result + 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    private Location location;

    private final Map<EnumWrappers.ItemSlot, ItemStack> equipment;

    private final EntityType entityType;

    private final List<Player> receivers;

    private final int id;

    private final WrappedDataWatcher dataWatcher;

    private Vector velocity;

    private byte headYaw;

    private Clickable<Player> clickable;

    public MoonFakeEntity(EntityType entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
        this.equipment = new HashMap<>();
        this.receivers = new ArrayList<>();
        this.id = getNextId();
        this.dataWatcher = new WrappedDataWatcher();
        this.velocity = new Vector();

        entities.put(id, this);
    }

    private void sendPacket(Player receiver, PacketType packetType) {
        switch (packetType) {
            case SPAWN: {
                WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving();

                packet.setEntityID(id);
                packet.setType(entityType);
                packet.setMetadata(dataWatcher);

                packet.setX(location.getX());
                packet.setY(location.getY());
                packet.setZ(location.getZ());

                packet.setYaw(location.getYaw());
                location.setPitch(location.getPitch());

                packet.sendPacket(receiver);
                break;
            }
            case DESTROY: {
                WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();

                packet.setEntityIds(new int[]{id});

                packet.sendPacket(receiver);
                break;
            }
            case METADATA: {
                WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();

                packet.setEntityID(id);
                packet.setMetadata(dataWatcher.getWatchableObjects());

                packet.sendPacket(receiver);
                break;
            }
            case TELEPORT: {
                WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();

                packet.setEntityID(id);

                packet.setX(location.getX());
                packet.setY(location.getY());
                packet.setZ(location.getZ());

                packet.setYaw(location.getYaw());
                packet.setPitch(location.getPitch());

                packet.sendPacket(receiver);
                break;
            }
            case EQUIPMENT: {
                for (EnumWrappers.ItemSlot slot : equipment.keySet()) {
                    ItemStack itemStack = equipment.get(slot);
                    if (itemStack == null) {
                        continue;
                    }

                    WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();

                    packet.setEntityID(id);
                    packet.setSlot(slot);
                    packet.setItem(itemStack);

                    packet.sendPacket(receiver);
                }
                break;
            }
            case HEAD_ROTATION: {
                WrapperPlayServerEntityHeadRotation packet = new WrapperPlayServerEntityHeadRotation();

                packet.setEntityID(id);
                packet.setHeadYaw(headYaw);

                packet.sendPacket(receiver);
                break;
            }
            case VELOCITY: {
                WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity();

                packet.setEntityID(id);

                packet.setVelocityX(velocity.getX());
                packet.setVelocityY(velocity.getY());
                packet.setVelocityZ(velocity.getZ());

                packet.sendPacket(receiver);
                break;
            }
        }
    }

    private void sendPacket(PacketType packetType) {
        for (Player receiver : receivers) {
            sendPacket(receiver, packetType);
        }
    }

    private void broadcastPacket(PacketType packetType) {
        receivers.clear();
        receivers.addAll(Bukkit.getOnlinePlayers());

        sendPacket(packetType);
    }

    private enum PacketType {
        SPAWN, DESTROY, METADATA, TELEPORT, VELOCITY, EQUIPMENT, HEAD_ROTATION
    }

    public void addReceiver(Player player) {
        if (player == null) {
            throw new RuntimeException("поч игрок null");
        }
        receivers.add(player);

        sendPacket(player, PacketType.SPAWN);
    }

    public void removeReceiver(Player player) {
        if (player == null) {
            throw new RuntimeException("поч игрок null");
        }
        receivers.remove(player);

        sendPacket(player, PacketType.DESTROY);
    }

    public boolean hasReceiver(Player player) {
        if (player == null) {
            throw new RuntimeException("поч игрок null");
        }
        return receivers.contains(player);
    }

    public void updateForReceiver(Player player) {
        if (player == null) {
            throw new RuntimeException("поч игрок null");
        }

        sendPacket(player, PacketType.METADATA);
    }

    public void updateWatcher() {
        sendPacket(PacketType.METADATA);
    }

    /*
    Тут сеттеры
     */

    public void setClickable(Clickable<Player> clickable) {
        this.clickable = clickable;
    }

    public void setLocation(Location location) {
        this.location = location;

        sendPacket(PacketType.TELEPORT);
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;

        sendPacket(PacketType.VELOCITY);
    }

    public void setEquipment(EnumWrappers.ItemSlot slot, ItemStack itemStack) {
        equipment.put(slot, itemStack);

        sendPacket(PacketType.EQUIPMENT);
    }

    public void setHeadPosition(byte headYaw) {
        this.headYaw = headYaw;

        sendPacket(PacketType.HEAD_ROTATION);
    }

    /*
    Entity
     */

    public void setBurning(boolean burning) {
        setFlag(0, 0, burning);

        updateWatcher();
    }

    public boolean isBurning() {
        return getFlag(0, 0);
    }

    public void setSneaking(boolean sneaking) {
        setFlag(0, 1, sneaking);

        updateWatcher();
    }

    public boolean isSneaking() {
        return getFlag(0, 1);
    }

    public void setSprinting(boolean sprinting) {
        setFlag(0, 3, sprinting);

        updateWatcher();
    }

    public boolean isSprinting() {
        return getFlag(0, 3);
    }

    public void setInvisible(boolean invisible) {
        setFlag(0, 5, invisible);

        updateWatcher();
    }

    public boolean isInvisible() {
        return getFlag(0, 5);
    }

    public void setAirTicks(short airTicks) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1, serializer), airTicks);

        updateWatcher();
    }

    public short getAirTicks() {
        return dataWatcher.getShort(1);
    }

    public void setCustomName(String name) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(String.class, false);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, serializer), name);

        updateWatcher();
    }

    public String getCustomName() {
        return dataWatcher.getString(2);
    }

    public void setCustomNameVisible(boolean visible) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Boolean.class);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, serializer), visible);

        updateWatcher();
    }

    public boolean getCustomNameVisible() {
        return dataWatcher.getByte(3) == 1;
    }

    public void setSilent(boolean visible) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class, false);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, serializer), (byte) (visible ? 1 : 0));

        updateWatcher();
    }

    public boolean isSilent() {
        return dataWatcher.getByte(4) == 1;
    }

    public float getHealth() {
        return dataWatcher.getFloat(6);
    }

    public void setHealth(float health) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Float.class, false);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(6, serializer), health);

        updateWatcher();
    }

    public boolean getFlag(int index, int num) {
        Byte value = dataWatcher.getByte(index);
        if (value == null) return false;
        return (value & (1 << num)) != 0;
    }

    public void setFlag(int index, int num, boolean flag) {
        //Byte value = dataWatcher.getByte(index);
        //if (value == null) {
        //    value = (byte) 0;
        //}
        //if (flag) {
        //    value = (byte)(value | (1 << num));
        //} else {
        //    value = (byte)(value & ~(1 << num));
        //}
        WrappedDataWatcher.WrappedDataWatcherObject isInvisibleIndex = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isInvisibleIndex, (byte) 0x20);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj != null && !(obj instanceof MoonFakeEntity)) return false;

        MoonFakeEntity fakeEntity = (MoonFakeEntity) obj;
        if (fakeEntity != null && fakeEntity.id == this.id) {
            return true;
        }
        return false;
    }

}
