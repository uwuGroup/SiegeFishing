package me.asakura_kukii.siegefishing.handler.item.gun.util;

import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PoseType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PosingHandler {
    public static int updatePlayerPose(PlayerData pD) {
        if (pD.stateCache.get("Carry")) {
            pD.poseState = PoseType.GUN_CARRY;
            return PoseType.GUN_CARRY.ordinal();
        }
        if (pD.stateCache.get("Scope")) {
            pD.poseState = PoseType.GUN_SCOPE;
            return PoseType.GUN_SCOPE.ordinal();
        }
        pD.poseState = PoseType.GUN_HOLD;
        return PoseType.GUN_HOLD.ordinal();
    }

    public static ItemStack updateGunPose(GunData gD, ItemStack iS, int s, PlayerData pD) {
        int poseIndex = pD.poseState.ordinal();
        //update bullet
        if (pD.gunReserve != -1) {
            iS = GunData.setReserve(gD, iS, pD.gunReserve);
            pD.gunReserve = -1;
        }
        net.minecraft.world.item.ItemStack nmsIS = CraftItemStack.asNMSCopy(iS);
        ItemStack modifiedIS = CraftItemStack.asBukkitCopy(nmsIS);
        ItemMeta iM = modifiedIS.getItemMeta();
        assert iM != null;
        Integer customModification = (Integer) NBTHandler.get(((String) NBTHandler.get(iS, "current", null)), "cMIB", Integer.class);
        if (poseIndex >= 3) {
            poseIndex = 0;
        }
        if ((iM.getCustomModelData() - gD.customModelIndex * 81 - customModification) / 27 == poseIndex) {
            pD.p.getInventory().setItem(s, iS);
            return iS;
        } else {
            iM.setCustomModelData(gD.customModelIndex * 81 + poseIndex * 27 + customModification);
            modifiedIS.setItemMeta(iM);

            pD.p.getInventory().setItem(s, modifiedIS);
            return modifiedIS;
        }
    }


    public static void initGunPose(GunData gD, ItemStack iS, int s, PlayerData pD, String state) {
        /*if (pD.p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            if (pD.p.getInventory().firstEmpty() != -1) {
                pD.p.getInventory().setItem(pD.p.getInventory().firstEmpty(), pD.p.getInventory().getItemInOffHand());
            } else {
                pD.p.getWorld().dropItem(pD.p.getLocation(), pD.p.getInventory().getItemInOffHand());
            }
        }*/
        if (pD.stateCache.containsKey("Scope") && pD.stateCache.get("Scope")) {
            ScopingHandler.initScoping(gD, iS, pD);
        }
        updatePlayerPose(pD);
        updateGunPose(gD, iS, s, pD);
        //StateHandler.updateGunPoseIndexToHandItemStack(slotOfHand, pD);
    }

    public static void restoreGunPose(GunData gD, ItemStack iS, int s, PlayerData pD, String state) {
        if (pD.gunReserve != -1) {
            iS = GunData.setReserve(gD, iS, pD.gunReserve);
            pD.gunReserve = -1;
        }
        if (pD.stateCache.containsKey("Scope") && pD.stateCache.get("Scope")) {
            ScopingHandler.breakScoping(pD);
        }

        updatePlayerPose(pD);
        updateGunPose(gD, iS, s, pD);
    }

    public static void resetGunPose(PlayerData pD) {
        ItemStack iS = pD.p.getInventory().getItemInOffHand();
        GunData gD = GunData.getData(iS);
        PlayerHandler.restorePlayerData(pD.p);
        pD.poseState = PoseType.GUN_HOLD;
        updatePlayerPose(pD);
        updateGunPose(gD, iS, 40, pD);
        //updateGunPoseIndexToHandItemStack(pD.p.getInventory().getHeldItemSlot(), pD);
    }
}
