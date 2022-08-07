package me.asakura_kukii.siegefishing.config.data.basic;


import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

public class SoundData extends FileData {

    public String sound = "default";
    public float volumeMin = (float) 0.9;
    public float volumeMax = (float) 1.0;
    public float pitchMin = (float) 0.95;
    public float pitchMax = (float) 1.05;

    public SoundData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
