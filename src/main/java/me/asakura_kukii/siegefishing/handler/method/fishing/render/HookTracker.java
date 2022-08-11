package me.asakura_kukii.siegefishing.handler.method.fishing.render;

import org.bukkit.Location;

public interface HookTracker {
    Location getRodLocation();

    void updateTracerLocation(Location nextLocation);
}
