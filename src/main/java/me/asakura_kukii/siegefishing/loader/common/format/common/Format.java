package me.asakura_kukii.siegefishing.loader.common.format.common;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public abstract class Format {
    public abstract Object check(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder);

    public static Object get(ConfigurationSection cS, String fileName, String path, Object obj, FormatType fT, boolean notEmpty, File folder) {
        String root = cS.getCurrentPath() + ".";
        if (root.matches(".")) {
            root = "";
        }

        if (cS.contains(path)) {
            String s = cS.getString(path);
            assert s != null;
            if (s.matches("")) {
                if (notEmpty) {
                    FileIO.fileStatusMapper.put(fileName, false);
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is empty\n");
                } else {
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is empty, neglected\n");
                }
                return obj;
            } else {
                return fT.f.check(cS, fileName, path, root, obj, folder);
            }
        } else {
            if (notEmpty) {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is missing\n");
            } else {
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is missing, neglected\n");
            }
            return obj;
        }
    }
}
