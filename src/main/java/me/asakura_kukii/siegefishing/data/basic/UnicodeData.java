package me.asakura_kukii.siegefishing.data.basic;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;

import java.util.HashMap;

public class UnicodeData extends FileData {
    public HashMap<String, String> map;

    public UnicodeData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
