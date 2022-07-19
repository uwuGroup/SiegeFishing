package me.asakura_kukii.siegefishing.handler.nonitem.method.common;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashMap;

public abstract class MethodHandler {
    public abstract void init(MethodNodeData mND, Location start, Vector direction, Entity e, HashMap<String, Object> extraData);

    public static void execute(MethodNodeData mND, Location start, Vector direction, Entity e, HashMap<String, Object> extraData) {
        mND.mT.mH.init(mND, start, direction, e, extraData);
    }
}
