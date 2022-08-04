package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyMaterial extends Verifier {

    public VerifyMaterial() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyMaterial(s, fileName, path, root, obj);
    }

    public static Object verifyMaterial(String s, String fileName, String path, String root, Object obj) {
        if (org.bukkit.Material.matchMaterial(s) != null) {
            return Objects.requireNonNull(org.bukkit.Material.matchMaterial(s));
        } else {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, root, path, "[" + s + "] is not material");
            return obj;
        }
    }
}
