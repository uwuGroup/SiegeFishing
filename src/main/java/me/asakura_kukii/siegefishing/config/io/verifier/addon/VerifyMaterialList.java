package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyMaterialList extends Verifier {
    public VerifyMaterialList() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Material> mL = new ArrayList<>();
        for (String s : sL) {
            Material m = (Material) VerifyMaterial.verifyMaterial(s, fileName, path, root, obj);
            mL.add(m);
        }
        return mL;
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        List<Material> mL = (List<Material>) obj;
        for (Material m : mL) {
            String s = m.name();
            sL.add(s);
        }
        fC.set(path, sL);
        return fC;
    }
}
