package me.asakura_kukii.siegefishing.io.loader.addon;

import me.asakura_kukii.siegefishing.data.addon.FishData;
import me.asakura_kukii.siegefishing.data.addon.FishRegionData;
import me.asakura_kukii.siegefishing.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FishRegionLoader extends Loader {

    public static Class<?> dataClass = FishRegionData.class;
    public static Constructor<?> constructor;

    public enum Map {
        REGION_VALUE("regionValue", new ImageValueData("", "", FileType.IMAGE_VALUE), VerifierType.FILE_DATA, true),
        PERCENT_MAP("percentMap", new FishData("", "", FileType.FISH), VerifierType.MAP_FILE_DATA_DOUBLE, true);

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

    public FishRegionLoader() {}

    static {
        try {
            constructor = dataClass.getConstructor(String.class, String.class, FileType.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        FileData dataObject = null;
        try {
            dataObject = (FileData) constructor.newInstance(identifier, fN, fT);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {

            for (Map m : Map.values()) {
                if (Verifier.get(fC, fN, m.path, dataClass) != null) {
                    Objects.requireNonNull(Verifier.get(fC, fN, m.path, dataClass)).set(dataObject, Verifier.get(fC, fN, m.path, m.o, m.vT, m.nE, folder));
                }
            }
            return dataObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        return null;
    }
}
