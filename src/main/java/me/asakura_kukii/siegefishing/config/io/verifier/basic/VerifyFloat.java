package me.asakura_kukii.siegefishing.config.io.verifier.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class VerifyFloat extends Verifier {

    public VerifyFloat() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyFloat(s, fileName, path, root, obj);
    }

    public static Object verifyFloat(String s, String fileName, String path, String root, Object obj) {
        try {
            return Float.parseFloat(s);
        } catch(Exception ignored) {
            FileIO.putError(fileName);
            FileIO.addErrorMsg(fileName, path, root, "[" + s + "] is not float");
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        fC.set(path, obj);
        return fC;
    }
}
