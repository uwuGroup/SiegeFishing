package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListColoredString extends Format {

    public ListColoredString() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<java.lang.String> cSL = new ArrayList<>();
        for (java.lang.String s : sL) {
            cSL.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cSL;
    }
}
