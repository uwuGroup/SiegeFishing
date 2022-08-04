package me.asakura_kukii.siegefishing.io.loader.basic;

import me.asakura_kukii.siegefishing.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.data.basic.SoundData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class ParticleLoader extends Loader {

    public static Class<?> dataClass = ParticleData.class;
    public static Constructor<?> constructor;

    public enum Map {
        PARTICLE("particle", Particle.BLOCK_CRACK, VerifierType.PARTICLE, true),
        COUNT("count", (int) 1, VerifierType.INTEGER, true),
        EXTRA("extra", (float) 1, VerifierType.FLOAT, true),
        SIZE_X("sizeX", (float) 0, VerifierType.FLOAT, true),
        SIZE_Y("sizeY", (float) 0, VerifierType.FLOAT, true),
        SIZE_Z("sizeZ", (float) 0, VerifierType.FLOAT, true),
        COLOR_R("colorR", (int) 0, VerifierType.INTEGER, true),
        COLOR_G("colorG", (int) 0, VerifierType.INTEGER, true),
        COLOR_B("colorB", (int) 0, VerifierType.INTEGER, true),
        BIAS_X("biasX", (double) 0, VerifierType.DOUBLE, true),
        BIAS_Y("biasY", (double) 0, VerifierType.DOUBLE, true),
        BIAS_Z("biasZ", (double) 0, VerifierType.DOUBLE, true),
        MATERIAL("material", Material.GRASS_BLOCK, VerifierType.MATERIAL, true);

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

    public ParticleLoader() {}

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
