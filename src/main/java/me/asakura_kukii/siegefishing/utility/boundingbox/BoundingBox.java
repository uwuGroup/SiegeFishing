package me.asakura_kukii.siegefishing.utility.boundingbox;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface BoundingBox {
    Location intersects(Location start, Vector direction);
    Location intersectsHead(Location start, Vector direction);
    Location intersectsBody(Location start, Vector direction);
    boolean headShotCheck(Location start, Vector direction);
}
