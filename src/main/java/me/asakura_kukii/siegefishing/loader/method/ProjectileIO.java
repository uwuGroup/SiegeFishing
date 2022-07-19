package me.asakura_kukii.siegefishing.loader.method;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.handler.nonitem.method.projectile.ProjectileData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.loader.method.common.MethodIO;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProjectileIO extends MethodIO {

    public ProjectileIO() {};

    public static Field getField(String s) {
        try {
            return ProjectileData.class.getField(s);
        } catch (Exception ignored) {
        }
        return null;
    }

    public enum Map {
        VEL("vel", (double) 3, FormatType.DOUBLE, getField("vel"), true),
        MAX_VEL("maxVel", (double) 5, FormatType.DOUBLE, getField("maxVel"), true),
        ACC("acc", new Vector3D(0, -0.1, 0), FormatType.VECTOR, getField("acc"), true),
        MAX_BOUNCE("maxBounce", (int) 20, FormatType.INTEGER, getField("maxBounce"), true),
        BOUNCE_FACTOR("bounceFactor", (double) 0.3, FormatType.DOUBLE, getField("bounceFactor"), true),
        BOUNCE_FRICTION("bounceFriction", (double) 0.3, FormatType.DOUBLE, getField("bounceFriction"), true),
        STICKY("sticky", (boolean) false, FormatType.BOOLEAN, getField("sticky"), true),
        ATTACHABLE("attachable", (boolean) false, FormatType.BOOLEAN, getField("attachable"), true),
        MAX_LIFE("maxLife", (Integer) 200, FormatType.INTEGER, getField("maxLife"), true),
        INTERVAL("interval", (Integer) 20, FormatType.INTEGER, getField("interval"), true),
        INTERVAL_START("intervalStart", (Integer) 20, FormatType.INTEGER, getField("intervalStart"), false),
        INTERVAL_STOP("intervalStop", (Integer) 200, FormatType.INTEGER, getField("intervalStop"), false),

        TRAIL_PARTICLE_LIST("trailParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("trailParticleDataList"), false),
        TRAIL_SOUND_LIST("trailSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("trailSoundDataList"), false),
        BOUNCE_PARTICLE_LIST("bounceParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("bounceParticleDataList"), false),
        BOUNCE_SOUND_LIST("bounceSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("bounceSoundDataList"), false),
        INTERVAL_PARTICLE_LIST("intervalParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("intervalParticleDataList"), false),
        INTERVAL_SOUND_LIST("intervalSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("intervalSoundDataList"), false),
        VANISH_PARTICLE_LIST("vanishParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("vanishParticleDataList"), false),
        VANISH_SOUND_LIST("vanishSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("vanishSoundDataList"), false);

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
    public List<Object> loadData(ConfigurationSection cS, String fN, MethodType mT) {

        ProjectileData pD = new ProjectileData();

        for (Map m : Map.values()) {
            try {
                m.f.set(pD, Format.get(cS, fN, m.path, m.o, m.fT, m.nE));
            } catch (Exception ignored) {
            }
        }

        List<Object> dataList = new ArrayList<Object>();
        dataList.add(0, pD);
        return dataList;
    }
}
