package me.asakura_kukii.siegefishing.config.data.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

public class ConfigData extends FileData {

    public static Integer refreshDelay = 0;
    public static double trailDistance = 0.4;
    public static double trailDensityBiasFactor = 1;
    public static String fishBoxName = "";
    public static String fishBookName = "";
    public ConfigData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
