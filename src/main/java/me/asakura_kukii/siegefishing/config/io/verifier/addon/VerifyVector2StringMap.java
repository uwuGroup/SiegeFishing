package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.addondatatype.SiegeVector;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyVector2StringMap extends Verifier {

    public VerifyVector2StringMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
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
                            FileIO.addErrorMsg(fileName, root, path, "[" + s + "][" + s2 + "] is not double");
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
            HashMap<SiegeVector, String> vector2StringMap = new HashMap<>();
            vector2StringMap.put(new SiegeVector(Double.parseDouble(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2])), ChatColor.translateAlternateColorCodes('&', s.split("\\^")[3]));
            return vector2StringMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<SiegeVector, String>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        HashMap<SiegeVector, String> vector2StringMap = (HashMap<SiegeVector, String>) obj;
        for (SiegeVector sV : vector2StringMap.keySet()) {
            String s = vector2StringMap.get(sV);
            String s1 = sV.x + "^" + sV.y + "^" + sV.z + "^" + s;
            sL.add(s1);
        }
        fC.set(path, sL);
        return fC;
    }
}
