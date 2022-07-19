package me.asakura_kukii.siegefishing.loader.common.format.common;

import me.asakura_kukii.siegefishing.loader.common.format.*;
import me.asakura_kukii.siegefishing.loader.common.format.Boolean;
import me.asakura_kukii.siegefishing.loader.common.format.Double;
import me.asakura_kukii.siegefishing.loader.common.format.Float;
import me.asakura_kukii.siegefishing.loader.common.format.Integer;
import me.asakura_kukii.siegefishing.loader.common.format.String;

public enum FormatType {
    STRING(new String()),
    COLORED_STRING(new ColoredString()),
    INTEGER(new Integer()),
    FLOAT(new Float()),
    DOUBLE(new Double()),
    BOOLEAN(new Boolean()),
    VECTOR(new Vector()),
    MATERIAL(new Material()),
    PARTICLE(new Particle()),
    SOUND(new Sound()),
    HAND(new Hand()),
    TYPE_RELOAD(new TypeReload()),
    TYPE_METHOD(new TypeMethod()),
    LIST_STRING(new ListString()),
    LIST_COLORED_STRING(new ListColoredString()),
    LIST_INTEGER(new ListInteger()),
    LIST_FLOAT(new ListDouble()),
    LIST_DOUBLE(new ListDouble()),
    LIST_BOOLEAN(new ListBoolean()),
    LIST_VECTOR(new ListVector()),
    LIST_MATERIAL(new ListMaterial()),
    LIST_PARTICLE(new ListParticle()),
    LIST_SOUND(new ListSound()),
    MAP_MATERIAL_MATERIAL(new MapMaterialDouble()),
    MAP_MATERIAL_DOUBLE(new MapMaterialDouble()),
    MAP_STRING_COLORED_STRING(new MapStringColoredString());
    public Format f;

    FormatType(Format f) {
        this.f = f;
    }
}
