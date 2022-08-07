package me.asakura_kukii.siegefishing.config.io.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
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
import java.util.Objects;

public class PlayerDataFileIO extends FileIO {

    public static Class<?> dataClass = PlayerData.class;
    public static Constructor<?> constructor;

    public enum Map {
        UNLOCK_FISH_LIST("unlockFishList", new ArrayList<ItemStack>(), VerifierType.LIST_ITEM_STACK, true),
        FAVOURED_FISH_LIST("favouredFishList", new ArrayList<ItemStack>(), VerifierType.LIST_ITEM_STACK, true),
        UNLOCK_FISH_IDENTIFIER_LIST("unlockFishIdentifierList", new ArrayList<String>(), VerifierType.LIST_STRING, true),
        UNLOCK_REGION_NAME_LIST("unlockRegionNameList", new ArrayList<String>(), VerifierType.LIST_STRING, true);

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
