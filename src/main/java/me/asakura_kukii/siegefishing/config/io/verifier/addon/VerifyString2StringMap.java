package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyString2StringMap extends Verifier {

    public VerifyString2StringMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<String, String> string2StringMap = new HashMap<>();
        for (String s : sL) {
            string2StringMap.putAll((Map<? extends String, ? extends String>) verifyString2String(s, fileName, path, root, obj, folder));
        }
        return string2StringMap;
    }

    public static Object verifyString2String(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length != 2) {
                FileIO.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                formatCorrect = false;
            }
        } else {
            FileIO.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
            formatCorrect = false;
        }

        if (formatCorrect) {
            HashMap<String, String> string2StringMap = new HashMap<>();
            string2StringMap.put(s.split("\\^")[0], s.split("\\^")[1]);
            return string2StringMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<String, String>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        HashMap<String, String> string2StringMap = (HashMap<String, String>) obj;
        for (String s : string2StringMap.keySet()) {
            String s1 = string2StringMap.get(s);
            String s2 = s + "^" + s1;
            sL.add(s2);
        }
        fC.set(path, sL);
        return fC;
    }
}
