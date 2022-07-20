package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class Hand extends Format {
    public Hand() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        java.lang.String s = cS.getString(path);
        return checkHand(s, fileName, path, root, obj);
    }

    public static Object checkHand(java.lang.String s, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        if (FileType.HAND.map.containsKey(s)) {
            return (HandData) FileType.HAND.map.get(s);
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + root + path + "-" + s + " is not HandData\n");
            return obj;
        }
    }
}
