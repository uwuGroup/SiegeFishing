package me.asakura_kukii.siegefishing.io.loader.addon;

import me.asakura_kukii.siegefishing.data.addon.FishData;
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

public class FishLoader extends Loader {

    public static Class<?> dataClass = FishData.class;
    public static Constructor<?> constructor;

    public enum Map {
        DISPLAY_NAME("displayName", (String) "", VerifierType.COLORED_STRING, true),
        LORE_LIST("loreList", new ArrayList<String>(), VerifierType.LIST_COLORED_STRING, false),
        MATERIAL("material", (Material) Material.BRICK, VerifierType.MATERIAL, false),
        CUSTOM_MODEL_INDEX("customModelIndex", (int) 0, VerifierType.INTEGER, true),
        SPAWN_TIME_MIN("spawnTimeMin", (double) 0, VerifierType.DOUBLE, true),
        SPAWN_TIME_MAX("spawnTimeMax", (double) 24, VerifierType.DOUBLE, true),
        DIFFICULTY("difficulty", (double) 25, VerifierType.DOUBLE, true),
        ANNOYING_FACTOR("annoyingFactor", (double) 1, VerifierType.DOUBLE, true),
        COLLECTOR_DESCRIPTION_LIST("collectorDescriptionList", new ArrayList<String>(), VerifierType.LIST_COLORED_STRING, false),
        POSSIBLE_CONVERSATION_LIST("possibleConversationList", new ArrayList<String>(), VerifierType.LIST_COLORED_STRING, false);

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

    public FishLoader() {}

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
