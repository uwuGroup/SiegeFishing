package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyBoolean extends Verifier {

    public VerifyBoolean() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyBoolean(s, fileName, path, root, obj);
    }

    public static Object verifyBoolean(String s, String fileName, String path, String root, Object obj) {
        if (s.matches("true") || s.matches("false")) {
            return Boolean.parseBoolean(s);
        } else {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] is not boolean");
            return obj;
        }
    }
}
