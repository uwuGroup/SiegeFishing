package me.asakura_kukii.siegefishing.handler.nonitem.method.projectile;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundHandler;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProjectileHandler extends MethodHandler {
    public ProjectileHandler() {};

    public static List<ProjectileData> projectileCache = new ArrayList<>();

    public void init(MethodNodeData mND, Location start, Vector direction, Entity e, HashMap<String, Object> extraData) {
        projectile(mND.next, start, direction, e, (ProjectileData) mND.data.get(0), extraData);
    }

    public void projectile(List<MethodNodeData> next, Location start, Vector direction, Entity e, ProjectileData pD, HashMap<String, Object> extraData) {

        ProjectileData pDClone = pD.cloneData();
        pDClone.currentPos = start.clone();
        pDClone.currentVel = direction.clone().multiply(pD.vel);
        pDClone.shooter = e;
        pDClone.next = next;
        pDClone.extraData = extraData;
        projectileCache.add(pDClone);
    }

    public static void projectileUpdater() {
        List<ProjectileData> deadProjectileList = new ArrayList<>();

        for (ProjectileData pD : projectileCache) {
            RayTraceResult rTR = Objects.requireNonNull(pD.currentPos.clone().getWorld()).rayTraceBlocks(pD.currentPos.clone(), pD.currentVel.clone(), pD.currentVel.clone().length(), FluidCollisionMode.NEVER, true);




            Location lastPos = pD.currentPos.clone();

            if (pD.life >= pD.maxLife || (pD.bounce >= pD.maxBounce) && (pD.maxBounce >= 0) && !pD.sticky && !pD.attach) {
                deadProjectileList.add(pD);
                //vanish
                for (MethodNodeData mND : pD.next) {
                    if (mND.trigger.matches("vanish")) {
                        MethodHandler.execute(mND, pD.currentPos.clone(), pD.currentVel.clone(), pD.shooter, pD.extraData);
                    }
                }

                for (ParticleData particleData : pD.vanishParticleDataList) {
                    ParticleHandler.spawnParticle(pD.currentPos.clone(), particleData);
                }

                for (SoundData sD : pD.vanishSoundDataList) {
                    SoundHandler.playSoundAtLoc(pD.currentPos.clone(), sD);
                }
                continue;
            }

            if (pD.attach && pD.attachTarget.isDead()) {
                pD.attach = false;
                pD.attachTarget = null;
                pD.attachVector = null;
            }

            if (pD.attachable) {
                RayTraceResult eRTR = Objects.requireNonNull(pD.currentPos.clone().getWorld()).rayTraceEntities(pD.currentPos.clone(), pD.currentVel.clone(), pD.currentVel.clone().length());
                if (eRTR != null&& !pD.attach && eRTR.getHitEntity() != pD.shooter) {
                    pD.attach = true;
                    pD.attachTarget = eRTR.getHitEntity();
                    pD.attachVector = eRTR.getHitPosition().clone().subtract(pD.attachTarget.getLocation().toVector().clone());
                }
            }

            if (pD.attach) {
                pD.currentPos = pD.attachTarget.getLocation().clone().add(pD.attachVector);
                pD.currentVel = pD.currentVel.add(new Vector(pD.acc.x, pD.acc.y, pD.acc.z));
            } else if (rTR != null) {
                Vector bounceDirection = Objects.requireNonNull(rTR.getHitBlockFace()).getDirection().normalize();
                Vector bouncePosition = rTR.getHitPosition();

                Vector d = bounceDirection.clone().multiply(pD.currentVel.clone().dot(bounceDirection.clone()));
                Vector e = pD.currentVel.clone().subtract(d.clone()).multiply(pD.bounceFriction);
                Vector f = d.clone().multiply(-1).multiply(pD.bounceFactor);
                Vector g = e.clone().add(f.clone());


                pD.currentPos = bouncePosition.toLocation(Objects.requireNonNull(pD.currentPos.getWorld()));

                if (!(pD.bounce >= pD.maxBounce && pD.sticky)) {
                    //bounce

                    pD.currentVel = g.add(new Vector(pD.acc.x, pD.acc.y, pD.acc.z));
                    for (MethodNodeData mND : pD.next) {
                        if (mND.trigger.matches("bounce")) {
                            MethodHandler.execute(mND, pD.currentPos.clone(), pD.currentVel.clone(), pD.shooter, pD.extraData);
                        }
                    }

                    for (ParticleData particleData : pD.bounceParticleDataList) {
                        ParticleHandler.spawnParticle(bouncePosition.clone().toLocation(Objects.requireNonNull(pD.currentPos.getWorld())), particleData);
                    }

                    for (SoundData sD : pD.bounceSoundDataList) {
                        SoundHandler.playSoundAtLoc(bouncePosition.clone().toLocation(Objects.requireNonNull(pD.currentPos.getWorld())), sD);
                    }
                }

                pD.bounce ++;
            } else {
                pD.currentPos = pD.currentPos.add(pD.currentVel);
                pD.currentVel = pD.currentVel.add(new Vector(pD.acc.x, pD.acc.y, pD.acc.z));
            }

            if (pD.maxVel > 0 && pD.currentVel.clone().length() >= pD.maxVel) {
                pD.currentVel = pD.currentVel.clone().normalize().multiply(pD.maxVel);
            }



            Vector trailVector = pD.currentPos.clone().toVector().subtract(lastPos.clone().toVector());
            double length = trailVector.length();
            Vector trailUnitVector = trailVector.clone().normalize();

            if (length > 0) {
                for (double i = 0; i < length; i = i + SiegeFishing.trailSpacing) {
                    Location l = lastPos.clone().add(trailUnitVector.clone().multiply(i));
                    for (ParticleData particleData : pD.trailParticleDataList) {
                        ParticleHandler.spawnParticle(l.clone(), particleData);
                    }
                }
            } else {
                for (ParticleData particleData : pD.trailParticleDataList) {
                    ParticleHandler.spawnParticle(pD.currentPos.clone().clone(), particleData);
                }
            }


            for (SoundData sD : pD.trailSoundDataList) {
                SoundHandler.playSoundAtLoc(pD.currentPos.clone(), sD);
            }

            if (pD.life % pD.interval == 0 && pD.life >= pD.intervalStart && pD.life <= pD.intervalStop) {

                //interval
                for (MethodNodeData mND : pD.next) {
                    if (mND.trigger.matches("interval")) {
                        MethodHandler.execute(mND, pD.currentPos.clone(), pD.currentVel.clone(), pD.shooter, pD.extraData);
                    }
                }

                for (ParticleData particleData : pD.intervalParticleDataList) {
                    ParticleHandler.spawnParticle(pD.currentPos.clone(), particleData);
                }

                for (SoundData sD : pD.intervalSoundDataList) {
                    SoundHandler.playSoundAtLoc(pD.currentPos.clone(), sD);
                }
            }
            pD.life++;
        }

        for (ProjectileData pD : deadProjectileList) {
            projectileCache.remove(pD);
        }
    }
}
