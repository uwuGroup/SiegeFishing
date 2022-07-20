package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListParticle extends Format {
    public ListParticle() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<ParticleData> pDL = new ArrayList<>();
        for (java.lang.String s : sL) {
            ParticleData pD = (ParticleData) Particle.checkParticle(s, fileName, path, root, obj);
            pDL.add(pD);
        }
        return pDL;
    }
}
