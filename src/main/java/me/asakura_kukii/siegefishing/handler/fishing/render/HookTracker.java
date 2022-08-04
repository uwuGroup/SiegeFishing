package me.asakura_kukii.siegefishing.handler.fishing.render;

import org.bukkit.Location;

public interface HookTracker {
    Location getRodLocation();
    RodRenderParam getRenderParam();
}
