package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.basic.VerifyDouble;
import me.asakura_kukii.siegefishing.config.io.verifier.basic.VerifyInteger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyInteger2DoubleMap extends Verifier {

    public VerifyInteger2DoubleMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<Integer, Double> integer2DoubleMap = new HashMap<>();
        for (String s : sL) {
            integer2DoubleMap.putAll((Map<? extends Integer, ? extends Double>) verifyInteger2DoubleMap(s, fileName, path, root, obj, folder));
        }
        return integer2DoubleMap;
    }

    public static Object verifyInteger2DoubleMap(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        if (s.contains("^")) {
            if (s.split("\\^").length == 2) {
                for (String s2 : s.split("\\^")) {
                    if (index == 0) {
                        VerifyDouble.verifyDouble(s2, fileName, path, root, 0.0);
                    }
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
            HashMap<Integer, Double> integer2DoubleMap = new HashMap<>();
            integer2DoubleMap.put(Integer.parseInt(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]));
            return integer2DoubleMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<Integer, Double>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        HashMap<Integer, Double> integer2DoubleMap = (HashMap<Integer, Double>) obj;
        for (Integer i : integer2DoubleMap.keySet()) {
            Double d = integer2DoubleMap.get(i);
            String s1 = i.toString() + "^" + d.toString();
            sL.add(s1);
        }
        fC.set(path, sL);
        return fC;
    }
}
