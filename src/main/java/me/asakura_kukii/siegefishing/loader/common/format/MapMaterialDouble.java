package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class MapMaterialDouble extends Format {

    public MapMaterialDouble() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        HashMap<org.bukkit.Material, java.lang.Double> mDM = new HashMap<>();
        for (java.lang.String s : sL) {
            if (checkMapMaterialDouble(s)) {
                int index = 0;
                org.bukkit.Material m = org.bukkit.Material.ANDESITE;
                double d = 0.00;
                for (java.lang.String s2 : s.split("\\^")) {
                    if (index == 0) {
                        m = Material.matchMaterial(s2);
                    } else {
                        d = java.lang.Double.parseDouble(s2);
                    }
                    index++;
                }
                mDM.put(m, d);
            } else {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not MaterialDoubleMap\n");
                return obj;
            }
        }

        return mDM;
    }

    public static boolean checkMapMaterialDouble(java.lang.String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==2) {
                int index = 0;
                for (java.lang.String s2 : s.split("\\^")) {
                    try {
                        if (index == 0) {
                            if (Material.matchMaterial(s2) == null) {
                                formatCorrect = false;
                            }
                        } else {
                            java.lang.Double.parseDouble(s2);
                        }
                        index++;
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
