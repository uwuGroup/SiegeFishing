package me.asakura_kukii.siegefishing.utility;

import org.bukkit.util.Vector;

public class VectorUtil {
    public static Vector normalize(Vector vector){
        if (vector.length() <= 0){
            return new Vector(1, 0, 0);
        }
        return vector.normalize();
    }

}
