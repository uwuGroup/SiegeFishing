package me.asakura_kukii.siegefishing.io.loader.basic;

import me.asakura_kukii.siegefishing.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class ConfigLoader extends Loader {

    public static Class<?> dataClass = ConfigData.class;
    public static Constructor<?> constructor;

    public enum Map {
        REFRESH_DELAY("refreshDelay", (int) 0, VerifierType.INTEGER, true),
        SEND_PACKET_RADIUS("sendPacketRadius", (int) 32, VerifierType.INTEGER, true),
        ENTITY_ID_PERCENTAGE_MIN("entityIdPercentageMin", 0.7, VerifierType.DOUBLE, true),
        ENTITY_ID_PERCENTAGE_MAX("entityIdPercentageMax", 0.8, VerifierType.DOUBLE, true);

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

    public ConfigLoader() {}

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
