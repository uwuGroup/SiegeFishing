package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Locale;

public class TypeMethod extends Format {
    public TypeMethod() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = cS.getString(path);
        return checkTypeMethod(s, fileName, path, root, obj);
    }

    public static Object checkTypeMethod(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        try {
            return MethodType.valueOf(s.toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not MethodType\n");
            return obj;
        }
    }
}
