package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyChunk extends Verifier {

    public VerifyChunk() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
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
                            Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not world");
                            formatCorrect = false;
                        }
                    } else {
                        try {
                            Integer.parseInt(s2);
                        } catch (Exception ignored) {
                            Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not integer");
                            formatCorrect = false;
                        }
                    }
                    index++;
                }
            } else {
                Loader.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                formatCorrect = false;
            }
        } else {
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
            formatCorrect = false;
        }
        if (formatCorrect) {
            World w = Bukkit.getWorld(s.split("\\^")[0]);
            assert w != null;
            return w.getChunkAt(Integer.parseInt(s.split("\\^")[1]), Integer.parseInt(s.split("\\^")[2]));
        } else {
            Loader.putError(fileName);
            return obj;
        }
    }
}
