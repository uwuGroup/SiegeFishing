package me.asakura_kukii.siegefishing.config.io.verifier.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyFloatList extends Verifier {
    public VerifyFloatList() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Float> fL = new ArrayList<>();
        for (String s : sL) {
            Float f = (Float) VerifyFloat.verifyFloat(s, fileName, path, root, obj);
            fL.add(f);
        }
        return fL;
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        fC.set(path, obj);
        return fC;
    }
}
