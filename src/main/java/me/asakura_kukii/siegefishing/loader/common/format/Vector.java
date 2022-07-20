package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class Vector extends Format {

    public Vector() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = (java.lang.String) FormatType.STRING.f.check(cS, fileName, path, root, obj, folder);
        return checkVector(s, fileName, path, root, obj);
    }

    public static Object checkVector(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==3 || s.split("\\^").length==5) {
                for (java.lang.String s2 : s.split("\\^")) {
                    try {
                        java.lang.Double.parseDouble(s2);
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
            } else {
                formatCorrect = false;
            }
        } else {
            formatCorrect = false;
        }

        if (formatCorrect) {
            return new Vector3D(java.lang.Double.parseDouble(s.split("\\^")[0]), java.lang.Double.parseDouble(s.split("\\^")[1]), java.lang.Double.parseDouble(s.split("\\^")[2]));
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is not Vector3D\n");
            return obj;
        }
    }
}
