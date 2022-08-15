package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

import java.util.HashMap;

public class EntityCollectorData extends FileData {

    public String tag = "";

    public EntityCollectorData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
