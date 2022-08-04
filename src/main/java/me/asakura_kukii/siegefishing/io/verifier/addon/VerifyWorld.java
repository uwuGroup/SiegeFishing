package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeImage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyWorld extends Verifier {

    public VerifyWorld() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyWorld(s, fileName, path, root, obj, folder);
    }

    public static Object verifyWorld(String s, String fileName, String path, String root, Object obj, File folder) {
        if (Bukkit.getWorld(s) != null) {
            return Bukkit.getWorld(s);
        } else {
            Loader.putError(fileName);
            Loader.addErrorMsg(fileName, root, path, "[" + s + "] is not world");
            return obj;
        }
    }
}
