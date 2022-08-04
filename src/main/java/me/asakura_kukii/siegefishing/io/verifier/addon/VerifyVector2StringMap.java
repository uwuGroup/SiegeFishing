package me.asakura_kukii.siegefishing.io.verifier.addon;

import it.unimi.dsi.fastutil.Hash;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyVector2StringMap extends Verifier {

    public VerifyVector2StringMap() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<SiegeVector, String> vector2StringMap = new HashMap<>();
        for (String s : sL) {
            vector2StringMap.putAll((Map<? extends SiegeVector, ? extends String>) VerifyVector2StringMap.verifyVector2String(s, fileName, path, root, obj));
        }
        return vector2StringMap;
    }

    public static Object verifyVector2String(String s, String fileName, String path, String root, Object obj) {
        boolean formatCorrect = true;
        int index = 0;
        if (s.contains("^")) {
            if (s.split("\\^").length == 4) {
                for (String s2 : s.split("\\^")) {
                    if (index <= 2) {
                        try {
                            Double.parseDouble(s2);
                        } catch (Exception ignored) {
                            Loader.addErrorMsg(fileName, root, path, "[" + s + "][" + s2 + "] is not double");
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
            HashMap<SiegeVector, String> vector2StringMap = new HashMap<>();
            vector2StringMap.put(new SiegeVector(Double.parseDouble(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2])), ChatColor.translateAlternateColorCodes('&', s.split("\\^")[3]));
            return vector2StringMap;
        } else {
            Loader.putError(fileName);
            return new HashMap<SiegeVector, String>();
        }
    }
}
