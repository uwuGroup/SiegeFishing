package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.addondatatype.SiegeVector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyVectorList extends Verifier {
    public VerifyVectorList() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<SiegeVector> vL = new ArrayList<>();
        for (String s : sL) {
            SiegeVector v = (SiegeVector) VerifyVector.verifyVector(s, fileName, path, root, obj);
            vL.add(v);
        }
        return vL;
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        List<SiegeVector> vL = (List<SiegeVector>) obj;
        for (SiegeVector sV : vL) {
            String s = sV.x + "^" + sV.y + "^" + sV.z;
            sL.add(s);
        }
        fC.set(path, sL);
        return fC;
    }
}
