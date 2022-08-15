package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityShopData extends FileData {

    public String tag = "";
    public String fishShopName = "";
    public HashMap<Integer, Double> levelPriceMap = new HashMap<>();

    public EntityShopData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
