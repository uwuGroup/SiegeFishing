package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.utility.coodinate.Image2D;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class Image extends Format {

    public Image() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = (java.lang.String) FormatType.STRING.f.check(cS, fileName, path, root, obj, folder);
        return checkImage(s, fileName, path, root, obj, folder);
    }

    public static Object checkImage(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {

        if (folder.exists() && new File(folder, s + "png").exists()) {
            File f = new File(s + "png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(f);
                return new Image2D(img);
            } catch (Exception ignored) {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not correct image path\n");
                return obj;
            }
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not correct image path\n");
            return obj;
        }
    }


}
