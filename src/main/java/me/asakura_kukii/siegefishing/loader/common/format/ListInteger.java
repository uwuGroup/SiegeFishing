package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ListInteger extends Format {
    public ListInteger() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<java.lang.Integer> iL = new ArrayList<>();
        for (java.lang.String s : sL) {
            java.lang.Integer i = (java.lang.Integer) Integer.checkInteger(s, fileName, path, root, obj);
            iL.add(i);
        }
        return iL;
    }
}
