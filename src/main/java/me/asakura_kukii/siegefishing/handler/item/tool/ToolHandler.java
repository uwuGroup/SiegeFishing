package me.asakura_kukii.siegefishing.handler.item.tool;

import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.item.gun.util.PosingHandler;
import me.asakura_kukii.siegefishing.handler.item.hand.HandHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PoseType;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import me.asakura_kukii.siegefishing.handler.item.mod.ModHandler;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;

public class ToolHandler {
    public static boolean triggerTool(ToolData tD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        if (state.matches("onModify")) {
            modifyGun(tD, iS, s, pD, state, slotOfHand);
            return true;
        }
        if (state.matches("onRestore") || state.matches("onInitiate")) {
            PlayerHandler.restorePlayerData(pD.p);
            pD.poseState = PoseType.NORMAL;
            return false;
        }
        return false;
    }



    public static void modifyGun(ToolData tD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        RayTraceResult rTR = pD.p.getWorld().rayTraceEntities(pD.p.getEyeLocation(), pD.p.getLocation().getDirection(), 3, 4, p -> p.getType().equals(EntityType.ARMOR_STAND)&&p.getScoreboardTags().contains("modding_table"));
        if (rTR != null && rTR.getHitEntity() != null) {
            if (rTR.getHitEntity().getType().equals(EntityType.ARMOR_STAND)) {
                ArmorStand aS = (ArmorStand) rTR.getHitEntity();
                if (aS.getEquipment() != null && aS.getEquipment().getHelmet() != null) {
                    ItemStack tempIS = aS.getEquipment().getHelmet();
                    if (GunData.getData(tempIS) != null) {
                        GunData gD = GunData.getData(tempIS);
                        if (pD.stateCache.containsKey("Sneak") && pD.stateCache.get("Sneak")) {
                            aS.getEquipment().setHelmet(new ItemStack(Material.AIR));
                            PosingHandler.updatePlayerPose(pD);
                            PosingHandler.updateGunPose(gD, tempIS, s, pD);
                            HandHandler.safeUpdateHand(pD);
                        } else {
                            removeAllMod(gD, pD, tempIS, aS);
                        }
                    }
                }
            }
        }
    }

    public static void removeAllMod(GunData gD, PlayerData pD, ItemStack iS, ArmorStand aS) {
        for (int i = 0; i < gD.modSlotCount; i++) {
            if (NBTHandler.contains(iS, "slot_" + i)) {
                String oldModNBTString = (String) NBTHandler.get(iS, "slot_" + i, null);
                ItemStack oldModItemStack = ModHandler.modNbtStringToModItemStack(oldModNBTString);

                iS = NBTHandler.remove(iS, "slot_" + i);
                iS = ModHandler.calculateCurrentData(gD, iS);
                Objects.requireNonNull(aS.getEquipment()).setItem(EquipmentSlot.HEAD, iS);

                ItemData.finalizeSendItemStack(oldModItemStack, pD, pD.p.getInventory().firstEmpty(), ItemData.randomizeLevel());
            }
        }
    }
}
