package me.asakura_kukii.siegefishing.data.basic;


import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;

public class SoundData extends FileData {

    public String sound;
    public float volumeMin;
    public float volumeMax;
    public float pitchMin;
    public float pitchMax;

    public SoundData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
