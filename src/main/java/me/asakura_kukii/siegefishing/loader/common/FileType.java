package me.asakura_kukii.siegefishing.loader.common;

import me.asakura_kukii.siegefishing.loader.file.*;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public enum FileType {
    PARTICLE(new ParticleIO()),
    SOUND(new SoundIO()),
    FISH(new FishIO()),
    GUN(new GunIO()),
    HAND(new HandIO()),
    MOD(new ModIO()),
    TOOL(new ToolIO()),
    UNICODE(new UnicodeIO());


    public final HashMap<String, FileData> map;
    public final File folder;
    public final FileIO fIO;
    public final HashMap<String, Object> subMap;


    FileType(FileIO fIO) {
        this.folder = FileHandler.loadSubfolder(this.name().toLowerCase(Locale.ROOT));
        this.map = new HashMap<String, FileData>();
        this.fIO = fIO;
        this.subMap = new HashMap<String, Object>();
    }
}
