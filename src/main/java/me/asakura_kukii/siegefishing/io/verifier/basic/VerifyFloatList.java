package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyFloatList extends Verifier {
    public VerifyFloatList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Float> fL = new ArrayList<>();
        for (String s : sL) {
            Float f = (Float) VerifyFloat.verifyFloat(s, fileName, path, root, obj);
            fL.add(f);
        }
        return fL;
    }
}
