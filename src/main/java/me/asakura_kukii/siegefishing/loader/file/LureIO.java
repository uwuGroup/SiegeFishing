package me.asakura_kukii.siegefishing.loader.file;

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

public class LureIO extends FileIO {

    public LureIO() {}

    public static Field getField(String s) {
        try {
            return GunData.class.getField(s);
        } catch (Exception ignored) {
        }
        return null;
    }

    public enum Map {
        DISPLAY_NAME("displayName", (String) "", FormatType.COLORED_STRING, getField("displayName"), true),
        LORE_LIST("loreList", new ArrayList<String>(), FormatType.LIST_COLORED_STRING, getField("loreList"), false),
        MATERIAL("material", (Material) Material.CROSSBOW, FormatType.MATERIAL, getField("material"), false),
        CUSTOM_MODEL_INDEX("customModelIndex", (int) 0, FormatType.INTEGER, getField("customModelIndex"), true),
        EFFECT_TIME("effectTime", (int) 0, FormatType.INTEGER, getField("effectTime"), true);



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

        GunData gD = new GunData(identifier, fN);

        for (GunIO.Map m : GunIO.Map.values()) {
            try {
                m.f.set(gD, Format.get(fC, fN, m.path, m.o, m.fT, m.nE));
            } catch (Exception ignored) {
            }
        }

        gD.mND = MethodIO.loadTree(fC, fN);

        return (FileData) gD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {

    }
}
