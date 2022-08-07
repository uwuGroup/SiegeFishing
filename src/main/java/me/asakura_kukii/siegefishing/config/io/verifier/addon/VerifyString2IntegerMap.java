package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.basic.VerifyInteger;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyString2IntegerMap extends Verifier {

    public VerifyString2IntegerMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<String, Integer> string2IntegerMap = new HashMap<>();
        for (String s : sL) {
            string2IntegerMap.putAll((Map<? extends String, ? extends Integer>) verifyString2IntegerMap(s, fileName, path, root, obj, folder));
        }
        return string2IntegerMap;
    }

    public static Object verifyString2IntegerMap(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        if (s.contains("^")) {
            if (s.split("\\^").length == 2) {
                for (String s2 : s.split("\\^")) {
                    if (index == 1) {
                        VerifyInteger.verifyInteger(s2, fileName, path, root, 0);
                    }
                    index ++;
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
            HashMap<String, Integer> string2IntegerMap = new HashMap<>();
            string2IntegerMap.put(s.split("\\^")[0], Integer.parseInt(s.split("\\^")[1]));
            return string2IntegerMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<String, Integer>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        HashMap<String, Integer> string2IntegerMap = (HashMap<String, Integer>) obj;
        for (String s : string2IntegerMap.keySet()) {
            Integer i = string2IntegerMap.get(s);
            String s1 = s + "^" + i.toString();
            sL.add(s1);
        }
        fC.set(path, sL);
        return fC;
    }
}
