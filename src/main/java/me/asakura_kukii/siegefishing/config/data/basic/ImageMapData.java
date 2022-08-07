package me.asakura_kukii.siegefishing.config.data.basic;


import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.addondatatype.SiegeImage;
import me.asakura_kukii.siegefishing.addondatatype.SiegeVector;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.Chunk;

import java.util.HashMap;

public class ImageMapData extends FileData {

    public SiegeImage image;
    public Chunk referenceChunk;
    public String regionNameDefault;
    public HashMap<SiegeVector, String> rgbRegionNameMap = new HashMap<>();

    public ImageMapData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public String getRegionNameAt(int x, int z) {
        int relativeX = x - referenceChunk.getX();
        int relativeZ = z - referenceChunk.getZ();
        if (!image.withinImage(relativeX, relativeZ)) {
            return FormatHandler.format(regionNameDefault, false);
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        try {
            red = this.image.red[relativeX][relativeZ];
            green = this.image.green[relativeX][relativeZ];
            blue = this.image.blue[relativeX][relativeZ];
        } catch (Exception ignored) {
        }
        if (red == 0 && green == 0 && blue == 0) {
            return FormatHandler.format(regionNameDefault, false);
        }
        SiegeVector key = null;
        for (SiegeVector sV : rgbRegionNameMap.keySet()) {
            if (sV.x == red && sV.y == green && sV.z == blue) {
                key = sV;
            }
        }

        if (key == null || !rgbRegionNameMap.containsKey(key)) {
            return FormatHandler.format(regionNameDefault, false);
        }
        String regionName = rgbRegionNameMap.get(key);
        if (regionName.contains("%rgb%")) {
            regionName = regionName.replaceAll("%rgb%", ColorHandler.gen(red, green, blue) + "");
        }
        return FormatHandler.format(regionName, false);
    }
}
