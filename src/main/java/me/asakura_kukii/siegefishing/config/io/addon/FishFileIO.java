package me.asakura_kukii.siegefishing.config.io.addon;

import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

public class FishFileIO extends FileIO {

    public static Class<?> dataClass = FishData.class;
    public static Constructor<?> constructor;

    public enum Map {
        DISPLAY_NAME("displayName", (String) "", VerifierType.STRING, true),
        LORE_LIST("loreList", new ArrayList<String>(), VerifierType.LIST_STRING, false),
        MATERIAL("material", (Material) Material.BRICK, VerifierType.MATERIAL, false),
        CUSTOM_MODEL_INDEX("customModelIndex", (int) 0, VerifierType.INTEGER, true),
        SPAWN_TIME_MIN("spawnTimeMin", (double) 0, VerifierType.DOUBLE, true),
        SPAWN_TIME_MAX("spawnTimeMax", (double) 24, VerifierType.DOUBLE, true),
        WEIGHT_MIN("weightMin", (double) 0.5, VerifierType.DOUBLE, true),
        WEIGHT_MAX("weightMax", (double) 1.0, VerifierType.DOUBLE, true),
        DIFFICULTY("difficulty", (double) 25, VerifierType.DOUBLE, true),
        ANNOYING_FACTOR("annoyingFactor", (double) 1, VerifierType.DOUBLE, true),
        COLLECTOR_DESCRIPTION_LIST("collectorDescriptionList", new ArrayList<String>(), VerifierType.LIST_STRING, false),
        POSSIBLE_CONVERSATION_LIST("possibleConversationList", new ArrayList<String>(), VerifierType.LIST_STRING, false);

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

    public FishFileIO() {}

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
