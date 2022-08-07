package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class VerifyChunk extends Verifier {

    public VerifyChunk() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyChunk(s, fileName, path, root, obj, folder);
    }

    public static Object verifyChunk(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        if (s.contains("^")) {
            if (s.split("\\^").length==3) {
                for (String s2 : s.split("\\^")) {
                    if (index == 0) {
                        if (Bukkit.getWorld(s2) == null) {
                            FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not world");
                            formatCorrect = false;
                        }
                    } else {
                        try {
                            Integer.parseInt(s2);
                        } catch (Exception ignored) {
                            FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not integer");
                            formatCorrect = false;
                        }
                    }
                    index++;
                }
            } else {
                FileIO.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                formatCorrect = false;
            }
        } else {
            FileIO.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
            formatCorrect = false;
        }
        if (formatCorrect) {
            World w = Bukkit.getWorld(s.split("\\^")[0]);
            assert w != null;
            return w.getChunkAt(Integer.parseInt(s.split("\\^")[1]), Integer.parseInt(s.split("\\^")[2]));
        } else {
            FileIO.putError(fileName);
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        Chunk c = (Chunk) obj;
        fC.set(path, c.getWorld().getName() + "^" + c.getX() + "^" + c.getZ());
        return fC;
    }
}
