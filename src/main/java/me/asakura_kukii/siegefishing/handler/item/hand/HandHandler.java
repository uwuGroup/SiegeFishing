package me.asakura_kukii.siegefishing.handler.item.hand;

import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class HandHandler {
    public static boolean triggerHand(HandData hD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        //firing handler
        if(state.matches("onUpdate")) {
            updateHand(hD, iS, s, pD, state, slotOfHand);
            return true;
        }
        return false;
    }

    public static void safeUpdateHand(PlayerData pD) {
        for (int i = 41; i >= 40; i--) {
            ItemStack iS;
            int slot;

            if (i == 41) {
                iS = pD.p.getInventory().getItemInMainHand();
                slot = pD.p.getInventory().getHeldItemSlot();
            } else {
                iS = pD.p.getInventory().getItem(i);
                slot = i;
            }

            if (iS == null) {
                continue;
            }
            ItemStack clonedIS = iS.clone();
            if (NBTHandler.hasSiegeWeaponCompoundTag(iS)) {
                if (HandData.getData(iS) != null) {
                    ItemStack handIS = ItemData.getItemStack(pD.hand, pD, 6);
                    pD.p.getInventory().setItem(slot, handIS);
                }
            }
        }
    }

    public static void updateHand(HandData hD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        ItemStack handIS = ItemData.getItemStack(pD.hand, pD, 6);
        pD.p.getInventory().setItem(s, handIS);
    }

    public static boolean triggerGenerateAndDestroyHand(PlayerData pD, String state, int slotOfHand) {
        //firing handler
        if(state.matches("onRestore") || state.matches("onInitiate")) {
            generateAndDestroyHand(pD, state, slotOfHand);
            return false;
        }
        return false;
    }

    public static void initHand(PlayerData pD, String state, int slotOfHand) {
        ItemStack mainIS = pD.p.getInventory().getItem(slotOfHand);
        String dN = Objects.requireNonNull(GunData.getData(mainIS)).displayName;
        ItemStack handIS = ItemData.getItemStack(pD.hand, pD, 6);
        ItemMeta iM = handIS.getItemMeta();
        assert iM != null;
        iM.setDisplayName(dN);
        handIS.setItemMeta(iM);
        pD.p.getInventory().setItem(40 ,mainIS);
        pD.p.getInventory().setItem(slotOfHand, handIS);
    }

    public static void restoreHand(PlayerData pD, String state, int slotOfHand) {
        ItemStack mainIS = pD.p.getInventory().getItemInOffHand();
        pD.p.getInventory().setItem(slotOfHand ,mainIS);
        pD.p.getInventory().setItem(40, ItemData.getItemStack(pD.hand, pD, 6));
    }

    public static void removeHand(PlayerData pD, String state, int slotOfHand) {

    }

    public static int checkHand(PlayerData pD, int slot) {
        ItemStack iS = pD.p.getInventory().getItem(slot);
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS)) {
            if (GunData.getData(iS) != null) {return 1;}
            //if hand
            if (HandData.getData(iS) != null) {return 2;}
        }
        return 0;
    }
    public static void generateAndDestroyHand(PlayerData pD, String state, int slotOfHand) {
        if (checkHand(pD, slotOfHand) == 1) {
            //main is exchange item
            initHand(pD, state, slotOfHand);
        } else if (checkHand(pD, slotOfHand) == 2) {
            //main is hand
            restoreHand(pD, state, slotOfHand);
        } else if (checkHand(pD, slotOfHand) == 0 && checkHand(pD, 40) == 2) {
            PlayerHandler.restorePlayerData(pD.p);
            pD.p.getInventory().setItem(40, new ItemStack(Material.AIR));
        }
    }
}
