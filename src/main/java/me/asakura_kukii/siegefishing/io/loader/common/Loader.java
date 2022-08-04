package me.asakura_kukii.siegefishing.io.loader.common;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.util.FileUtil;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.common.VerifierType;
import me.asakura_kukii.siegefishing.utility.format.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;
import static me.asakura_kukii.siegefishing.SiegeFishing.server;

public abstract class Loader {
    public static HashMap<String, Boolean> fileStatusMapper = new HashMap<>();
    public static HashMap<String, String> fileMessageMapper = new HashMap<>();
    public static List<String> invalidFileNameList = new ArrayList<>();

    public static void loadAll() {
        fileStatusMapper.clear();
        fileMessageMapper.clear();
        invalidFileNameList.clear();

        for(FileType fT : FileType.getFileTypeList()) {
            fT.map.clear();
            fT.subMap.clear();
            if (fT.folder.exists()) {
                loadDefaultFile(fT);
                loadFileTreeRecursively(fT.folder, fT);
                server.getConsoleSender().sendMessage(Color.ANSI_GREEN + consolePluginPrefix + Color.ANSI_WHITE + "Loaded " + fT.map.keySet().size() + " " + fT.typeName);
            } else {
                FileUtil.loadSubfolder(fT.folder.getName());
            }
        }
    }

    public static void loadFileTreeRecursively(File folder, FileType fT) {
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.getName().contains("yml")) {
                FileConfiguration fC = YamlConfiguration.loadConfiguration(f);
                fileStatusMapper.put(folder.getName() + "." + f.getName(), true);
                loadFile(fC, folder.getName() + "." + f.getName(), fT, fT.folder);
            } else if (f.isDirectory()){
                loadFileTreeRecursively(f, fT);
            }
        }
    }

    public static void loadFile(FileConfiguration fC, String fN, FileType fT, File folder) {
        fileMessageMapper.put(fN, "");
        String identifier = "";
        identifier = (String) Verifier.get(fC, fN, "identifier", identifier, VerifierType.STRING, true, folder);
        if (fT.map.containsKey(identifier)) {
            fileStatusMapper.put(fN, false);
            fileMessageMapper.put(fN, fileMessageMapper.get(fN) + consolePluginPrefix + "identifier [" + identifier + "] is used\n");
        }
        if (fileStatusMapper.get(fN)) {
            fileMessageMapper.put(fN, fileMessageMapper.get(fN) + consolePluginPrefix + "Loading file [" + fN + "]\n");
            //goto specific loader
            FileData fD = fT.fIO.loadData(fC, fN, fT, identifier, folder);
            HashMap<String, Object> subMap = fT.fIO.loadSubData(fC, fN, fT, identifier, folder);
            if (fD != null) {
                fD.fT = fT;
            }
            if (fileStatusMapper.get(fN)) {
                if (fD != null) {
                    fT.map.put(identifier,fD);
                }
                if (subMap != null) {
                    fT.subMap.putAll(subMap);
                }

                server.getConsoleSender().sendMessage(Color.ANSI_GREEN + consolePluginPrefix + Color.ANSI_WHITE + "Loaded " + fT.name().toLowerCase(Locale.ROOT) + " [" + identifier + "]");
            } else {
                invalidFileNameList.add(fN);
                server.getConsoleSender().sendMessage(Color.ANSI_GREEN + consolePluginPrefix + Color.ANSI_RED + "Failed when loading " + fT.name().toLowerCase(Locale.ROOT) + " [" + identifier + "]\n\n" + fileMessageMapper.get(fN) + Color.ANSI_WHITE);
            }
        }
    }

    public static void loadDefaultFile(FileType fT) {
        try {

            FileUtil.copyJarResourcesRecursively(fT.folder, "default/" + fT.typeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putError(String fileName) {
        Loader.fileStatusMapper.put(fileName, false);
    }

    public static void addErrorMsg(String fileName, String root, String path, String msg) {
        String cacheMsg = "";
        if  (Loader.fileMessageMapper.containsKey(fileName)) {
            cacheMsg = Loader.fileMessageMapper.get(fileName);
        }
        Loader.fileMessageMapper.remove(fileName);
        Loader.fileMessageMapper.put(fileName, cacheMsg + consolePluginPrefix + root + path + " " + msg + "\n");
    }

    public abstract FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder);

    public abstract HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder);
}
