package me.asakura_kukii.siegefishing.handler.item.gun.fire;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundHandler;
import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import me.asakura_kukii.siegefishing.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class FiringHandler {

    public static void initiateFiring(GunData gD, ItemStack iS, int slot, PlayerData pD, String state) {
        if (!FullAutoHandler.fullAutoFiringMap.containsKey(pD.p.getUniqueId())) {
            if (pD.gunReserve == -1) {
                pD.gunReserve = GunData.getReserve(iS);
            }
            int gunResidualBulletCount = pD.gunReserve;
            if (gD.bulletCountPerShot <= gunResidualBulletCount) {
                long lastShot;
                if (gD.lastShotTimer.containsKey(pD.p.getUniqueId())) {
                    lastShot = System.currentTimeMillis() - gD.lastShotTimer.get(pD.p.getUniqueId());
                } else {
                    lastShot = -1;
                }
                if (lastShot < 0 || gD.semiAutoFireDelay * 1000L < lastShot) {

                    String currentPropertyString = (String) NBTHandler.get(iS, "current", null);

                    if (PlayerHandler.playerDataMapper.get(pD.p.getUniqueId()).stateCache.containsKey("FullAuto")) {
                        if (gD.fullAuto && pD.stateCache.get("FullAuto")) {
                            FullAutoHandler.fullAutoFiring(gD, iS, slot, pD, state, lastShot, currentPropertyString);
                        } else {
                            SemiAutoHandler.semiAutoFiring(gD, iS, slot, pD, state, lastShot, currentPropertyString);
                        }
                    } else {
                        SemiAutoHandler.semiAutoFiring(gD, iS, slot, pD, state, lastShot, currentPropertyString);
                    }
                }
            } else {
                gD.rT.r.reload(gD, iS, slot, pD);
            }
        }
    }

    public static boolean checkReserveCache(PlayerData pD) {
        return pD.gunReserve == -1;
    }

    public static void updateReserveToItemStack(PlayerData pD) {
        if (pD.gunReserve != -1) {
            ItemStack offHandItemStack = pD.p.getInventory().getItemInOffHand();
            if (GunData.getData(offHandItemStack) != null) {
                GunData gD = GunData.getData(offHandItemStack);
                offHandItemStack = GunData.setReserve(gD, offHandItemStack, pD.gunReserve);
                pD.p.getInventory().setItemInOffHand(offHandItemStack);
            }
            pD.gunReserve = -1;
        }
    }

    public static void stepBackHandler(GunData gD, PlayerData pD, boolean auto) {
        Vector stepBackVector = pD.p.getLocation().getDirection().normalize().setY(0);
        stepBackVector.multiply(gD.stepBackFactor);
        stepBackVector.multiply(-1);

        Vector stepReturnVector = stepBackVector.clone();

        if (auto && gD.fullAutoFireDelay * 20 <= 2) {
            pD.p.setVelocity(stepBackVector.multiply(0.4).add(pD.p.getVelocity().clone()));
        } else {
            pD.p.setVelocity(stepBackVector.add(pD.p.getVelocity().clone()));
        }


        if (gD.stepReturn) {
            if (auto) {
                if (gD.fullAutoFireDelay * 20 > 2) {
                    new BukkitRunnable() {
                        public void run() {
                            if (Bukkit.getOnlinePlayers().contains(pD.p)) {
                                stepReturnVector.multiply(-gD.stepReturnFactor);
                                pD.p.setVelocity(stepReturnVector.add(pD.p.getVelocity().clone()));
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getInstance(), (long) (gD.fullAutoFireDelay * 10), 1000);
                }
            } else {
                new BukkitRunnable() {
                    public void run() {
                        if (Bukkit.getOnlinePlayers().contains(pD.p)) {
                            stepReturnVector.multiply(-gD.stepReturnFactor);
                            pD.p.setVelocity(stepReturnVector.add(pD.p.getVelocity().clone()));
                            cancel();
                        }
                    }
                }.runTaskTimer(Main.getInstance(), 3, 1000);
            }
        }
    }

    public static void initiateDamageLine(GunData gD, LivingEntity entity, Vector firingVector, String cPS) {
        double damageMultiplier = 1.00;
        double rangeMultiplier = 1.00;
        boolean silenced = false;
        try {
            damageMultiplier = (double) NBTHandler.get(cPS, "dM", Double.class);
            rangeMultiplier = (double) NBTHandler.get(cPS, "rM", Double.class);
            silenced = (boolean) NBTHandler.get(cPS, "sG", Boolean.class);
        } catch (Exception ignored) {
        }

        HashMap<String, Object> extraData = new HashMap<>();
        extraData.put("dM", damageMultiplier);
        extraData.put("rM", rangeMultiplier);

        Location start;
        PlayerData pD = null;
        if (entity instanceof Player) {
            pD = PlayerHandler.getPlayerData((Player) entity);
        }

        if (pD != null && pD.stateCache.get("Scope")) {
            start = PositionHandler.locRelativeToLivingEntitySight(pD.p, gD.muzzleScopeBias.x, gD.muzzleScopeBias.y, gD.muzzleScopeBias.z);
        } else {
            start = PositionHandler.locRelativeToLivingEntitySight(entity, gD.muzzleNormalBias.x, gD.muzzleNormalBias.y, gD.muzzleNormalBias.z);
        }

        final int[] duration = {0};
        PlayerData finalPD = pD;
        new BukkitRunnable() {
            public void run() {
                Location muzzleLocation;
                if (finalPD != null && finalPD.stateCache.get("Scope")) {
                    muzzleLocation = PositionHandler.locRelativeToLivingEntitySight(finalPD.p, gD.muzzleScopeBias.x, gD.muzzleScopeBias.y, gD.muzzleScopeBias.z);
                } else {
                    muzzleLocation = PositionHandler.locRelativeToLivingEntitySight(entity, gD.muzzleNormalBias.x, gD.muzzleNormalBias.y, gD.muzzleNormalBias.z);
                }
                if (duration[0] < gD.muzzleParticleDuration) {
                    for (ParticleData particleData : gD.muzzleParticleDataList)
                        ParticleHandler.spawnParticle(muzzleLocation, particleData);
                } else {
                    cancel();
                }
                duration[0]++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);


        if (gD.soundList.size() >= 1) {
            if (!silenced) {
                SoundHandler.playSoundAtLoc(start, gD.soundList.get(0));
            } else if (gD.soundList.size() >= 2) {
                SoundHandler.playSoundAtLoc(start, gD.soundList.get(1));
            } else {
                SoundHandler.playSoundAtLoc(start, gD.soundList.get(0));
            }
        }


        for (int i = 0; i < gD.bulletCountPerShot; i++) {
            Vector finalFiringVector = bulletSpreadHandler(gD, firingVector.clone());

            MethodHandler.execute(gD.mND, start.clone(), finalFiringVector.clone(), entity, extraData);
            /*
            for (Entity e : entitiesHit.keySet()) {
                BoundingBox pBB = new BoundingBox((Player) e);
                if (pBB.headShotCheck(start, direction, (Player) e)) {
                    pD.p.sendMessage(e.getName() + " = " + " Headshot! ");
                } else {
                    pD.p.sendMessage(e.getName() + " = " + " Normal.");
                }
            }
            */
        }
    }

    public static Vector bulletSpreadHandler(GunData gD, Vector v) {
        Random random = new Random();
        return v.add(new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize().multiply(Math.sin(gD.bulletSpread / 180.0 * Math.PI))).normalize();
    }
}
