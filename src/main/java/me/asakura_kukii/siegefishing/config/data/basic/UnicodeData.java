package me.asakura_kukii.siegefishing.config.data.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

import java.util.HashMap;

public class UnicodeData extends FileData {

    public HashMap<String, String> map = new HashMap<>();
    public HashMap<String, Integer> pictureHeightMap = new HashMap<>();

    public UnicodeData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
