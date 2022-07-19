package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.handler.item.fish.FishData;
import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.handler.item.gun.reload.ReloadType;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.loader.method.common.MethodIO;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.*;

public class FishIO extends FileIO {

    public FishIO() {}

    public static Field getField(String s) {
        try {
            return FishData.class.getField(s);
        } catch (Exception ignored) {
        }
        return null;
    }

    public enum Map {
        DISPLAY_NAME("displayName", (String) "", FormatType.COLORED_STRING, getField("displayName"), true),
        LORE_LIST("loreList", new ArrayList<String>(), FormatType.LIST_COLORED_STRING, getField("loreList"), false),
        MATERIAL("material", (Material) Material.BRICK, FormatType.MATERIAL, getField("material"), false),
        CUSTOM_MODEL_INDEX("customModelIndex", (int) 0, FormatType.INTEGER, getField("customModelIndex"), true),
        SPAWN_TIME_MIN("spawnTimeMin", (double) 0, FormatType.DOUBLE, getField("spawnTimeMin"), true),
        SPAWN_TIME_MAX("spawnTimeMax", (double) 24, FormatType.DOUBLE, getField("spawnTimeMax"), true),
        SPAWN_TEMP_MIN("spawnTempMin", (double) 20, FormatType.DOUBLE, getField("spawnTempMin"), true),
        SPAWN_TEMP_MAX("spawnTempMax", (double) 25, FormatType.DOUBLE, getField("spawnTempMax"), true),
        DRAG_FORCE_MAX("dragForceMax", (double) 25, FormatType.DOUBLE, getField("dragForceMax"), true),
        STRUGGLE_FACTOR("struggleFactor", (double) 1, FormatType.DOUBLE, getField("struggleFactor"), true);
        //LURE_MULTIPLIER_LIST("lureMultiplierList", new HashMap<String, Double>(), FormatType.MAP_MATERIAL_DOUBLE, getField("lureMultiplierList"), true);


        public String path;
        public Object o;
        public FormatType fT;
        public Field f;
        public boolean nE;


        Map(String path, Object o, FormatType fT, Field f, Boolean nE) {
            this.path = path;
            this.o = o;
            this.fT = fT;
            this.f = f;
            this.nE = nE;
        }
    }

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier) {

        FishData fD = new FishData(identifier, fN);

        for (FishIO.Map m : FishIO.Map.values()) {
            try {
                m.f.set(fD, Format.get(fC, fN, m.path, m.o, m.fT, m.nE));
            } catch (Exception ignored) {
            }
        }

        return (FileData) fD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {

    }
}
