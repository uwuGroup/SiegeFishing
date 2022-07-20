package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;

public class Chunk extends Format {

    public Chunk() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = (java.lang.String) FormatType.STRING.f.check(cS, fileName, path, root, obj, folder);
        return checkChunk(s, fileName, path, root, obj);
    }

    public static Object checkChunk(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==3) {
                boolean b = true;
                for (java.lang.String s2 : s.split("\\^")) {
                    if (b) {
                        World w = Bukkit.getWorld(s2);
                        if (w == null) {
                            formatCorrect = false;
                        }
                        b = false;
                    } else {
                        try {
                            java.lang.Integer.parseInt(s2);
                        } catch (Exception ignored) {
                            formatCorrect = false;
                        }
                    }
                }
            } else {
                formatCorrect = false;
            }
        } else {
            formatCorrect = false;
        }

        if (formatCorrect) {
            return Objects.requireNonNull(Bukkit.getWorld(s.split("\\^")[0])).getChunkAt(java.lang.Integer.parseInt(s.split("\\^")[1]), java.lang.Integer.parseInt(s.split("\\^")[2]));
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + " is not Chunk\n");
            return obj;
        }
    }
}
