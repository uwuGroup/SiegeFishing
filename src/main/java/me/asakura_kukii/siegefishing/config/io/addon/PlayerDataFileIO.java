package me.asakura_kukii.siegefishing.config.io.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.BaitData;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PlayerDataFileIO extends FileIO {

    public static Class<?> dataClass = PlayerData.class;
    public static Constructor<?> constructor;

    public enum Map {
        BALANCE("balance", 0.0, VerifierType.DOUBLE, true),
        ENERGY("energy", 1.0, VerifierType.DOUBLE, true),
        ACTIVE_BAIT_DATA("activeBaitData", new BaitData("", "", FileType.BAIT), VerifierType.FILE_DATA, false),
        UNLOCK_FISH_IDENTIFIER_MAP("unlockFishIdentifierMap", null, VerifierType.STRING_INTEGER_MAP, true),
        SPECIAL_FISH_IDENTIFIER_MAP("specialFishIdentifierMap", null, VerifierType.STRING_INTEGER_MAP, true),
        UNLOCK_REGION_NAME_MAP("unlockRegionNameMap", null, VerifierType.STRING_INTEGER_MAP, true),
        UNLOCK_BOAT_IDENTIFIER_LIST("unlockBoatIdentifierList", new ArrayList<>(), VerifierType.LIST_STRING, true),
        UNLOCK_ROD_IDENTIFIER_LIST("unlockRodIdentifierList", new ArrayList<>(), VerifierType.LIST_STRING, true),
        UNLOCK_FOOD_IDENTIFIER_LIST("unlockFoodIdentifierList", new ArrayList<>(), VerifierType.LIST_STRING, true),
        UNLOCK_BAIT_IDENTIFIER_LIST("unlockBaitIdentifierList", new ArrayList<>(), VerifierType.LIST_STRING, true),
        UNLOCK_ACHIEVEMENT_LIST("unlockAchievementList", new ArrayList<>(), VerifierType.LIST_STRING, true),
        GIVE_ACHIEVEMENT_LIST("giveAchievementList", new ArrayList<>(), VerifierType.LIST_STRING, true),

        FISH_BOX("fishBox", null, VerifierType.MAP_INTEGER_ITEM_STACK, true),
        BACK_ITEM_STACK_LIST("backItemStackList", null, VerifierType.MAP_INTEGER_ITEM_STACK, true),

        FISH_PERCENT("fishPercent", 0.0, VerifierType.DOUBLE, true),
        BOAT_PERCENT("boatPercent", 0.0, VerifierType.DOUBLE, true),
        FOOD_PERCENT("foodPercent", 0.0, VerifierType.DOUBLE, true),
        REGION_PERCENT("regionPercent", 0.0, VerifierType.DOUBLE, true),
        TOTAL_PERCENT("totalPercent", 0.0, VerifierType.DOUBLE, true);

        public String path;
        public Object o;
        public VerifierType vT;
        public boolean nE;

        Map(String path, Object o, VerifierType vT, Boolean nE) {
            this.path = path;
            this.o = o;
            this.vT = vT;
            this.nE = nE;
        }
    }

    public PlayerDataFileIO() {}

    static {
        try {
            constructor = dataClass.getConstructor(String.class, String.class, FileType.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileData construct(String identifier, String fN, FileType fT) {
        FileData dataObject = null;
        try {
            dataObject = (FileData) constructor.newInstance(identifier, fN, fT);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        FileData dataObject = construct(identifier, fN, fT);
        try {

            for (Map m : Map.values()) {
                if (Verifier.getField(fC, fN, m.path, dataClass) != null) {
                    Objects.requireNonNull(Verifier.getField(fC, fN, m.path, dataClass)).set(dataObject, Verifier.getObject(fC, fN, m.path, m.o, m.vT, m.nE, folder));
                }
            }
            return dataObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    @Override
    public FileConfiguration saveData(FileConfiguration fC, FileData fD) {
        try {
            for (Map m : Map.values()) {
                if (Verifier.getField(fC, fD.fileName, m.path, dataClass) != null) {
                    fC = Verifier.getString(fC, fD, m.path, Verifier.getField(fC, fD.fileName, m.path, dataClass).get(fD), m.vT);
                }
            }
            return fC;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fC;
    }
}
