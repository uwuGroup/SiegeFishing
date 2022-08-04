package me.asakura_kukii.siegefishing.data.basic;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import org.bukkit.Material;
import org.bukkit.Particle;

public class ParticleData extends FileData {

    public Particle particle;
    public int count = 1;
    public float extra = 1;
    public Material material;
    public float sizeX = 0;
    public float sizeY = 0;
    public float sizeZ = 0;
    public int colorR = 0;
    public int colorG = 0;
    public int colorB = 0;
    public double biasX = 0;
    public double biasY = 0;
    public double biasZ = 0;

    public ParticleData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
