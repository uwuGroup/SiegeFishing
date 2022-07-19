package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ListMaterial extends Format {
    public ListMaterial() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<Material> mL = new ArrayList<>();
        for (java.lang.String s : sL) {
            Material m = (Material) Material.checkMaterial(s, fileName, path, root, obj);
            mL.add(m);
        }
        return mL;
    }
}
