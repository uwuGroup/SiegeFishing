package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CrabRegionData extends FileData {
    public ImageValueData regionValue;
    public HashMap<FishData, Double> percentMap;

    public CrabRegionData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
