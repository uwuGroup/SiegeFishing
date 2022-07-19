package me.asakura_kukii.siegefishing.handler.item.gun.util;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.tool.ToolData;
import me.asakura_kukii.siegefishing.handler.item.gun.fire.FullAutoHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.reload.Reload;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;

public class ModdingHandler {
    public static void initModding(GunData gD, ItemStack iS, int s, PlayerData pD, String state) {
        RayTraceResult rTR = pD.p.getWorld().rayTraceEntities(pD.p.getEyeLocation(), pD.p.getLocation().getDirection(), 3, 4, p -> p.getType().equals(EntityType.ARMOR_STAND));
        if (rTR != null && rTR.getHitEntity() != null && rTR.getHitEntity().getScoreboardTags().contains("modding_table")) {
            if (rTR.getHitEntity().getType().equals(EntityType.ARMOR_STAND)) {
                ArmorStand aS = (ArmorStand) rTR.getHitEntity();
                if (GunData.getData(pD.p.getInventory().getItem(s)) != null) {

                    if (FullAutoHandler.checkFullAutoFiring(pD)) {
                        iS = FullAutoHandler.breakFullAutoFiring(pD);
                    }
                    if (Reload.checkReloading(pD)) {
                        Reload.breakReloading(pD);
                    }
                    if (ScopingHandler.checkScoping(pD)) {
                        ScopingHandler.breakScoping(pD);
                    }
                    if (pD.gunReserve != -1) {
                        iS = GunData.setReserve(gD, iS, pD.gunReserve);
                        pD.gunReserve = -1;
                    }

                    if (aS.getEquipment() == null || (aS.getEquipment() != null && aS.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.AIR)) {
                        Objects.requireNonNull(aS.getEquipment()).setItem(EquipmentSlot.HEAD, iS);
                        pD.p.getInventory().setItem(s, ToolData.getItemStack((ItemData) FileType.TOOL.map.get("default"), pD, 1));
                    }

                    /*else if (aS.getEquipment() != null && aS.getEquipment().getItemInMainHand() != null) {
                        ItemStack tempIS = aS.getEquipment().getItemInMainHand();
                        if (NBTHandler.hasSiegeWeaponCompoundTag(tempIS)) {
                            if (((String) NBTHandler.get(tempIS, "type", String.class)).matches("gun")) {
                                if (GunData.gunDataMapper.containsKey((String) NBTHandler.get(tempIS, "id", String.class))) {
                                    Objects.requireNonNull(aS.getEquipment()).setItem(EquipmentSlot.HEAD, iS);
                                    pD.p.getInventory().setItem(pD.p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
                                    //TODO: init
                                    //PosingHandler.initGunPose(pD, tempIS, pD.p.getInventory().getHeldItemSlot());
                                }
                            }
                        }
                    }*/
                }
            }
        }
    }
}
