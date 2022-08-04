package me.asakura_kukii.siegefishing.handler.fishing.render;

import me.asakura_kukii.siegefishing.SiegeFishing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RodRender {
    private static Map<UUID, HookTracker> trackerMap = new HashMap<>();
    private static Map<UUID, Long> lastSpawnMap = new HashMap<>();
    private static long tick = 0;
    // todo move these to fishing manager
    public void startFishing(Player player, HookTracker hookTracker){
        trackerMap.put(player.getUniqueId(), hookTracker);
    }

    public void stopFishing(Player player){
        trackerMap.remove(player.getUniqueId());
        lastSpawnMap.remove(player.getUniqueId());
    }

    public static void startLoop(){
        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    HookTracker hookTracker = trackerMap.get(player.getUniqueId());
                    if (hookTracker == null){
                        return;
                    }
                    RodRenderParam renderParam = hookTracker.getRenderParam();
                    Long lastSpawn = lastSpawnMap.computeIfAbsent(player.getUniqueId(), (ignored) -> tick);

                    // if is in cooldown
                    if (tick - lastSpawn < renderParam.getInteval()){
                        return;
                    }
                    lastSpawnMap.put(player.getUniqueId(), tick);
                    new RodRenderTask(hookTracker, player).start();
                });
                tick++;
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 0, 0);
    }

    public boolean isFishing(Player player){
        return trackerMap.get(player.getUniqueId()) != null;
    }
}
