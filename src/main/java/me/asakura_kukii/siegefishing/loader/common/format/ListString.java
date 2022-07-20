package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class ListString extends Format {

    public ListString() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj, File folder) {
        return cS.getStringList(path);
    }
}
