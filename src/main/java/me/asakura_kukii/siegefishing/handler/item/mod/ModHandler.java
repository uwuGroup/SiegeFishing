package me.asakura_kukii.siegefishing.handler.item.mod;

import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PoseType;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialType;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModHandler {
    public static boolean triggerMod(ModData mD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        if (state.matches("onModify")) {
            modifyGun(mD, iS, s, pD, state, slotOfHand);
            return true;
        }
        if (state.matches("onRestore") || state.matches("onInitiate")) {
            PlayerHandler.restorePlayerData(pD.p);
            pD.poseState = PoseType.NORMAL;
            return false;
        }
        return false;
    }

    public static void spawnModdingTable(Location l) {
        ArmorStand aS = (ArmorStand) Objects.requireNonNull(l.getWorld()).spawnEntity(l, EntityType.ARMOR_STAND);
        aS.setArms(true);
        aS.setBasePlate(false);
        aS.setInvisible(true);
        aS.setInvulnerable(true);
        aS.setCustomNameVisible(true);
        aS.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6&lModding Table"));
        aS.setLeftArmPose(new EulerAngle(0,0,0));
        aS.setRightArmPose(new EulerAngle(0,0,0));
        aS.setHeadPose(new EulerAngle(0, Math.PI / 2, 0));
        aS.getScoreboardTags().add("modding_table");
        ItemStack iS = new ItemStack(Material.COOKIE);
        ItemMeta iM = iS.getItemMeta();
        if (iM == null)
            iM = Bukkit.getServer().getItemFactory().getItemMeta(Material.COOKIE);
        assert iM != null;
        iM.setCustomModelData(1);
        iS.setItemMeta(iM);
        Objects.requireNonNull(aS.getEquipment()).setItem(EquipmentSlot.HAND, iS);
        aS.setPersistent(true);
        aS.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        aS.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        aS.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
        aS.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        aS.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
        aS.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
    }

    public static ItemStack calculateCurrentData(GunData gD, ItemStack iS) {
        ItemStack clonedIS = iS.clone();

        List<String> potentialString = PotentialHandler.readPotentialFromNBT(iS, PotentialType.WEAPON);
        List<List<Double>> potentialDataList = new ArrayList<>();
        for (String s : potentialString) {
            potentialDataList.add(PotentialHandler.interpretPotential(s));
        }


        int customModelIndexBias = 0;

        double damageMultiplier = 1.00 * potentialDataList.get(0).get(0);
        double horizontalRecoilMultiplier = 1.00;
        double verticalRecoilMultiplier = 1.00;
        double accurateTimeMultiplier = 1.00;
        double inaccuracyMultiplier = 1.00 * (2 - potentialDataList.get(2).get(0));
        double rangeMultiplier = 1.00;
        boolean silenceGun = false;

        Boolean overrideScope = false;
        Boolean scopePotionEffect = false;
        Integer scopeZoomLevel = 0;
        Boolean scopeNightVision = false;
        Double scopeSpeedCompensation = 1.0;
        for (int i = 0; i < gD.modSlotCount; i++) {
            if (NBTHandler.contains(clonedIS, "slot_" + i)) {
                String nbtString = (String) NBTHandler.get(clonedIS, "slot_" + i, null);
                customModelIndexBias += ((Integer) NBTHandler.get(nbtString, "mCMI", Integer.class)) * ((Integer) NBTHandler.get(nbtString, "mCMIM", Integer.class));



                damageMultiplier *= (Double) NBTHandler.get(nbtString, "dM", Double.class);
                horizontalRecoilMultiplier  *= (Double) NBTHandler.get(nbtString, "hRM", Double.class);
                verticalRecoilMultiplier *= (Double) NBTHandler.get(nbtString, "vRM", Double.class);
                accurateTimeMultiplier *= (Double) NBTHandler.get(nbtString, "aTM", Double.class);
                inaccuracyMultiplier *= (Double) NBTHandler.get(nbtString, "iM", Double.class);
                rangeMultiplier *= (Double) NBTHandler.get(nbtString, "rM", Double.class);



                if ((Boolean) NBTHandler.get(nbtString, "sG", Boolean.class)) {
                    silenceGun = true;
                }
                if ((Boolean) NBTHandler.get(nbtString, "oS", Boolean.class)) {
                    overrideScope = true;
                    scopePotionEffect = (Boolean) NBTHandler.get(nbtString, "sPE", Boolean.class);
                    scopeZoomLevel = (Integer) NBTHandler.get(nbtString, "sZL", Integer.class);
                    scopeNightVision = (Boolean) NBTHandler.get(nbtString, "sNV", Boolean.class);
                    scopeSpeedCompensation = (Double) NBTHandler.get(nbtString, "sSC", Double.class);
                }
            }
        }
        String currentNbtString = "";
        currentNbtString = NBTHandler.set(currentNbtString, "cMIB", customModelIndexBias, false);
        currentNbtString = NBTHandler.set(currentNbtString, "dM", damageMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "hRM", horizontalRecoilMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "vRM", verticalRecoilMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "sG", silenceGun, false);
        currentNbtString = NBTHandler.set(currentNbtString, "aTM", accurateTimeMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "iM", inaccuracyMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "rM", rangeMultiplier, false);
        currentNbtString = NBTHandler.set(currentNbtString, "oS", overrideScope, false);
        currentNbtString = NBTHandler.set(currentNbtString, "sPE", scopePotionEffect, false);
        currentNbtString = NBTHandler.set(currentNbtString, "sZL", scopeZoomLevel, false);
        currentNbtString = NBTHandler.set(currentNbtString, "sNV", scopeNightVision, false);
        currentNbtString = NBTHandler.set(currentNbtString, "sSC", scopeSpeedCompensation, false);
        clonedIS = NBTHandler.set(clonedIS, "current", currentNbtString, true);
        ItemMeta iM = clonedIS.getItemMeta();
        assert iM != null;
        iM.setCustomModelData(gD.customModelIndex * 81 + customModelIndexBias);
        List<String> loreList = gD.loreList;
        loreList = PotentialHandler.formatPotentialRelatedStringList(iS, loreList, PotentialType.WEAPON);
        iM.setLore(loreList);
        clonedIS.setItemMeta(iM);
        return clonedIS;
    }

    public static ItemStack modNbtStringToModItemStack(String nbtString) {
        String modId = (String) NBTHandler.get(nbtString, "id", String.class);
        if (FileType.MOD.map.containsKey(modId)) {
            ModData oldModData = (ModData) FileType.MOD.map.get(modId);
            ItemStack newModItemStack = ItemData.getItemStack(oldModData, null, 1);
            newModItemStack = NBTHandler.set(newModItemStack, "", nbtString, true);
            return newModItemStack;
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    public static void modifyGun(ModData mD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        RayTraceResult rTR = pD.p.getWorld().rayTraceEntities(pD.p.getEyeLocation(), pD.p.getLocation().getDirection(), 3, 4, p -> p.getType().equals(EntityType.ARMOR_STAND)&&p.getScoreboardTags().contains("modding_table"));
        if (rTR != null && rTR.getHitEntity() != null) {
            if (rTR.getHitEntity().getType().equals(EntityType.ARMOR_STAND)) {
                ArmorStand aS = (ArmorStand) rTR.getHitEntity();
                if (aS.getEquipment() != null && aS.getEquipment().getHelmet() != null) {
                    ItemStack tempIS = aS.getEquipment().getHelmet();
                    if (GunData.getData(tempIS) != null) {
                        GunData gD = GunData.getData(tempIS);
                        if (NBTHandler.contains(tempIS, "slot_" + mD.modSlot)) {
                            String oldModNBTString = (String) NBTHandler.get(tempIS, "slot_" + mD.modSlot, null);
                            ItemStack oldModItemStack = modNbtStringToModItemStack(oldModNBTString);

                            tempIS = NBTHandler.set(tempIS, "slot_" + mD.modSlot, NBTHandler.get(iS, "", null), true);
                            tempIS = ModHandler.calculateCurrentData(gD, tempIS);
                            aS.getEquipment().setItem(EquipmentSlot.HEAD, tempIS);

                            pD.p.getInventory().setItem(s, oldModItemStack);
                        } else {
                            tempIS = NBTHandler.set(tempIS, "slot_" + mD.modSlot, NBTHandler.get(iS, "", null), true);
                            tempIS = ModHandler.calculateCurrentData(gD, tempIS);
                            aS.getEquipment().setItem(EquipmentSlot.HEAD, tempIS);
                            pD.p.getInventory().setItem(s, new ItemStack(Material.AIR));
                            pD.p.getInventory().setItem(slotOfHand, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
}
