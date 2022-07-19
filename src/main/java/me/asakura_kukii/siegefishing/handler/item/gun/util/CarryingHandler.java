package me.asakura_kukii.siegefishing.handler.item.gun.util;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class CarryingHandler {
    public static void toggleCarrying(GunData gD, ItemStack iS, int s, PlayerData pD, String state) {
        PosingHandler.updatePlayerPose(pD);

        if (pD.stateCache.get("Scope") && pD.stateCache.get("Carry")) {
            ScopingHandler.breakScoping(pD);
            //scope and then sprint
            pD.stateCache.remove("Scope");
            pD.stateCache.put("Scope", false);
        }
        PosingHandler.updateGunPose(gD, iS, s, pD);
    }

    public static void stopSprinting(PlayerData pD) {
        int fL = pD.p.getFoodLevel();
        pD.p.setFoodLevel(6);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SiegeFishing.pluginInstance, new Runnable() {
            @Override
            public void run() {
                pD.p.setFoodLevel(fL);
            }
        }, 5);
    }
}
