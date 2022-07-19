package me.asakura_kukii.siegefishing.loader.method;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.handler.nonitem.method.particle.CircularParticleData;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.loader.method.common.MethodIO;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CircularParticleIO extends MethodIO {

    public CircularParticleIO() {};

    public static Field getField(String s) {
        try {
            return CircularParticleData.class.getField(s);
        } catch (Exception ignored) {
        }
        return null;
    }

    public enum Map {
        RADIUS("radius", (double) 5, FormatType.DOUBLE, getField("radius"), true),
        SAMPLE("sample", (int) 5, FormatType.INTEGER, getField("sample"), true),
        PARTICLE_LIST("particleDataList", new ArrayList<>(), FormatType.LIST_PARTICLE, getField("particleDataList"), true),
        CIRCLE_AMOUNT("circleAmount", (boolean) true, FormatType.INTEGER, getField("circleAmount"), true),

        ORTHOGONAL("orthogonal", (boolean) true, FormatType.BOOLEAN, getField("orthogonal"), true),
        CIRCLE_AXIS("circleAxis", (boolean) true, FormatType.BOOLEAN, getField("circleAxis"), true),
        CIRCLE_X("circleX", (boolean) true, FormatType.BOOLEAN, getField("circleX"), true),
        CIRCLE_Z("circleZ", (boolean) true, FormatType.BOOLEAN, getField("circleZ"), true),
        ALIGN_AXIS_TO_WORLD("alignAxisToWorld", (boolean) true, FormatType.BOOLEAN, getField("alignAxisToWorld"), true),
        ALIGN_AXIS_TO_DIRECTION("alignAxisToDirection", (boolean) true, FormatType.BOOLEAN, getField("alignAxisToDirection"), true);

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

        CircularParticleData cPD = new CircularParticleData();

        for (Map m : Map.values()) {
            try {
                m.f.set(cPD, Format.get(cS, fN, m.path, m.o, m.fT, m.nE));
            } catch (Exception ignored) {
            }
        }

        List<Object> dataList = new ArrayList<Object>();
        dataList.add(0, cPD);
        return dataList;
    }
}
