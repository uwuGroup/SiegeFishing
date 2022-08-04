package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyBooleanList extends Verifier {
    public VerifyBooleanList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Boolean> bL = new ArrayList<>();
        for (String s : sL) {
            Boolean b = (Boolean) VerifyBoolean.verifyBoolean(s, fileName, path, root, obj);
            bL.add(b);
        }
        return bL;
    }
}
