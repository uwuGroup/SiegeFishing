package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ParticleIO extends FileIO {

    public ParticleIO() {}

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        ParticleData pD = new ParticleData(identifier, fN);

        Particle particle = Particle.ASH;
        int c = 1;
        float dx = 0;
        float dy = 0;
        float dz = 0;
        float e = 1;
        int r = 0;
        int g = 0;
        int b = 0;
        double bx = 0;
        double by = 0;
        double bz = 0;
        Material material = Material.ANDESITE;

        //reading...
        pD.particle = (Particle) FormatHandler.checkConfigurationFormat(fC, fN, "particle", particle, true);
        pD.c = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "c", c, true);
        pD.dx = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "dx", dx, true);
        pD.dy = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "dy", dy, true);
        pD.dz = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "dz", dz, true);
        pD.e = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "e", e, true);
        pD.r = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "r", r, true);
        pD.g = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "g", g, true);
        pD.b = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "b", b, true);
        pD.bx = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "bx", bx, true);
        pD.by = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "by", by, true);
        pD.bz = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "bz", bz, true);
        pD.material = (Material) FormatHandler.checkConfigurationFormat(fC, fN, "material", material, true);

        return pD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {
        ParticleData pD = new ParticleData("default_blood", "");
        pD.particle = Particle.BLOCK_DUST;
        pD.c = 5;
        pD.dx = (float) 0.05;
        pD.dy = (float) 0.05;
        pD.dz = (float) 0.05;
        pD.e = 1;
        pD.r = 166;
        pD.g = 0;
        pD.b = 0;
        pD.bx = 0;
        pD.by = 0;
        pD.bz = 0;
        pD.material = Material.REDSTONE_BLOCK;
        fT.map.put(pD.identifier, (FileData) pD);

        pD = new ParticleData("default_laser", "");
        pD.particle = Particle.REDSTONE;
        pD.c = 1;
        pD.dx = (float) 0.0;
        pD.dy = (float) 0.0;
        pD.dz = (float) 0.0;
        pD.e = (float) 0.5;
        pD.r = 255;
        pD.g = 0;
        pD.b = 0;
        pD.bx = 0;
        pD.by = 0;
        pD.bz = 0;
        pD.material = Material.REDSTONE_BLOCK;
        fT.map.put(pD.identifier, (FileData) pD);
    }
}
