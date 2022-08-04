package me.asakura_kukii.siegefishing.io.loader.common;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.addon.*;
import me.asakura_kukii.siegefishing.io.loader.basic.*;
import me.asakura_kukii.siegefishing.io.util.FileUtil;

import java.io.File;
import java.util.*;

public enum FileType {
    //SEQUENCE IS IMPORTANT!

    UNICODE("unicode", new UnicodeLoader(), false),
    IMAGE_MAP("imageMap", new ImageMapLoader(), false),
    IMAGE_VALUE("imageValue", new ImageValueLoader(), false),
    FISH("fish", new FishLoader(), true),
    BASIC_ITEM("basicItem", new BasicItemLoader(), true),
    PARTICLE("particle", new ParticleLoader(), false),
    SOUND("sound", new SoundLoader(), false),
    CONFIG("config", new ConfigLoader(), false),

    FISH_REGION("fishRegion", new FishRegionLoader(), false),
    FISH_SESSION("fishSession", new FishSessionLoader(), false);

    public final HashMap<String, FileData> map;
    public final File folder;
    public final Loader fIO;
    public final HashMap<String, Object> subMap;
    public final boolean linkItem;
    public final String typeName;

    FileType(String typeName, Loader fIO, boolean linkItem) {
        this.folder = FileUtil.loadSubfolder(typeName);
        this.map = new HashMap<String, FileData>();
        this.fIO = fIO;
        this.subMap = new HashMap<String, Object>();
        this.linkItem = linkItem;
        this.typeName = typeName;
    }

    public static List<FileType> getFileTypeList() {
        return new ArrayList<>(Arrays.asList(FileType.values()));
    }

    public static List<String> getFileTypeNameList() {
        List<String> fileTypeNameList = new ArrayList<>();
        for (FileType fT : FileType.values()) {
            fileTypeNameList.add(fT.typeName);
        }
        return fileTypeNameList;
    }

    public static List<String> getItemLinkedFileTypeNameList() {
        List<String> fileTypeNameList = new ArrayList<>();
        for (FileType fT : getItemLinkedFileTypeList()) {
            if (fT.linkItem) {
                fileTypeNameList.add(fT.typeName);
            }
        }
        return fileTypeNameList;
    }

    public static List<FileType> getItemLinkedFileTypeList() {
        List<FileType> ItemLinkedFileTypeList = new ArrayList<>();
        for (FileType fT : FileType.values()) {
            if (fT.linkItem) {
                ItemLinkedFileTypeList.add(fT);
            }
        }
        return ItemLinkedFileTypeList;
    }

    public static FileType getItemLinkedFileTypeFromName(String typeName) {
        for (FileType fT : FileType.values()) {
            if (fT.typeName.equalsIgnoreCase(typeName) && fT.linkItem) {
                return fT;
            }
        }
        return null;
    }

    public static FileType getFileTypeFromName(String typeName) {
        for (FileType fT : FileType.values()) {
            if (fT.typeName.equalsIgnoreCase(typeName)) {
                return fT;
            }
        }
        return null;
    }

    public static FileData forceGetDataFromMap(String identifier, FileType fT) {
        return fT.map.getOrDefault(identifier, null);
    }

    public static List<FileData> getSortedFileDataList(FileType fT) {
        Collection<FileData> fileDataList = fT.map.values();
        FileData[] fileDataArray = fileDataList.toArray(new FileData[0]);
        Arrays.sort(fileDataArray, new Comparator<FileData>() {
            @Override
            public int compare(FileData o1, FileData o2) {
                return o1.identifier.compareTo(o2.identifier);
            }
        });
        return Arrays.asList(fileDataArray);
    }
}
