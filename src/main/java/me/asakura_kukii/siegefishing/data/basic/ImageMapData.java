package me.asakura_kukii.siegefishing.data.basic;


import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeImage;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import me.asakura_kukii.siegefishing.utility.format.Color;
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
            return regionNameDefault;
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
            return regionNameDefault;
        }
        SiegeVector key = null;
        for (SiegeVector sV : rgbRegionNameMap.keySet()) {
            if (sV.x == red && sV.y == green && sV.z == blue) {
                key = sV;
            }
        }

        if (key == null || !rgbRegionNameMap.containsKey(key)) {
            return regionNameDefault;
        }
        String regionName = rgbRegionNameMap.get(key);
        if (regionName.contains("%c")) {
            regionName = regionName.replaceAll("%c", Color.generateColorCode(red, green, blue) + "");
        }
        return regionName;
    }
}
