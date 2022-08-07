package me.asakura_kukii.siegefishing.handler.fishing;

import me.asakura_kukii.siegefishing.handler.fishing.render.HookTracker;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * trace an entity as a hook
 */
public class SiegeHookTracer implements HookTracker {
    private Entity entity;

    public SiegeHookTracer(Entity entity){
        this.entity = entity;
    }
    @Override
    public Location getRodLocation() {
        return entity.getLocation();
    }

    @Override
    public void updateTracerLocation(Location nextLocation) {
        this.entity.teleport(nextLocation);
    }

}
