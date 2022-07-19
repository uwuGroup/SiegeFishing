package me.asakura_kukii.siegefishing.handler.item.fish;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.item.mod.ModData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.entity.Fish;
import org.bukkit.inventory.ItemStack;

public class FishData extends ItemData {
    public double spawnTimeMin;
    public double spawnTimeMax;
    public double spawnTempMin;
    public double spawnTempMax;
    public double dragForceMax;
    public double struggleFactor;
    //public HashMap<, Double>

    public FishData(String identifier, String fileName){
        this.identifier = identifier;
        this.fileName = fileName;
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level) {
        return iS;
    }

    public static FishData getData(ItemStack iS) {
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("fish")) {
                if (FileType.MOD.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (FishData) FileType.MOD.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
