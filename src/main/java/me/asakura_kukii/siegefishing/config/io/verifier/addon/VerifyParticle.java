package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class VerifyParticle extends Verifier {
    public VerifyParticle() {}

    @Override
    public Object get(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyParticle(s, fileName, path, root, obj);
    }

    public static Object verifyParticle(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        try {
            return Particle.valueOf(s.toUpperCase());
        } catch (Exception ignored) {
            FileIO.putError(fileName);
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] is not particle");
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        Particle p = (Particle) obj;
        fC.set(path, p.name());
        return fC;
    }
}
