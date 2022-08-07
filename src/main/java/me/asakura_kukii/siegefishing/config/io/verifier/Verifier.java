package me.asakura_kukii.siegefishing.config.io.verifier;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Field;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public abstract class Verifier {
    public abstract Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder);

    public abstract FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj);

    public static Field getField(ConfigurationSection cS, String fileName, String path, Class<?> dataClass) {
        String root = cS.getCurrentPath() + ".";
        if (root.matches(".")) {
            root = "";
        }
        try {
            return dataClass.getField(path);
        } catch (Exception ignored) {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + consolePluginPrefix + root + path + " is missing field\n");
        }
        return null;
    }

    public static Object getObject(ConfigurationSection cS, String fileName, String path, Object obj, VerifierType lT, boolean notEmpty, File folder) {
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
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + consolePluginPrefix + root + path + " is empty\n");
                } else {
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + consolePluginPrefix + root + path + " is empty, neglected\n");
                }
                return obj;
            } else {
                return lT.f.get(cS, fileName, path, root, obj, folder);
            }
        } else {
            if (notEmpty) {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + consolePluginPrefix + root + path + " is missing\n");
            } else {
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + consolePluginPrefix + root + path + " is missing, neglected\n");
            }
            return obj;
        }
    }

    public static FileConfiguration getString(FileConfiguration fC, FileData fD, String path, Object obj, VerifierType vT) {
        return vT.f.set(fC, fD, path, obj);
    }
}
