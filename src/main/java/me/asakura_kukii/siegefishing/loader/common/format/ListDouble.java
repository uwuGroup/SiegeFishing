package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ListDouble extends Format {
    public ListDouble() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<java.lang.Double> dL = new ArrayList<>();
        for (java.lang.String s : sL) {
            java.lang.Double d = (java.lang.Double) Double.checkDouble(s, fileName, path, root, obj);
            dL.add(d);
        }
        return dL;
    }
}
