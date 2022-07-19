package me.asakura_kukii.siegefishing.loader.method;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.handler.nonitem.method.damageline.DamageLineData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.loader.method.common.MethodIO;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DamageLineIO extends MethodIO {

    public DamageLineIO() {};

    public static Field getField(String s) {
        try {
            return DamageLineData.class.getField(s);
        } catch (Exception ignored) {
        }
        return null;
    }

    public enum Map {
        DAMAGE("damage", (double) 3, FormatType.DOUBLE, getField("damage"), true),
        LENGTH("length", (double) 50, FormatType.DOUBLE, getField("length"), true),

        TRAIL_PARTICLE_LIST("trailParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("trailParticleDataList"), false),
        TRAIL_SOUND_LIST("trailSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("trailSoundDataList"), false),
        HIT_PARTICLE_LIST("trailParticleDataList", new ArrayList<ParticleData>(), FormatType.LIST_PARTICLE, getField("hitParticleDataList"), false),
        HIT_SOUND_LIST("trailSoundDataList", new ArrayList<SoundData>(), FormatType.LIST_SOUND, getField("hitSoundDataList"), false),
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

        DamageLineData dLD = new DamageLineData();

        for (Map m : Map.values()) {
            try {
                m.f.set(dLD, Format.get(cS, fN, m.path, m.o, m.fT, m.nE));
            } catch (Exception ignored) {
            }
        }

        List<Object> dataList = new ArrayList<Object>();
        dataList.add(0, dLD);
        return dataList;
    }
}
