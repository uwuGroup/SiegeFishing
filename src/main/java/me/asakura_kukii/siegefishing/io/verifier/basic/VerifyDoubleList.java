package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyDoubleList extends Verifier {
    public VerifyDoubleList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Double> dL = new ArrayList<>();
        for (String s : sL) {
            Double d = (Double) VerifyDouble.verifyDouble(s, fileName, path, root, obj);
            dL.add(d);
        }
        return dL;
    }
}
