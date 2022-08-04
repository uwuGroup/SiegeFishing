package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyVector extends Verifier {

    public VerifyVector() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyVector(s, fileName, path, root, obj);
    }

    public static Object verifyVector(String s, String fileName, String path, String root, Object obj) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==3 || s.split("\\^").length==5) {
                for (String s2 : s.split("\\^")) {
                    try {
                        Double.parseDouble(s2);
                    } catch (Exception ignored) {
                        Loader.addErrorMsg(fileName, root, path, "[" + s + "][" + s2 + "] is not double");
                        formatCorrect = false;
                    }
                }
            } else {
                Loader.addErrorMsg(fileName, root, path, "[" + s + "] lacks argument");
                formatCorrect = false;
            }
        } else {
            Loader.addErrorMsg(fileName, root, path, "[" + s + "] has invalid argument count");
            formatCorrect = false;
        }

        if (formatCorrect) {
            return new SiegeVector(Double.parseDouble(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2]));
        } else {
            Loader.putError(fileName);
            return obj;
        }
    }
}
