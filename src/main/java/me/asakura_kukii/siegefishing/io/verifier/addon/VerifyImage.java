package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeImage;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyImage extends Verifier {

    public VerifyImage() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyImage(s, fileName, path, root, obj, folder);
    }

    public static Object verifyImage(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        SiegeImage sI = null;
        if (folder.exists() && new File(folder, s).exists()) {
            File f = new File(folder, s);
            BufferedImage img = null;
            try {
                img = ImageIO.read(f);
                sI = new SiegeImage(img);
            } catch (Exception ignored) {
                Loader.addErrorMsg(fileName, path, root, "[" + s + "] is not image");
                formatCorrect = false;
            }
        } else {
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] is not image path");
            formatCorrect = false;
        }

        if (formatCorrect) {
            return sI;
        } else {
            Loader.putError(fileName);
            return obj;
        }
    }
}
