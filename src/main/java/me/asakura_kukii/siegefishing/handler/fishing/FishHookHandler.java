package me.asakura_kukii.siegefishing.handler.fishing;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;
import me.asakura_kukii.siegefishing.data.basic.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FishHookHandler {
    public static HashMap<UUID, UUID> hookMap = new HashMap<>();

    public static void throwHook(Player p) {
        if (!hookMap.containsKey(p.getUniqueId())) {
            ArmorStand aS = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            aS.setSmall(true);
            aS.setInvulnerable(true);
            aS.setInvisible(true);
            aS.setGravity(true);
            Objects.requireNonNull(aS.getEquipment()).setHelmet(new ItemStack(Material.COOKIE));
            hookMap.put(p.getUniqueId(), aS.getUniqueId());
        }
    }

    public static void spawnHook(Player p) {

    }

    public static void updateHook(Player p) {

    }
}
