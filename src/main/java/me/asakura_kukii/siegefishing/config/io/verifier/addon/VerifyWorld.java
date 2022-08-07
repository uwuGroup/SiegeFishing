package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class VerifyWorld extends Verifier {

    public VerifyWorld() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyWorld(s, fileName, path, root, obj, folder);
    }

    public static Object verifyWorld(String s, String fileName, String path, String root, Object obj, File folder) {
        if (Bukkit.getWorld(s) != null) {
            return Bukkit.getWorld(s);
        } else {
            FileIO.putError(fileName);
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] is not world");
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        fC.set(path, ((World) obj).getName());
        return fC;
    }
}
