package me.asakura_kukii.siegefishing.config.io.verifier;

import me.asakura_kukii.siegefishing.config.io.verifier.addon.*;
import me.asakura_kukii.siegefishing.config.io.verifier.basic.*;

public enum VerifierType {
    STRING(new VerifyString()),
    LIST_STRING(new VerifyStringList()),

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

    ITEM_STACK(new VerifyItemStack()),
    LIST_ITEM_STACK(new VerifyItemStackList()),

    PARTICLE(new VerifyParticle()),

    IMAGE(new VerifyImage()),

    CHUNK(new VerifyChunk()),

    WORLD(new VerifyWorld()),

    MAP_VECTOR_STRING(new VerifyVector2StringMap()),

    FILE_DATA(new VerifyFileData()),

    MAP_FILE_DATA_DOUBLE(new VerifyFileData2DoubleMap()),

    STRING_COLORED_STRING_MAP(new VerifyString2StringMap()),

    STRING_INTEGER_MAP(new VerifyString2IntegerMap()),
    ;

    public Verifier f;

    VerifierType(Verifier f) {
        this.f = f;
    }
}
