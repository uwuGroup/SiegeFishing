package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class SoundIO extends FileIO{

    public SoundIO() {}

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        SoundData sD = new SoundData(identifier, fN);

        String sound = "";
        float vMin = 0;
        float vMax = 1;
        float pMin = 0;
        float pMax = 1;

        //reading...
        sD.sound = (String) FormatHandler.checkConfigurationFormat(fC, fN, "sound", sound, true);
        sD.volumeMin = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "volumeMin", vMin, true);
        sD.volumeMax = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "volumeMax", vMax, true);
        sD.pitchMin = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "pitchMin", pMin, true);
        sD.pitchMax = (Float) FormatHandler.checkConfigurationFormat(fC, fN, "pitchMax", pMax, true);

        return sD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {
        SoundData sD = new SoundData("default_headshot", "");

        sD.sound = Sound.ITEM_SHIELD_BREAK.getKey().toString();
        sD.volumeMin = (float) 0.7;
        sD.volumeMax = (float) 0.7;
        sD.pitchMin = (float) 1.9;
        sD.pitchMax = 2;

        fT.map.put(sD.identifier, (FileData) sD);
    }
}
