package me.asakura_kukii.siegefishing.data.basic;


import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeImage;
import org.bukkit.Chunk;

public class ImageValueData extends FileData {

    public SiegeImage image;
    public Chunk referenceChunk;
    public double valueMin;
    public double valueMax;
    public double valueNormal;

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
