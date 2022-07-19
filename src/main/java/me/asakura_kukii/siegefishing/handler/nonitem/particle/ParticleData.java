package me.asakura_kukii.siegefishing.handler.nonitem.particle;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import org.bukkit.Material;
import org.bukkit.Particle;

public class ParticleData extends FileData {

    public Particle particle;
    public int c = 1;
    public float dx = 0;
    public float dy = 0;
    public float dz = 0;
    public float e = 1;
    public int r = 0;
    public int g = 0;
    public int b = 0;
    public double bx = 0;
    public double by = 0;
    public double bz = 0;
    public Material material;

    public ParticleData(String identifier, String fileName) {
        this.identifier = identifier;
        this.fileName = fileName;
    }
}
