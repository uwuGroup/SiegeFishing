package me.asakura_kukii.siegefishing.handler.item.gun.fire;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import me.asakura_kukii.siegefishing.main.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class FullAutoHandler {
    public static HashMap<UUID, BukkitTask> fullAutoFiringMap = new HashMap<>();
    public static HashMap<UUID, Integer> fullAutoFiringResidualBulletMap = new HashMap<>();
    public static HashMap<UUID, Integer> fullAutoFiringSlotMap = new HashMap<>();
    public static HashMap<UUID, GunData> fullAutoFiringGunDataMap = new HashMap<>();
    public static HashMap<UUID, ItemStack> fullAutoFiringItemStackMap = new HashMap<>();

    public static void fullAutoFiring(GunData gD, ItemStack iS, int slot, PlayerData pD, String state, long lastShot, String cPS) {
        double horizontalRecoilMultiplier = 1.00;
        double verticalRecoilMultiplier = 1.00;
        try {
            horizontalRecoilMultiplier = (double) NBTHandler.get(cPS, "hRM", Double.class);
            verticalRecoilMultiplier = (double) NBTHandler.get(cPS, "vRM", Double.class);
        } catch (Exception ignored) {
        }

        fullAutoFiringGunDataMap.put(pD.p.getUniqueId(), gD);
        fullAutoFiringItemStackMap.put(pD.p.getUniqueId(), iS);
        fullAutoFiringSlotMap.put(pD.p.getUniqueId(), slot);
        int gunResidualBulletCount = GunData.getReserve(iS);
        if (pD.gunReserve != -1) {
            gunResidualBulletCount = pD.gunReserve;
            pD.gunReserve = -1;
        }
        int finalGunResidualBulletCount = gunResidualBulletCount;
        double finalHorizontalRecoilMultiplier = horizontalRecoilMultiplier;
        double finalVerticalRecoilMultiplier = verticalRecoilMultiplier;
        fullAutoFiringMap.put(pD.p.getUniqueId(), new BukkitRunnable() {
            int firedAmount = 0;
            int amount = finalGunResidualBulletCount;
            @Override
            public void run() {
                if (!pD.stateCache.get("FullAuto")) {
                    fullAutoFiringMap.remove(pD.p.getUniqueId()).cancel();
                    stopFullAutoFiring(gD, pD, iS, slot, amount);
                    firedAmount = 0;
                    return;
                }

                if (amount < gD.bulletCountPerShot) {
                    fullAutoFiringMap.remove(pD.p.getUniqueId()).cancel();
                    updateAfterFullAutoFiring(gD, pD, iS, slot, amount);
                    firedAmount = 0;
                    return;
                }
                Double theta = gD.recoilCurve.get(firedAmount).x * finalHorizontalRecoilMultiplier / 180 * Math.PI;
                Double pitch = gD.recoilCurve.get(firedAmount).y * finalVerticalRecoilMultiplier / 180 * Math.PI;
                if (pD.stateCache.containsKey("Scope") && pD.stateCache.get("Scope")) {
                    theta *= SiegeFishing.scopeRecoilMultiplier;
                    pitch *= SiegeFishing.scopeInaccuracyMultiplier;
                }
                Vector firingVector = PositionHandler.VecRelativeToLivingEntitySight((LivingEntity) pD.p, theta, pitch, 1.0, 1.0);
                if (firedAmount == 0) {
                    firingVector.add(SemiAutoHandler.semiAutoInaccuracyHandler(gD, pD, lastShot, cPS)).normalize();
                } else {
                    firingVector.add(fullAutoInaccuracyHandler(gD, pD, firedAmount, cPS)).normalize();
                }
                FiringHandler.initiateDamageLine(gD, pD.p, firingVector, cPS);
                if(gD.stepBack) {
                    FiringHandler.stepBackHandler(gD, pD, true);
                }

                firedAmount = firedAmount + gD.bulletCountPerShot;
                amount = amount - gD.bulletCountPerShot;
                if (amount < 0)
                    amount = 0;
                fullAutoFiringResidualBulletMap.remove(pD.p.getUniqueId());
                fullAutoFiringResidualBulletMap.put(pD.p.getUniqueId(), amount);
                pD.gunReserve = amount;
                //QualityArmory.sendHotbarGunAmmoCount(player, g, temp, false, amount, g.getMaxBullets());
            }
        }.runTaskTimer(Main.getInstance(), 0, (long) (gD.fullAutoFireDelay * 20)));
    }

    public static void stopFullAutoFiring(GunData gD, PlayerData pD, ItemStack iS, int slot, int amount) {
        if (amount == 0) {
            updateAfterFullAutoFiring(gD, pD, iS, slot, amount);
        } else {
            pD.gunReserve = amount;
        }
    }

    public static void updateAfterFullAutoFiring(GunData gD, PlayerData pD, ItemStack iS, int slot, int amount) {
        pD.p.getInventory().setItem(slot, iS);
        if (amount == 0) {
            gD.rT.r.reload(gD, iS, slot, pD);
        }
    }

    public static boolean checkFullAutoFiring(PlayerData pD) {
        return fullAutoFiringMap.containsKey(pD.p.getUniqueId());
    }

    public static ItemStack breakFullAutoFiring(PlayerData pD) {
        ItemStack fix = fullAutoFiringItemStackMap.get(pD.p.getUniqueId());
        fix = GunData.setReserve(fullAutoFiringGunDataMap.get(pD.p.getUniqueId()), fix, fullAutoFiringResidualBulletMap.get(pD.p.getUniqueId()));
        fullAutoFiringGunDataMap.remove(pD.p.getUniqueId());
        fullAutoFiringItemStackMap.remove(pD.p.getUniqueId());
        fullAutoFiringSlotMap.remove(pD.p.getUniqueId());
        fullAutoFiringResidualBulletMap.remove(pD.p.getUniqueId());
        fullAutoFiringMap.remove(pD.p.getUniqueId()).cancel();
        return fix;
    }

    public static void breakUpdateFullAutoFiring(PlayerData pD) {
        //StateHandler.updateGunPoseIndexToHandItemStack(pD.p.getInventory().getHeldItemSlot(), pD);
        pD.p.getInventory().setItemInOffHand(breakFullAutoFiring(pD));
    }

    public static Vector fullAutoInaccuracyHandler(GunData gD, PlayerData pD, int firedAmount, String cPS) {
        double inaccuracyMultiplier = 1.00;
        if (pD.stateCache.containsKey("Scope") && pD.stateCache.get("Scope")) {
            inaccuracyMultiplier = SiegeFishing.scopeInaccuracyMultiplier;
        }
        try {
            inaccuracyMultiplier = (double) NBTHandler.get(cPS, "iM", Double.class);
        } catch (Exception ignored) {
        }

        double inaccuracyDegree = firedAmount * gD.inaccuracyStep;
        if (inaccuracyDegree >= gD.inaccuracyMax) {
            inaccuracyDegree = gD.inaccuracyMax;
        }
        Random random = new Random();
        return new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize().multiply(Math.sin(inaccuracyDegree / 180.0 * Math.PI)).multiply(inaccuracyMultiplier);
    }
}
