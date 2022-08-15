package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class VerifyPotion extends Verifier {
    public VerifyPotion() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyPotion(s, fileName, path, root, obj);
    }

    public static Object verifyPotion(String s, String fileName, String path, String root, Object obj) {
        try {
            return PotionEffectType.getByName(s.toUpperCase());
        } catch (Exception ignored) {
            FileIO.putError(fileName);
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] is not potion effect");
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        PotionEffectType p = (PotionEffectType) obj;
        fC.set(path, p.toString());
        return fC;
    }
}
