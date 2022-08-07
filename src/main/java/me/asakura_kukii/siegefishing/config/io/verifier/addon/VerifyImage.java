package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.addondatatype.SiegeImage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VerifyImage extends Verifier {

    public VerifyImage() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyImage(s, fileName, path, root, obj, folder);
    }

    public static Object verifyImage(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        SiegeImage sI = null;
        if (folder.exists() && new File(folder, s).exists()) {
            File f = new File(folder, s);
            try {
                sI = new SiegeImage(f);
            } catch (Exception ignored) {
                FileIO.addErrorMsg(fileName, path, root, "[" + s + "] is not image");
                formatCorrect = false;
            }
        } else {
            FileIO.addErrorMsg(fileName, path, root, "[" + s + "] is not image path");
            formatCorrect = false;
        }

        if (formatCorrect) {
            return sI;
        } else {
            FileIO.putError(fileName);
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        SiegeImage sI = (SiegeImage) obj;
        fC.set(path, sI.f.getName());
        return fC;
    }
}
