package me.asakura_kukii.siegefishing.config.io.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.RodData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

public class RodFileIO extends FileIO {

    public static Class<?> dataClass = RodData.class;
    public static Constructor<?> constructor;

    public enum Map {
        DISPLAY_NAME("displayName", (String) "", VerifierType.STRING, true),
        LORE_LIST("loreList", new ArrayList<String>(), VerifierType.LIST_STRING, false),
        MATERIAL("material", (Material) Material.BRICK, VerifierType.MATERIAL, false),
        CUSTOM_MODEL_INDEX("customModelIndex", (int) 0, VerifierType.INTEGER, true),
        AVERAGE_WAIT_TIME("avgWaitTime", (double) 600, VerifierType.DOUBLE, true),
        LUCK_BOOST("luckBoost", (double) 0.5, VerifierType.DOUBLE, true),
        MAX_PRESSURE("maxPressure", (double) 30, VerifierType.DOUBLE, true),
        MAX_SWING_DISTANCE("maxSwingDistance", (double) 7, VerifierType.DOUBLE, true),
        SWING_VELOCITY("swingVelocity", (double) 0.2, VerifierType.DOUBLE, true),
        ROD_END_BIAS_X("rodEndBiasX", (double) 0.4, VerifierType.DOUBLE, true),
        ROD_END_BIAS_Y("rodEndBiasY", (double) 0.4, VerifierType.DOUBLE, true),
        ROD_END_BIAS_Z("rodEndBiasZ", (double) 0.4, VerifierType.DOUBLE, true),

        STRING_PARTICLE_DATA("stringParticleData", new ParticleData("", "", FileType.PARTICLE), VerifierType.FILE_DATA, true),
        HOOK_PARTICLE_DATA("hookParticleData", new ParticleData("", "", FileType.PARTICLE), VerifierType.FILE_DATA, true);

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

    public RodFileIO() {}

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
