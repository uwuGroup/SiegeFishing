package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyFloat extends Verifier {

    public VerifyFloat() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyFloat(s, fileName, path, root, obj);
    }

    public static Object verifyFloat(String s, String fileName, String path, String root, Object obj) {
        try {
            return Float.parseFloat(s);
        } catch(Exception ignored) {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] is not float");
            return obj;
        }
    }
}
