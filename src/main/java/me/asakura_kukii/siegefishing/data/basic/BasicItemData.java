package me.asakura_kukii.siegefishing.data.basic;

import me.asakura_kukii.siegefishing.data.common.ItemData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

public class BasicItemData extends ItemData {

    public BasicItemData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemData iD, ItemStack iS, PlayerData pD, int level) {
        return iS;
    }

    public static BasicItemData getData(ItemStack iS) {
        if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("basicItem")) {
                if (FileType.FISH.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (BasicItemData) FileType.FISH.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
