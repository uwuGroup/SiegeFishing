package me.asakura_kukii.siegefishing.config.io;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.utility.file.FileUtil;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.net.TcpSocketManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;
import static me.asakura_kukii.siegefishing.SiegeFishing.server;

public abstract class FileIO {
    public static HashMap<String, Boolean> fileStatusMapper = new HashMap<>();
    public static HashMap<String, String> fileMessageMapper = new HashMap<>();
    public static List<String> invalidFileNameList = new ArrayList<>();

    public static void unloadOnDemand(FileData fD) {
        fD.fT.map.remove(fD.identifier);
        fileStatusMapper.remove(fD.fileName);
        fileMessageMapper.remove(fD.fileName);
        invalidFileNameList.remove(fD.fileName);
    }

    public static FileData loadOnDemand(String identifier, String fN, FileType fT) {
        Bukkit.getLogger().info("loading");
        File f = new File(fT.folder, fN.replaceAll(fT.typeName + ".", ""));
        if (fT.map.containsKey(identifier)) {
            if (!f.exists()) {
                saveFile(fT.map.get(identifier), true);
            }
            return fT.map.get(identifier);
        } else {
            FileData fD;
            if (f.exists()) {
                loadFile(f, fN, fT, identifier);
                if (fT.map.containsKey(identifier)) {
                    return fT.map.get(identifier);
                }
                //file exists, but load failed. copying to another location
                try {
                    Calendar c = Calendar.getInstance();
                    int date = c.get(Calendar.DATE);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    FileUtils.copyFile(f, new File(fT.folder, fN.replaceAll(fT.typeName + ".", "") + "+backup+" + date + "+" + hour + "+" + minute));
                } catch (Exception ignored) {
                }
            }
            fD = fT.fIO.construct(identifier, fN, fT);
            fD.f = f;
            saveFile(fD, true);
            return fD;
        }
    }

    public static void loadAll() {
        fileStatusMapper.clear();
        fileMessageMapper.clear();
        invalidFileNameList.clear();

        for(FileType fT : FileType.getFileTypeList()) {
            if (!fT.loadOption.equals(IOOption.LOAD_ALL_ON_START)) {
                continue;
            }

            fT.map.clear();
            if (fT.folder.exists()) {
                loadDefaultFile(fT);
                loadFileTreeRecursively(fT.folder, fT, "");
                server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_WHITE + "Loaded " + fT.map.keySet().size() + " " + fT.typeName);
            } else {
                FileUtil.loadSubfolder(fT.folder.getName());
            }
        }
    }

    public static void saveAll() {
        fileStatusMapper.clear();
        fileMessageMapper.clear();
        invalidFileNameList.clear();

        for(FileType fT : FileType.getFileTypeList()) {
            if (!fT.saveOption.equals(IOOption.SAVE_ALL_ON_STOP)) {
                continue;
            }
            if (!fT.folder.exists()) {
                FileUtil.loadSubfolder(fT.folder.getName());
            }
            for (FileData fD : fT.map.values()) {
                saveFile(fD, true);
            }
            server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_WHITE + "Saved " + fT.map.keySet().size() + " " + fT.typeName);
            fT.map.clear();
        }
    }

    public static void loadFileTreeRecursively(File folder, FileType fT, String root) {
        root = root + folder.getName() + ".";
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.getName().contains("yml")) {
                loadFile(f, root + f.getName(), fT, null);
            } else if (f.isDirectory()){
                loadFileTreeRecursively(f, fT, root);
            }
        }
    }

    public static FileData loadFile(File f, String fN, FileType fT, String estimatedIdentifier) {
        Bukkit.getLogger().info(fN);
        FileConfiguration fC = YamlConfiguration.loadConfiguration(f);
        fileStatusMapper.put(fN, true);
        fileMessageMapper.put(fN, "");
        String identifier = "";
        identifier = (String) Verifier.getObject(fC, fN, "identifier", identifier, VerifierType.STRING, true, f.getParentFile());
        if (fT.map.containsKey(identifier)) {
            fileStatusMapper.put(fN, false);
            fileMessageMapper.put(fN, fileMessageMapper.get(fN) + consolePluginPrefix + "identifier [" + identifier + "] is used\n");
        }
        if (estimatedIdentifier != null && !estimatedIdentifier.equals(identifier)) {
            fileStatusMapper.put(fN, false);
            fileMessageMapper.put(fN, fileMessageMapper.get(fN) + consolePluginPrefix + "identifier [" + identifier + "] is not estimated\n");
        }
        if (fileStatusMapper.get(fN)) {
            fileMessageMapper.put(fN, fileMessageMapper.get(fN) + consolePluginPrefix + "Loading file [" + fN + "]\n");
            //goto specific io
            FileData fD = fT.fIO.loadData(fC, fN, fT, identifier, f.getParentFile());
            if (fD != null) {
                fD.fT = fT;
                fD.f = f;
            }
            if (fileStatusMapper.get(fN)) {
                if (fD != null) {
                    fT.map.put(identifier,fD);
                }
                return fD;
            } else {
                invalidFileNameList.add(fN);
                server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_RED + "Failed when loading " + fT.name().toLowerCase(Locale.ROOT) + " [" + identifier + "]\n\n" + fileMessageMapper.get(fN) + ColorHandler.ANSI_WHITE);
            }
        }
        FileData fD = fT.fIO.construct(identifier, fN, fT);
        fD.f = f;
        return fD;
    }

    public static void saveFile(FileData fD, boolean force) {
        Bukkit.getLogger().info("saving");
        File f = fD.f;
        if (!force && f.exists()) {
            return;
        }
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (Exception ignored) {
        }
        FileConfiguration fC = YamlConfiguration.loadConfiguration(f);
        fC.set("identifier", fD.identifier);
        fC = fD.fT.fIO.saveData(fC, fD);
        try {
            fC.save(f);
        } catch (Exception ignored) {
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
        FileIO.fileStatusMapper.put(fileName, false);
    }

    public static void addErrorMsg(String fileName, String root, String path, String msg) {
        String cacheMsg = "";
        if  (FileIO.fileMessageMapper.containsKey(fileName)) {
            cacheMsg = FileIO.fileMessageMapper.get(fileName);
        }
        FileIO.fileMessageMapper.remove(fileName);
        FileIO.fileMessageMapper.put(fileName, cacheMsg + consolePluginPrefix + root + path + " " + msg + "\n");
    }

    public abstract FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder);

    public abstract FileConfiguration saveData(FileConfiguration fC, FileData fD);

    public abstract FileData construct(String identifier, String fN, FileType fT);
}
