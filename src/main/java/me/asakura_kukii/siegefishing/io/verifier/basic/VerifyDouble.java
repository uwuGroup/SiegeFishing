package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyDouble extends Verifier {

    public VerifyDouble() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyDouble(s, fileName, path, root, obj);
    }

    public static Object verifyDouble(String s, String fileName, String path, String root, Object obj) {
        try {
            return Double.parseDouble(s);
        } catch(Exception ignored) {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] is not double");
            return obj;
        }
    }
}
