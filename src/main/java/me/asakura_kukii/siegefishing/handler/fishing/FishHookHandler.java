package me.asakura_kukii.siegefishing.handler.fishing;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;


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
