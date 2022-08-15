package me.asakura_kukii.siegefishing.config.data;

import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.IOOption;
import me.asakura_kukii.siegefishing.config.io.addon.*;
import me.asakura_kukii.siegefishing.config.io.basic.*;
import me.asakura_kukii.siegefishing.utility.file.FileUtil;

import java.io.File;
import java.util.*;

public enum FileType {
    //SEQUENCE IS IMPORTANT!

    UNICODE(            "unicode",              new UnicodeFileIO(),            false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    EXTRA_STRING_LIST(  "extraStringList",      new ExtraStringListFileIO(),    false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    IMAGE_MAP(          "imageMap",             new ImageMapFileIO(),           false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    IMAGE_VALUE(        "imageValue",           new ImageValueFileIO(),         false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    BASIC_ITEM(         "basicItem",            new BasicItemFileIO(),          true,   IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    PARTICLE(           "particle",             new ParticleFileIO(),           false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    SOUND(              "sound",                new SoundFileIO(),              false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    POTION(             "potion",               new PotionFileIO(),             false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),

    FISH(               "fish",                 new FishFileIO(),               true,   IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    ROD(                "rod",                  new RodFileIO(),                true,   IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    SHOVEL("shovel", new ShovelFileIO(), true, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    BOAT("boat", new BoatFileIO(), true, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    FOOD("food", new FoodFileIO(), true, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    BAIT("bait", new BaitFileIO(), true, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    AWARD("award", new AwardFileIO(), true, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),

    CONFIG(             "config",               new ConfigFileIO(),             false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),

    ENTITY_COLLECTOR_DATA_FILE("collector", new EntityCollectorFileIO(), false, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    ENTITY_SHOP_DATA_FILE("shop", new EntityShopDataFileIO(), false, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    ENTITY_CONTAINER_DATA_FILE("container", new EntityContainerFileIO(), false, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),
    ENTITY_MARKET_DATA_FILE("market", new EntityMarketDataFileIO(), false, IOOption.LOAD_ALL_ON_START, IOOption.NO_SAVE),

    PLAYER_DATA(        "playerData",           new PlayerDataFileIO(),         false,  IOOption.LOAD_ON_DEMAND,        IOOption.SAVE_ALL_ON_STOP),
    FISH_REGION(        "fishRegion",           new FishRegionFileIO(),         false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    CRAB_REGION(        "crabRegion",           new CrabRegionFileIO(),         false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE),
    FISH_SESSION(       "fishSession",          new FishSessionFileIO(),        false,  IOOption.LOAD_ALL_ON_START,     IOOption.NO_SAVE);

    public final HashMap<String, FileData> map;
    public final File folder;
    public final FileIO fIO;
    public final boolean linkItem;
    public final String typeName;
    public final IOOption loadOption;
    public final IOOption saveOption;

    FileType(String typeName, FileIO fIO, boolean linkItem, IOOption loadOption, IOOption saveOption) {
        this.folder = FileUtil.loadSubfolder(typeName);
        this.map = new HashMap<String, FileData>();
        this.fIO = fIO;
        this.linkItem = linkItem;
        this.typeName = typeName;
        this.loadOption = loadOption;
        this.saveOption = saveOption;
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
