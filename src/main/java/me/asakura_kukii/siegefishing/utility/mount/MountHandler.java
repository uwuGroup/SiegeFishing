package me.asakura_kukii.siegefishing.utility.mount;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MountHandler {
    public static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public static HashMap<UUID, List<Integer>> entityIdUUIDMap = new HashMap<>();
    public static HashMap<UUID, List<PacketContainer>> generatePacketContainerMap = new HashMap<>();
    public static List<Integer> occupiedEntityIdList = new ArrayList<>();


    //TODO:REFRESH IS NOT WORKING, DESTROY PACKETS UNSTABLE WHEN DISCONNECTING
    //TODO:ADD PLAYER-ITEMSTACK HASHMAP FOR CHANGING MOUNTING MODELS

    public static void refreshMount(Player p) {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }

        for (int i = 0; i < 2; i++) {
            PacketContainer destroyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            int eID = entityIdUUIDMap.get(p.getUniqueId()).get(i);
            destroyPacket.getIntegers().write(0, eID);

            try {
                protocolManager.sendServerPacket(p, destroyPacket);
                for (Entity target : p.getNearbyEntities(32, 32, 32)) {
                    if (target instanceof Player) {
                        protocolManager.sendServerPacket((Player) target, destroyPacket);
                    }
                }
                occupiedEntityIdList.remove(eID);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        entityIdUUIDMap.remove(p.getUniqueId());
        generatePacketContainerMap.remove(p.getUniqueId());
    }

    public static void generateMount(Player p) {

        if (entityIdUUIDMap.containsKey(p.getUniqueId())) {
            refreshMount(p);
        }


        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }

        List<Integer> entityIDList = new ArrayList<>();
        List<PacketContainer> pCL = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            int entityID = (int) ((Math.random() * 0.5 + 0.5) * Integer.MAX_VALUE);
            //ensuring unique
            while(occupiedEntityIdList.contains(entityID)) {
                entityID = (int) ((Math.random() * 0.5 + 0.5) * Integer.MAX_VALUE);
            }
            PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            spawnPacket.getIntegers().write(0, entityID).write(1, 1);
            spawnPacket.getUUIDs().write(0, UUID.randomUUID());
            spawnPacket.getDoubles().write(0, p.getLocation().getX()).write(1, p.getLocation().getY()).write(2, p.getLocation().getZ());

            PacketContainer metaPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            metaPacket.getIntegers().write(0, entityID);
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
            WrappedDataWatcher.WrappedDataWatcherObject thing1 = new WrappedDataWatcher.WrappedDataWatcherObject(15, serializer);
            WrappedDataWatcher.WrappedDataWatcherObject thing2 = new WrappedDataWatcher.WrappedDataWatcherObject(0, serializer);
            byte value1 = (byte) 0x10;
            WrappedWatchableObject item1 = new WrappedWatchableObject(thing1, value1);
            byte value2 = (byte) 0x20;
            WrappedWatchableObject item2 = new WrappedWatchableObject(thing2, value2);
            metaPacket.getWatchableCollectionModifier().write(0, Lists.newArrayList(item1, item2));

            PacketContainer equipPacket = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
            equipPacket.getIntegers().write(0, entityID);
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
            ItemStack boatItem = new ItemStack(Material.COOKIE);
            ItemMeta iM = boatItem.getItemMeta();
            iM.setCustomModelData(i+1);
            boatItem.setItemMeta(iM);
            list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, boatItem));
            equipPacket.getSlotStackPairLists().write(0, list);



            try {
                protocolManager.sendServerPacket(p, spawnPacket);
                protocolManager.sendServerPacket(p, metaPacket);
                protocolManager.sendServerPacket(p, equipPacket);
                for (Entity target : p.getNearbyEntities(32, 32, 32)) {
                    if (target instanceof Player) {
                        protocolManager.sendServerPacket((Player) target, spawnPacket);
                        protocolManager.sendServerPacket((Player) target, metaPacket);
                        protocolManager.sendServerPacket((Player) target, equipPacket);
                    }
                }
                entityIDList.add(entityID);
                occupiedEntityIdList.add(entityID);
                pCL.add(spawnPacket);
                pCL.add(metaPacket);
                pCL.add(equipPacket);

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        PacketContainer mountPacket = new PacketContainer(PacketType.Play.Server.MOUNT);
        mountPacket.getIntegers().write(0, p.getEntityId());
        mountPacket.getIntegerArrays().write(0, new int[]{entityIDList.get(0), entityIDList.get(1)});

        try {
            protocolManager.sendServerPacket(p, mountPacket);
            for (Entity target : p.getNearbyEntities(32, 32, 32)) {
                if (target instanceof Player) {
                    protocolManager.sendServerPacket((Player) target, mountPacket);
                }
            }
            pCL.add(mountPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        entityIdUUIDMap.put(p.getUniqueId(), entityIDList);
        generatePacketContainerMap.put(p.getUniqueId(), pCL);
    }

    public static void orientMount(Player p) {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
        if (entityIdUUIDMap.containsKey(p.getUniqueId())) {

            for (int i = 0; i < 2; i ++) {
                PacketContainer rotateHeadPacket = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
                rotateHeadPacket.getIntegers().write(0, entityIdUUIDMap.get(p.getUniqueId()).get(i));
                if (p.getVehicle() != null && p.getVehicle() instanceof Boat) {
                    rotateHeadPacket.getBytes().write(0, (byte) (p.getVehicle().getLocation().getYaw() * 256 / 360));
                } else {
                    rotateHeadPacket.getBytes().write(0, (byte) (p.getLocation().getYaw() * 256 / 360));
                }

                PacketContainer rotateBodyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
                rotateBodyPacket.getIntegers().write(0, entityIdUUIDMap.get(p.getUniqueId()).get(i));
                if (p.getVehicle() != null && p.getVehicle() instanceof Boat) {
                    rotateBodyPacket.getBytes().write(0, (byte) (p.getVehicle().getLocation().getYaw() * 256 / 360));
                } else {
                    rotateBodyPacket.getBytes().write(0, (byte) (p.getLocation().getYaw() * 256 / 360));
                }
                rotateBodyPacket.getBytes().write(1, (byte) 0);

                try {
                    protocolManager.sendServerPacket(p, rotateHeadPacket);
                    protocolManager.sendServerPacket(p, rotateBodyPacket);
                    for (Entity target : p.getNearbyEntities(32, 32, 32)) {
                        if (target instanceof Player) {
                            protocolManager.sendServerPacket((Player) target, rotateHeadPacket);
                            protocolManager.sendServerPacket((Player) target, rotateBodyPacket);
                        }
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        } else {
            generateMount(p);
        }
    }
}
