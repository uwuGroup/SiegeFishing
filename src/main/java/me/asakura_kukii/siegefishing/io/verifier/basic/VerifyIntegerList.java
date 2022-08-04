package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyIntegerList extends Verifier {
    public VerifyIntegerList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Integer> iL = new ArrayList<>();
        for (String s : sL) {
            Integer i = (Integer) VerifyInteger.verifyInteger(s, fileName, path, root, obj);
            iL.add(i);
        }
        return iL;
    }
}
