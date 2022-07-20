package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class Boolean extends Format {

    public Boolean() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = (java.lang.String) FormatType.STRING.f.check(cS, fileName, path, root, obj, folder);
        return checkBoolean(s, fileName, path, root, obj);
    }

    public static Object checkBoolean(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        if (s.matches("true") || s.matches("false")) {
            return java.lang.Boolean.parseBoolean(s);
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not Boolean\n");
            return obj;
        }
    }
}
