package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FishRegionData extends ItemData {
    public ImageValueData regionValue;
    public HashMap<FishData, Double> percentMap;

    public FishRegionData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemData iD, ItemStack iS, PlayerData pD, int level) {
        return iS;
    }

    public static FishRegionData getData(ItemStack iS) {
        if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("fish")) {
                if (FileType.FISH.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (FishRegionData) FileType.FISH.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
