package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyVectorList extends Verifier {
    public VerifyVectorList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<SiegeVector> vL = new ArrayList<>();
        for (String s : sL) {
            SiegeVector v = (SiegeVector) VerifyVector.verifyVector(s, fileName, path, root, obj);
            vL.add(v);
        }
        return vL;
    }
}
