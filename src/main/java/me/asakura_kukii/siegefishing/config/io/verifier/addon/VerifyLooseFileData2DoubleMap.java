package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class VerifyLooseFileData2DoubleMap extends Verifier {

    public VerifyLooseFileData2DoubleMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<FileData, Double> fileData2DoubleMap = new HashMap<>();
        for (String s : sL) {
            fileData2DoubleMap.putAll((Map<? extends FileData, ? extends Double>) verifyLooseFileData2DoubleMap(s, fileName, path, root, obj, folder));
        }
        return fileData2DoubleMap;
    }

    public static Object verifyLooseFileData2DoubleMap(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        FileType fT = null;

        if (s.contains("^")) {
            if (s.split("\\^").length==3) {
                for (String s2 : s.split("\\^")) {
                    if (index == 0) {
                        if (!FileType.getFileTypeNameList().contains(s2)) {
                            FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not typeName");
                            formatCorrect = false;
                        } else {
                            fT = FileType.getFileTypeFromName(s2);
                        }
                    } else if (index == 1 && fT != null) {
                        if (!fT.map.containsKey(s2)) {
                            FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not " + fT.typeName + " identifier");
                            formatCorrect = false;
                        }
                    } else {
                        try {
                            Double.parseDouble(s2);
                        } catch (Exception ignored) {
                            FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not double");
                            formatCorrect = false;
                        }
                    }
                    index ++;
                }
            } else {
                FileIO.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                formatCorrect = false;
            }
        } else {
            FileIO.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
            formatCorrect = false;
        }
        if (formatCorrect) {
            HashMap<FileData, Double> fileData2DoubleMap = new HashMap<>();
            fileData2DoubleMap.put(Objects.requireNonNull(FileType.getFileTypeFromName(s.split("\\^")[0])).map.get(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2]));
            return fileData2DoubleMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<FileData, Double>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        HashMap<FileData, Double> fileData2DoubleMap = (HashMap<FileData, Double>) obj;
        for (FileData fD1 : fileData2DoubleMap.keySet()) {
            Double d = fileData2DoubleMap.get(fD1);
            String s = fD1.fT.typeName + "^" + fD1.identifier + "^" + d.toString();
            if (fD1.identifier.equalsIgnoreCase("")) {
                s = "";
            }
            sL.add(s);
        }
        fC.set(path, sL);
        return fC;
    }
}
