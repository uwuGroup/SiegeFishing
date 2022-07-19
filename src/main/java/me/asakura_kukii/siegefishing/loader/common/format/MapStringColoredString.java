package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class MapStringColoredString extends Format {

    public MapStringColoredString() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        HashMap<java.lang.String, java.lang.String> mSS = new HashMap<>();
        for (java.lang.String s : sL) {
            if (checkStringColoredStringMap(s)) {
                int index = 0;
                java.lang.String key = "",value = "";
                for (java.lang.String s2 : s.split("\\^")) {
                    if (index == 0) {
                        key = s2;
                    } else {
                        value = ChatColor.translateAlternateColorCodes('&', s2);
                    }
                    index++;
                }
                mSS.put(key, value);
            } else {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not StringColoredStringMap\n");
                return obj;
            }
        }

        return mSS;
    }

    public static boolean checkStringColoredStringMap(java.lang.String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==2) {
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
