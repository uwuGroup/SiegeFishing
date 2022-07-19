package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class MapMaterialMaterial extends Format {

    public MapMaterialMaterial() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        HashMap<Material, Material> mMM = new HashMap<>();
        for (java.lang.String s : sL) {
            if (checkMaterialMaterialMap(s)) {
                int index = 0;
                Material m1 = Material.ANDESITE;
                Material m2 = Material.ANDESITE;
                for (java.lang.String s2 : s.split("\\^")) {
                    if (index == 0) {
                        m1 = Material.matchMaterial(s2);
                    } else {
                        m2 = Material.matchMaterial(s2);
                    }
                    index++;
                }
                mMM.put(m1, m2);
            } else {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not MaterialMaterialMap\n");
                return obj;
            }
        }
        
        return mMM;
    }

    public static boolean checkMaterialMaterialMap(java.lang.String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==2) {
                for (java.lang.String s2 : s.split("\\^")) {
                    try {
                        if (Material.matchMaterial(s2) == null) {
                            formatCorrect = false;
                        }
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
