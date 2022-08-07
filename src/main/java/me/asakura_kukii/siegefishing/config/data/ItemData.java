package me.asakura_kukii.siegefishing.config.data;

import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
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

    public abstract ItemStack finalizeGetItemStack(ItemData iD, ItemStack iS, PlayerData pD, int level);

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

    public static ItemStack getItemStack(ItemData iD, PlayerData pD, int amount, int level) {
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
        iS = iD.finalizeGetItemStack(iD, iS, pD, level);
        iS.setAmount(amount);
        return iS;
    }

    public static void finalizeSendItemStack(ItemStack iS, PlayerData pD, int slot, int level) {
        pD.p.getInventory().setItem(slot, iS);
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
