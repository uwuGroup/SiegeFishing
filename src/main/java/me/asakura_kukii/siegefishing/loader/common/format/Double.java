package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class Double extends Format {

    public Double() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = (java.lang.String) FormatType.STRING.f.check(cS, fileName, path, root, obj, folder);
        return checkDouble(s, fileName, path, root, obj);
    }

    public static Object checkDouble(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        try {
            return java.lang.Double.parseDouble(s);
        } catch(Exception ignored) {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not Double\n");
            return obj;
        }
    }
}
