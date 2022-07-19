package me.asakura_kukii.siegefishing.handler.item.gun.fire;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.gun.util.PosingHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class SemiAutoHandler {

    public static void semiAutoFiring(GunData gD, ItemStack iS, int slot, PlayerData pD, String state, long lastShot, String cPS) {
        Vector firingVector = pD.p.getEyeLocation().getDirection().clone().normalize();
        firingVector.add(semiAutoInaccuracyHandler(gD, pD, lastShot, cPS)).normalize();
        FiringHandler.stepBackHandler(gD, pD, false);
        FiringHandler.initiateDamageLine(gD, pD.p, firingVector, cPS);
        pD.gunReserve -= gD.bulletCountPerShot;
        if (pD.stateCache.containsKey("Scope") && !pD.stateCache.get("Scope") && SiegeFishing.forceUpdateWhenNormalSemiFire) {
            PosingHandler.updateGunPose(gD, iS, slot, pD);
        }
    }

    public static Vector semiAutoInaccuracyHandler(GunData gD, PlayerData pD, long lastShot, String cPS) {
        double inaccuracyMultiplier = 1.00;
        double accurateTimeMultiplier = 1.00;
        if (pD.stateCache.containsKey("Scope") && pD.stateCache.get("Scope")) {
            inaccuracyMultiplier = SiegeFishing.scopeInaccuracyMultiplier;
        }
        try {
            inaccuracyMultiplier = (double) NBTHandler.get(cPS, "iM", Double.class);
            accurateTimeMultiplier = (double) NBTHandler.get(cPS, "aTM", Double.class);
        } catch (Exception ignored) {
        }
        gD.lastShotTimer.put(pD.p.getUniqueId(),System.currentTimeMillis());
        double inaccuracyDegree;
        //time to calculate the direction of firing
        if (lastShot <= gD.accurateTime1 * 1000 * accurateTimeMultiplier) {
            inaccuracyDegree = gD.inaccuracy0 - (gD.inaccuracy0 - gD.inaccuracy1) * (lastShot / (gD.accurateTime1 * 1000));
        } else {
            inaccuracyDegree = gD.inaccuracy1 - gD.inaccuracy1 * ((lastShot - (gD.accurateTime1 * 1000)) / (gD.accurateTime2 * 1000 - gD.accurateTime1 * 1000));
        }
        if (inaccuracyDegree < 0) {
            inaccuracyDegree = 0;
        }
        Random random = new Random();
        return new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize().multiply(Math.sin(inaccuracyDegree / 180.0 * Math.PI)).multiply(inaccuracyMultiplier);
    }
}
