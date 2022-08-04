package me.asakura_kukii.siegefishing.data.addon;

import me.asakura_kukii.siegefishing.data.common.ItemData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FishData extends ItemData {
    public double spawnTimeMin;
    public double spawnTimeMax;
    public double difficulty;
    public double annoyingFactor;
    public List<String> collectorDescriptionList;
    public List<String> possibleConversationList;

    public FishData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    @Override
    public ItemStack finalizeGetItemStack(ItemData iD, ItemStack iS, PlayerData pD, int level) {
        return iS;
    }

    public static FishData getData(ItemStack iS) {
        if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("fish")) {
                if (FileType.FISH.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (FishData) FileType.FISH.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
