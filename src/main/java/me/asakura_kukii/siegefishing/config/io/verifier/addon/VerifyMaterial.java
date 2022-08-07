package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Objects;

public class VerifyMaterial extends Verifier {

    public VerifyMaterial() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyMaterial(s, fileName, path, root, obj);
    }

    public static Object verifyMaterial(String s, String fileName, String path, String root, Object obj) {
        if (org.bukkit.Material.matchMaterial(s) != null) {
            return Objects.requireNonNull(org.bukkit.Material.matchMaterial(s));
        } else {
            FileIO.putError(fileName);
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] is not material");
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        Material m = (Material) obj;
        fC.set(path, m.name());
        return fC;
    }
}
