package me.asakura_kukii.siegefishing.loader.common.format;

import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ListVector extends Format {
    public ListVector() {}

    @Override
    public Object check(ConfigurationSection cS, java.lang.String fileName, java.lang.String path, java.lang.String root, Object obj) {
        List<java.lang.String> sL = cS.getStringList(path);

        List<Vector3D> vL = new ArrayList<>();
        for (java.lang.String s : sL) {
            Vector3D v = (Vector3D) Vector.checkVector(s, fileName, path, root, obj);
            vL.add(v);
        }
        return vL;
    }
}
