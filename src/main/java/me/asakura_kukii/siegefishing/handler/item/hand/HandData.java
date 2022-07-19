package me.asakura_kukii.siegefishing.handler.item.hand;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HandData extends ItemData {
    public HandData(String identifier, String fileName){
        this.identifier = identifier;
        this.fileName = fileName;
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level) {
        iS = NBTHandler.set(iS, "type", "hand", false);
        ItemMeta iM = iS.getItemMeta();
        assert iM != null;
        int poseIndex = pD.poseState.ordinal();
        iM.setCustomModelData(this.customModelIndex * 3 + poseIndex);
        iS.setItemMeta(iM);
        return iS;
    }

    public static HandData getData(ItemStack iS) {
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("hand")) {
                if (FileType.HAND.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (HandData) FileType.HAND.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
