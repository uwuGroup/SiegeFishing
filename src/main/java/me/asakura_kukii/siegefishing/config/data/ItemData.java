package me.asakura_kukii.siegefishing.config.data;

import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class ItemData extends FileData {

    //information
    public String displayName = "";
    public Material material = Material.COOKIE;
    public int customModelIndex = 0;
    public List<String> loreList = new ArrayList<>();

    public ItemData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public abstract ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra);

    public abstract boolean trigger(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS);

    public static FileType getItemType(ItemStack iS) {
        if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            String typeName = (String) NBTHandler.get(iS, "type", String.class);
            if (FileType.getFileTypeFromName(typeName) == null) {
                return null;
            }
            return FileType.getFileTypeFromName(typeName);
        }
        return null;
    }

    public static ItemData getItemData(ItemStack iS) {
        if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            String typeName = (String) NBTHandler.get(iS, "type", String.class);
            if (FileType.getFileTypeFromName(typeName) == null) {
                return null;
            }
            FileType fT = FileType.getFileTypeFromName(typeName);
            String identifier = (String) NBTHandler.get(iS, "id", String.class);
            assert fT != null;
            if (!fT.map.containsKey(identifier)) {
                return null;
            }
            return (ItemData) fT.map.get(identifier);
        }
        return null;
    }

    public static ItemStack getItemStack(ItemData iD, PlayerData pD, int amount, int extra) {
        ItemStack iS = new ItemStack(iD.material);
        iS = NBTHandler.set(iS, "id", iD.identifier, false);
        iS = NBTHandler.set(iS, "type", iD.fT.typeName, false);
        ItemMeta iM = iS.getItemMeta();
        if (iM == null)
            iM = Bukkit.getServer().getItemFactory().getItemMeta(iD.material);
        assert iM != null;


        iM.setCustomModelData(iD.customModelIndex);
        iM.setDisplayName(FormatHandler.format(iD.displayName, false));
        iM.setLore(FormatHandler.format(iD.loreList));
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.setUnbreakable(true);

        iS.setItemMeta(iM);
        iS = iD.finalizeGenerateItemStack(iS, pD, extra);
        iS.setAmount(amount);
        return iS;
    }

    public static void sendItemStack(ItemData iD, PlayerData pD, int amount, int extra) {
        ItemStack iS = ItemData.getItemStack(iD, pD, amount, extra);

        if (pD.p.getInventory().firstEmpty() == -1) {
            Objects.requireNonNull(pD.p.getLocation().getWorld()).dropItem(pD.p.getEyeLocation(), iS);
        } else {
            pD.p.getInventory().setItem(pD.p.getInventory().firstEmpty(), iS);
        }
    }

    public static void sendItemStack(ItemData iD, PlayerData pD, int amount, int extra, int slot) {
        ItemStack iS = ItemData.getItemStack(iD, pD, amount, extra);
        pD.p.getInventory().setItem(slot, iS);
    }
}
