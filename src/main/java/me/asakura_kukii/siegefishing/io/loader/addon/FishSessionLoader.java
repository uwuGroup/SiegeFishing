package me.asakura_kukii.siegefishing.io.loader.addon;

import me.asakura_kukii.siegefishing.data.addon.FishData;
import me.asakura_kukii.siegefishing.data.addon.FishSessionData;
import me.asakura_kukii.siegefishing.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class FishSessionLoader extends Loader {

    public static Class<?> dataClass = FishSessionData.class;
    public static Constructor<?> constructor;

    public enum Map {
        WORLD("world", null, VerifierType.WORLD, true),
        MIDNIGHT_GAME_TICK("midnightGameTick", 17000, VerifierType.INTEGER, true),
        TEMPERATURE_BIAS_NOON("temperatureBiasNoon", (double) 0, VerifierType.DOUBLE, true),
        TEMPERATURE_BIAS_MIDNIGHT("temperatureBiasMidnight", (double) -10, VerifierType.DOUBLE, true),
        TEMPERATURE_VALUE("temperatureValue", new ImageValueData("", "", FileType.IMAGE_VALUE), VerifierType.FILE_DATA, true),
        REGION_NAME_MAP("regionNameMap", new ImageMapData("", "", FileType.IMAGE_MAP), VerifierType.FILE_DATA, true);

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

    public FishSessionLoader() {}

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
