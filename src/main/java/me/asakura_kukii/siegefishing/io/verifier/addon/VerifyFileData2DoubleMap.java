package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.io.verifier.data.SiegeVector;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;

public class VerifyFileData2DoubleMap extends Verifier {

    public VerifyFileData2DoubleMap() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<FileData, Double> fileData2DoubleMap = new HashMap<>();
        for (String s : sL) {
            fileData2DoubleMap.putAll((Map<? extends FileData, ? extends Double>) verifyFileData2Double(s, fileName, path, root, obj, folder));
        }
        return fileData2DoubleMap;
    }

    public static Object verifyFileData2Double(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        FileType referenceFileType = null;
        if (!(obj instanceof FileData)) {
            Loader.addErrorMsg(fileName, path, root, "reference object error, contact developers");
            formatCorrect = false;
        } else {
            referenceFileType = ((FileData) obj).fT;
        }
        FileType fT = null;

        if (referenceFileType != null) {
            if (s.contains("^")) {
                if (s.split("\\^").length==3) {
                    for (String s2 : s.split("\\^")) {
                        if (index == 0) {
                            if (!FileType.getFileTypeNameList().contains(s2)) {
                                Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not typeName");
                                formatCorrect = false;
                            } else {
                                fT = FileType.getFileTypeFromName(s2);
                                if (!fT.equals(referenceFileType)) {
                                    Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not " + referenceFileType.typeName);
                                    formatCorrect = false;
                                }
                            }
                        } else if (index == 1 && fT != null) {
                            if (!fT.map.containsKey(s2)) {
                                Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not " + fT.typeName + " identifier");
                                formatCorrect = false;
                            }
                        } else {
                            try {
                                Double.parseDouble(s2);
                            } catch (Exception ignored) {
                                Loader.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not double");
                                formatCorrect = false;
                            }
                        }
                        index ++;
                    }
                } else {
                    Loader.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                    formatCorrect = false;
                }
            } else {
                Loader.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
                formatCorrect = false;
            }
        }

        if (formatCorrect) {
            HashMap<FileData, Double> fileData2DoubleMap = new HashMap<>();
            fileData2DoubleMap.put(Objects.requireNonNull(FileType.getFileTypeFromName(s.split("\\^")[0])).map.get(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2]));
            return fileData2DoubleMap;
        } else {
            Loader.putError(fileName);
            return new HashMap<FileData, Double>();
        }
    }
}
