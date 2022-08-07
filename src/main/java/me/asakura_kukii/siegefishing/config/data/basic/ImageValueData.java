package me.asakura_kukii.siegefishing.config.data.basic;


import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.addondatatype.SiegeImage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class ImageValueData extends FileData {

    public SiegeImage image;
    public Chunk referenceChunk = Bukkit.getWorlds().get(0).getChunkAt(0,0);
    public double valueMin = 0;
    public double valueMax = 1;
    public double valueNormal = 0;

    public ImageValueData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public double getRelativeValueAt(int x, int z) {
        int relativeX = x - referenceChunk.getX();
        int relativeZ = z - referenceChunk.getZ();

        double d = this.valueNormal;
        try {
            d = this.image.greyPercent[relativeX][relativeZ] * (valueMax - valueMin) + valueMin;
        } catch (Exception ignored) {
        }
        return d;
    }

    public double getRealValueAt(int x, int z) {
        double d = this.valueNormal;
        try {
            d = this.image.greyPercent[x][z] * (valueMax - valueMin) + valueMin;
        } catch (Exception ignored) {
        }
        return d;
    }
}
