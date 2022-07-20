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

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class GunIO extends FileIO {

    public GunIO() {}

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
        MUZZLE_NORMAL_BIAS("muzzleNormalBias", new Vector3D(0,0,0), FormatType.VECTOR, getField("muzzleNormalBias"), false),
        MUZZLE_SCOPE_BIAS("muzzleScopeBias", new Vector3D(0,0,0), FormatType.VECTOR, getField("muzzleScopeBias"), false),
        SOUND_LIST("soundList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("soundList"), false),
        MUZZLE_PARTICLE_LIST("muzzleParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("muzzleParticleDataList"), false),
        MUZZLE_PARTICLE_DURATION("muzzleParticleDuration", (int) 4, FormatType.INTEGER, getField("muzzleParticleDuration"), false),

        BULLET_COUNT("bulletCount", (int) 30, FormatType.INTEGER, getField("bulletCount"), false),
        BULLET_COUNT_PER_SHOT("bulletCountPerShot", (int) 1, FormatType.INTEGER, getField("bulletCountPerShot"), false),
        BULLET_SPREAD("bulletSpread", (double) 0, FormatType.DOUBLE, getField("bulletSpread"), false),
        SEMI_AUTO_FIRE_DELAY("semiAutoFireDelay", (double) 0.1, FormatType.DOUBLE, getField("semiAutoFireDelay"), false),
        ACCURATE_TIME_1("accurateTime1", (double) 0.3, FormatType.DOUBLE, getField("accurateTime1"), false),
        ACCURATE_TIME_2("accurateTime2", (double) 0.5, FormatType.DOUBLE, getField("accurateTime2"), false),
        INACCURACY_0("inaccuracy0", (double) 2, FormatType.DOUBLE, getField("inaccuracy0"), false),
        INACCURACY_1("inaccuracy1", (double) 1, FormatType.DOUBLE, getField("inaccuracy1"), false),
        STEP_BACK("stepBack", (boolean) true, FormatType.BOOLEAN, getField("stepBack"), false),
        STEP_RETURN("stepReturn", (boolean) true, FormatType.BOOLEAN, getField("stepReturn"), false),
        STEP_BACK_FACTOR("stepBackFactor", (double) 0.06, FormatType.DOUBLE, getField("stepBackFactor"), false),
        STEP_RETURN_FACTOR("stepReturnFactor", (double) 0.6, FormatType.DOUBLE, getField("stepReturnFactor"), false),
        FULL_AUTO("fullAuto", (boolean) true, FormatType.BOOLEAN, getField("fullAuto"), false),
        FULL_AUTO_FIRE_DELAY("fullAutoFireDelay", (double) 0.1, FormatType.DOUBLE, getField("fullAutoFireDelay"), false),
        INACCURACY_STEP("inaccuracyStep", (double) 0.05, FormatType.DOUBLE, getField("inaccuracyStep"), false),
        INACCURACY_MAX("inaccuracyMax", (double) 1, FormatType.DOUBLE, getField("inaccuracyMax"), false),
        RECOIL_CURVE("recoilCurve", new ArrayList<Vector3D>(), FormatType.LIST_VECTOR, getField("recoilCurve"), true),

        MOD_SLOT_COUNT("modSlotCount", (int) 3, FormatType.INTEGER, getField("modSlotCount"), false),
        SCOPE_POTION_EFFECT("scopePotionEffect", (boolean) true, FormatType.BOOLEAN, getField("scopePotionEffect"), false),
        SCOPE_ZOOM_LEVEL("scopeZoomLevel", (int) 0, FormatType.INTEGER, getField("scopeZoomLevel"), false),
        SCOPE_NIGHT_VISION("scopeNightVision", (boolean) false, FormatType.BOOLEAN, getField("scopeNightVision"), false),
        SCOPE_SPEED_COMPENSATION("scopeSpeedCompensation", (double) 3, FormatType.DOUBLE, getField("scopeSpeedCompensation"), false),
        RELOAD_TYPE("reloadingHandler", ReloadType.CLIP, FormatType.TYPE_RELOAD, getField("rT"), false),
        RELOAD_DELAY("reloadDelay", (double) 3.2, FormatType.DOUBLE, getField("reloadDelay"), false);

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
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {

        GunData gD = new GunData(identifier, fN);

        for (GunIO.Map m : GunIO.Map.values()) {
            try {
                m.f.set(gD, Format.get(fC, fN, m.path, m.o, m.fT, m.nE, folder));
            } catch (Exception ignored) {
            }
        }

        gD.mND = MethodIO.loadTree(fC, fN);

        return (FileData) gD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {

    }
}
