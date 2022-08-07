package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

import java.util.ArrayList;
import java.util.List;

public class ExtraStringListData extends FileData {
    public List<String> extraStringList = new ArrayList<>();

    public ExtraStringListData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>
}
