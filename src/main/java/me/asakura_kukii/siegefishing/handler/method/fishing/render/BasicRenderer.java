package me.asakura_kukii.siegefishing.handler.method.fishing.render;

import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BasicRenderer extends BukkitRunnable {
    private long tick = 0;
    private static Map<UUID, Long> lastSpawnMap = new HashMap<>();
    private FishingSession fishingSession;

    public BasicRenderer(FishingSession fishingSession){
        this.fishingSession = fishingSession;
    }

    public FishingSession getFishingSession() {
        return fishingSession;
    }


    public void lastSpawn(UUID uuid, long tick){
        lastSpawnMap.put(uuid, tick);
    }

    public long getLastSpawn(UUID uuid){
        return lastSpawnMap.getOrDefault(uuid, 0L);
    }

    @Override
    public void run() {
        if(!getFishingSession().isValid()){
            cancel();
            return;
        }

        renderTick(tick);
        tick++;
    }

    protected abstract void renderTick(long tick);

    protected void onHitWater(){}

    protected void onHooked(){}

    protected void onFinish(){}

    public void onFail() {}

    protected boolean isRodItem(ItemStack itemInMainHand) {
        // todo: implement
        return true;
    }

    public void stop() {
        this.cancel();
    }
}
