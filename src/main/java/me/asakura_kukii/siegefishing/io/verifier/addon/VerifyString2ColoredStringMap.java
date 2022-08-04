package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.utility.format.Color;
import me.asakura_kukii.siegefishing.utility.format.UnicodeHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VerifyString2ColoredStringMap extends Verifier {

    public VerifyString2ColoredStringMap() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);
        HashMap<String, String> string2ColoredStringMap = new HashMap<>();
        for (String s : sL) {
            string2ColoredStringMap.putAll((Map<? extends String, ? extends String>) verifyString2ColoredString(s, fileName, path, root, obj, folder));
        }
        return string2ColoredStringMap;
    }

    public static Object verifyString2ColoredString(String s, String fileName, String path, String root, Object obj, File folder) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length != 2) {
                Loader.addErrorMsg(fileName, path, root, "[" + s + "] has invalid argument count");
                formatCorrect = false;
            }
        } else {
            Loader.addErrorMsg(fileName, path, root, "[" + s + "] lacks separation");
            formatCorrect = false;
        }

        if (formatCorrect) {
            HashMap<String, String> string2ColoredStringMap = new HashMap<>();
            String s1 = s.split("\\^")[1];
            s1 = UnicodeHandler.map(s1);
            s1 = Color.mapColorCode(s1);
            string2ColoredStringMap.put(s.split("\\^")[0], ChatColor.translateAlternateColorCodes('&', s1));
            return string2ColoredStringMap;
        } else {
            Loader.putError(fileName);
            return new HashMap<String, String>();
        }
    }
}
