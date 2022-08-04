package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.utility.format.Color;
import me.asakura_kukii.siegefishing.utility.format.UnicodeHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyColoredStringList extends Verifier {

    public VerifyColoredStringList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<String> cSL = new ArrayList<>();
        for (String s : sL) {
            s = UnicodeHandler.map(s);
            s = Color.mapColorCode(s);
            cSL.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cSL;
    }
}
