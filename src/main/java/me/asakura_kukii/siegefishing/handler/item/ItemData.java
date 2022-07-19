package me.asakura_kukii.siegefishing.handler.item;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ItemData extends FileData {

    //information
    public String displayName;
    public Material material;
    public int customModelIndex;
    public List<String> loreList;


    public boolean dropGlow;
    public Color dropGlowColor;

    public abstract ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level);

    public static void finalizeSendItemStack(ItemStack iS, PlayerData pD, int slot, int level) {
        if (slot == -1) {
            pD.p.getWorld().dropItem(pD.p.getLocation(), iS);
            return;
        }
        if (pD.p.getInventory().getHeldItemSlot() == slot) {
            pD.p.getInventory().setItem(40, iS);
            if (pD.p.getInventory().getItemInOffHand().getType() != Material.AIR) {
                if (pD.p.getInventory().firstEmpty() != -1) {
                    pD.p.getInventory().setItem(pD.p.getInventory().firstEmpty(), pD.p.getInventory().getItemInOffHand());
                } else {
                    pD.p.getWorld().dropItem(pD.p.getLocation(), pD.p.getInventory().getItemInOffHand());
                }
            }
            pD.p.getInventory().setItem(slot, HandData.getItemStack(pD.hand, pD, 6, level));
        } else {
            pD.p.getInventory().setItem(slot, iS);
        }
    }

    public static ItemStack getItemStack(ItemData iD, PlayerData pD, int amount) {
        return getItemStack(iD,pD,amount,randomizeLevel());
    }

    public static ItemStack getItemStack(ItemData iD, PlayerData pD, int amount, int level) {
        ItemStack iS = new ItemStack(iD.material);
        iS = NBTHandler.set(iS, "id", iD.identifier, false);

        ItemMeta iM = iS.getItemMeta();
        if (iM == null)
            iM = Bukkit.getServer().getItemFactory().getItemMeta(iD.material);
        assert iM != null;

        List<String> tempLoreList = new ArrayList<>(iD.loreList);
        iM.setLore(tempLoreList);
        iM.setCustomModelData(iD.customModelIndex);
        iM.setDisplayName(iD.displayName);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.setUnbreakable(true);

        iS.setItemMeta(iM);
        iS = iD.finalizeGetItemStack(iS, pD, level);
        iS.setAmount(amount);
        return iS;
    }

    public static void sendItemStack(ItemData iD, PlayerData pD, int amount) {
        sendItemStack(iD, pD, amount, pD.p.getInventory().firstEmpty());
    }

    public static void sendItemStack(ItemData iD, PlayerData pD, int amount, int slot) {
        ItemStack iS = ItemData.getItemStack(iD, pD, amount, randomizeLevel());
        finalizeSendItemStack(iS, pD, slot, randomizeLevel());
    }

    public static void sendItemStack(ItemData iD, PlayerData pD, int amount, int slot, int level) {
        ItemStack iS = ItemData.getItemStack(iD, pD, amount, level);
        finalizeSendItemStack(iS, pD, slot, (int)level);
    }

    public static int randomizeLevel() {
        Random r = new Random();
        return (int)Math.floor(r.nextDouble()*10.0+1.0);
    }
}
