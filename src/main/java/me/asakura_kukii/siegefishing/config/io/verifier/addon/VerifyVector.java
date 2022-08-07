package me.asakura_kukii.siegefishing.config.io.verifier.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.addondatatype.SiegeVector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class VerifyVector extends Verifier {

    public VerifyVector() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyVector(s, fileName, path, root, obj);
    }

    public static Object verifyVector(String s, String fileName, String path, String root, Object obj) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==3 || s.split("\\^").length==5) {
                for (String s2 : s.split("\\^")) {
                    try {
                        Double.parseDouble(s2);
                    } catch (Exception ignored) {
                        FileIO.addErrorMsg(fileName, root, path, "[" + s + "][" + s2 + "] is not double");
                        formatCorrect = false;
                    }
                }
            } else {
                FileIO.addErrorMsg(fileName, root, path, "[" + s + "] lacks argument");
                formatCorrect = false;
            }
        } else {
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] has invalid argument count");
            formatCorrect = false;
        }

        if (formatCorrect) {
            return new SiegeVector(Double.parseDouble(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2]));
        } else {
            FileIO.putError(fileName);
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        SiegeVector sV = (SiegeVector) obj;
        String s = sV.x + "^" + sV.y + "^" + sV.z;
        fC.set(path, s);
        return fC;
    }
}
