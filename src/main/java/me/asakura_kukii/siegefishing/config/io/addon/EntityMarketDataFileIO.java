package me.asakura_kukii.siegefishing.config.io.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.EntityMarketData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityShopData;
import me.asakura_kukii.siegefishing.config.data.addon.ExtraStringListData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class EntityMarketDataFileIO extends FileIO {

    public static Class<?> dataClass = EntityMarketData.class;
    public static Constructor<?> constructor;

    public enum Map {
        TAG("tag", "", VerifierType.STRING, true),
        FISH_SHOP_NAME("fishShopName", "", VerifierType.STRING, true),
        LEVEL_PRICE_MAP("itemDataPriceMap", null, VerifierType.MAP_LOOSE_FILE_DATA_DOUBLE, true);

        public String path;
        public Object o;
        public VerifierType vT;
        public boolean nE;

        Map(String path, Object o, VerifierType vT, Boolean nE) {
            this.path = path;
            this.o = o;
            this.vT = vT;
            this.nE = nE;
        }
    }

    public EntityMarketDataFileIO() {}

    static {
        try {
            constructor = dataClass.getConstructor(String.class, String.class, FileType.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileData construct(String identifier, String fN, FileType fT) {
        FileData dataObject = null;
        try {
            dataObject = (FileData) constructor.newInstance(identifier, fN, fT);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        FileData dataObject = construct(identifier, fN, fT);
        try {

            for (Map m : Map.values()) {
                if (Verifier.getField(fC, fN, m.path, dataClass) != null) {
                    Objects.requireNonNull(Verifier.getField(fC, fN, m.path, dataClass)).set(dataObject, Verifier.getObject(fC, fN, m.path, m.o, m.vT, m.nE, folder));
                }
            }
            return dataObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    @Override
    public FileConfiguration saveData(FileConfiguration fC, FileData fD) {
        try {
            for (Map m : Map.values()) {
                if (Verifier.getField(fC, fD.fileName, m.path, dataClass) != null) {
                    fC = Verifier.getString(fC, fD, m.path, Verifier.getField(fC, fD.fileName, m.path, dataClass).get(fD), m.vT);
                }
            }
            return fC;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fC;
    }
}
