package me.asakura_kukii.siegefishing.utility.format;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class FormatHandler {
    public static String format(String s, boolean wrap) {
        s = ColorHandler.map(s);
        s = UnicodeHandler.map(s, wrap);
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static List<String> format(List<String> sL) {
        List<String> formattedStringList = new ArrayList<>();
        for (String s : sL) {
            formattedStringList.add(format(s, false));
        }
        return formattedStringList;
    }
}
