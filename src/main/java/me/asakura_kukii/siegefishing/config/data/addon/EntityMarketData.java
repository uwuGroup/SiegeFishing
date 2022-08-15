package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityMarketData extends FileData {

    public String tag = "";
    public String fishShopName = "";
    public HashMap<ItemData, Double> itemDataPriceMap = new HashMap<>();

    public EntityMarketData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
