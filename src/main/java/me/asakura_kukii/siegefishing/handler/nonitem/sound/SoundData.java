package me.asakura_kukii.siegefishing.handler.nonitem.sound;

import me.asakura_kukii.siegefishing.loader.common.FileData;

public class SoundData extends FileData {

    public String sound;
    public float volumeMin;
    public float volumeMax;
    public float pitchMin;
    public float pitchMax;

    public SoundData(String identifier, String fileName) {
        this.identifier = identifier;
        this.fileName = fileName;
    }
}
