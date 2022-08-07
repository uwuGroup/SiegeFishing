package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Objects;

public class VerifyFileData extends Verifier {

    public VerifyFileData() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyFileData(s, fileName, path, root, obj, folder);
    }

    public static Object verifyFileData(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        int index = 0;
        FileType referenceFileType = null;
        if (!(obj instanceof FileData)) {
            FileIO.addErrorMsg(fileName, path, root, "reference object error, contact developers");
            formatCorrect = false;
        } else {
            referenceFileType = ((FileData) obj).fT;
        }
        FileType fT = null;

        if (referenceFileType != null) {
            if (s.contains("^")) {
                if (s.split("\\^").length==2) {
                    for (String s2 : s.split("\\^")) {
                        if (index == 0) {
                            if (!FileType.getFileTypeNameList().contains(s2)) {
                                FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not typeName");
                                formatCorrect = false;
                            } else {
                                fT = FileType.getFileTypeFromName(s2);
                                if (!fT.equals(referenceFileType)) {
                                    FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not " + referenceFileType.typeName);
                                    formatCorrect = false;
                                }
                            }
                        } else if (fT != null) {
                            if (!fT.map.containsKey(s2)) {
                                FileIO.addErrorMsg(fileName, path, root, "[" + s + "][" + s2 + "] is not " + fT.typeName + " identifier");
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
        }


        if (formatCorrect) {
            return Objects.requireNonNull(FileType.getFileTypeFromName(s.split("\\^")[0])).map.get(s.split("\\^")[1]);
        } else {
            FileIO.putError(fileName);
            return obj;
        }
    }
    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        FileData fD1 = (FileData) obj;
        String s = fD1.fT.typeName + "^" + fD1.identifier;
        fC.set(path, s);
        return fC;
    }
}
