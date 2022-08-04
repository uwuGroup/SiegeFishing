package me.asakura_kukii.siegefishing.data.basic;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import org.bukkit.Chunk;

public class ConfigData extends FileData {

    public static Integer refreshDelay = 0;
    public static Integer sendPacketRadius = 32;
    public static Double entityIdPercentageMin = 0.7;
    public static Double entityIdPercentageMax = 0.8;

    public ConfigData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
