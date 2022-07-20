package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.CommonFileData;
import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.utility.colorcode.ColorCode;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

public class UnicodeIO extends FileIO {

    public UnicodeIO() {}

    public enum Map {
        UNICODE_MAP_LIST("unicodeMapList", new HashMap<String, String>(), FormatType.MAP_STRING_COLORED_STRING, null, false);

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
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        return new CommonFileData(identifier, fN);
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {

        HashMap<String, Object> subMap = new HashMap<>();

        for (UnicodeIO.Map m : UnicodeIO.Map.values()) {
            try {
                HashMap<String, String> tempMap = (HashMap<String, String>) Format.get(fC, fN, m.path, m.o, m.fT, m.nE, folder);
                for (String key : tempMap.keySet()) {
                    subMap.put(key, tempMap.get(key));
                }
            } catch (Exception ignored) {
            }
        }

        SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + subMap.toString());

        return subMap;
    }

    @Override
    public void loadDefault(FileType fT) {

    }
}
