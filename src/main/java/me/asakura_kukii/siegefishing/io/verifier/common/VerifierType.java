package me.asakura_kukii.siegefishing.io.verifier.common;

import me.asakura_kukii.siegefishing.io.verifier.addon.*;
import me.asakura_kukii.siegefishing.io.verifier.basic.*;

public enum VerifierType {
    STRING(new VerifyString()),
    LIST_STRING(new VerifyStringList()),

    COLORED_STRING(new VerifyColoredString()),
    LIST_COLORED_STRING(new VerifyColoredStringList()),

    BOOLEAN(new VerifyBoolean()),
    LIST_BOOLEAN(new VerifyBooleanList()),

    INTEGER(new VerifyInteger()),
    LIST_INTEGER(new VerifyIntegerList()),

    FLOAT(new VerifyFloat()),
    LIST_FLOAT(new VerifyFloat()),

    DOUBLE(new VerifyDouble()),
    LIST_DOUBLE(new VerifyDoubleList()),

    VECTOR(new VerifyVector()),
    LIST_VECTOR(new VerifyVectorList()),

    MATERIAL(new VerifyMaterial()),
    LIST_MATERIAL(new VerifyMaterialList()),

    PARTICLE(new VerifyParticle()),

    IMAGE(new VerifyImage()),

    CHUNK(new VerifyChunk()),

    WORLD(new VerifyWorld()),

    MAP_VECTOR_STRING(new VerifyVector2StringMap()),

    FILE_DATA(new VerifyFileData()),

    MAP_FILE_DATA_DOUBLE(new VerifyFileData2DoubleMap()),

    STRING_COLORED_STRING_MAP(new VerifyString2ColoredStringMap())
    ;

    public Verifier f;

    VerifierType(Verifier f) {
        this.f = f;
    }
}
