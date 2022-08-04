package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyParticle extends Verifier {
    public VerifyParticle() {}

    @Override
    public Object verify(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyParticle(s, fileName, path, root, obj);
    }

    public static Object verifyParticle(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        try {
            return Particle.valueOf(s.toUpperCase());
        } catch (Exception ignored) {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, root, path, "[" + s + "] is not particle");
            return obj;
        }
    }
}
