package me.asakura_kukii.siegefishing.handler.nonitem.method.damageline;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundHandler;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.boundingbox.BoundingBox;
import me.asakura_kukii.siegefishing.utility.boundingbox.BoundingBoxHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.block.BlockHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleHandler;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class DamageLineHandler extends MethodHandler {

    public DamageLineHandler() {};

    public void init(MethodNodeData mND, Location start, Vector direction, Entity e, HashMap<String, Object> extraData) {
        damageLine(mND.next, start, direction, e, (DamageLineData) mND.data.get(0), extraData);
    }

    public static void damageLine(List<MethodNodeData> mNDL, Location start, Vector direction, Entity shooter, DamageLineData dLD, HashMap<String, Object> extraData) {
        double dM = 1,rM = 1;
        if (extraData.containsKey("dM")) {
            dM = (double) extraData.get("dM");
        }
        if (extraData.containsKey("rM")) {
            rM = (double) extraData.get("rM");
        }


        double maxDistance = dLD.length * rM;
        HashMap<Location, Double> blocksHit = new HashMap<>();
        HashMap<Entity, Location> entitiesHit = new HashMap<>();
        HashMap<Double, Object> objectsHit = new HashMap<>();
        HashMap<Object, Double> finalHitMap = new HashMap<>();
        Location centerTest = start.clone().add(direction.clone().multiply(maxDistance/2));

        for (Entity e : Objects.requireNonNull(Objects.requireNonNull(centerTest.getWorld()).getNearbyEntities(centerTest, maxDistance/2, maxDistance/2, maxDistance/2))) {
            if (e instanceof LivingEntity) {
                if (e != shooter && e != shooter.getVehicle() && !entitiesHit.containsKey(e)) {
                    Vector startToE = e.getLocation().add(new Vector(0, 1, 0)).toVector().subtract(start.clone().toVector());
                    double eDistanceOnLine = startToE.clone().dot(direction.clone());
                    double eDistanceToLine = startToE.clone().subtract(direction.clone().multiply(startToE.dot(direction.clone()))).length();

                    if (eDistanceToLine <= 3.0) {
                        BoundingBox bB = BoundingBoxHandler.getBoundingBox((LivingEntity) e);
                        if (bB != null) {
                            Location hitLocation = bB.intersects(start.clone(), direction.clone());
                            if (hitLocation != null) {
                                entitiesHit.put(e, hitLocation);
                                objectsHit.put(eDistanceOnLine, e);
                            }
                        }
                    }
                }
            }
        }

        double finalDistance = dLD.length * rM;
        Location finalLocation = start.clone().add(direction.clone().normalize().multiply(finalDistance));
        boolean headShotSound = false;

        for (double dist = 0; dist <= dLD.length * rM; dist = dist + SiegeFishing.blockCheckSpacing) {
            Location current = start.clone().add(direction.clone().normalize().multiply(dist));
            Material m = current.getBlock().getType();
            if (m == Material.AIR) {
                continue;
            }

            if (SiegeFishing.breakableBlockMap.containsKey(m) && !blocksHit.containsKey(current)) {
                blocksHit.put(current, dist);
                objectsHit.put(dist, current);

            } else {
                RayTraceResult rTR = Objects.requireNonNull(current.clone().getWorld()).rayTraceBlocks(current.clone(), direction.clone().normalize(), SiegeFishing.blockCheckSpacing, FluidCollisionMode.NEVER, true);
                if (rTR != null) {
                    finalDistance = dist;
                    finalLocation = current;
                    break;
                }
            }
        }

        double damage = dLD.damage * dM;
        //Calculating final damage
        if (!objectsHit.isEmpty()) {
            Object[] keySet = objectsHit.keySet().toArray();
            Arrays.sort(keySet);
            for(Object o: keySet) {
                Object value = objectsHit.get((Double) o);
                if ((Double) o > finalDistance) {
                    break;
                }
                if (value instanceof Location) {

                    Location l = (Location) value;
                    Material m = l.getBlock().getType();

                    ParticleHandler.spawnParticle(l, ParticleHandler.blockDustParticle(l.getBlock().getType()));

                    if (!BlockHandler.blockHealthMapper.containsKey(l.getBlock().getLocation())) {
                        BlockHandler.blockHealthMapper.put(l.getBlock().getLocation(), 1.00);
                    }

                    if (SiegeFishing.penetrableBlockMap.containsKey(m)) {
                        double blockDamage = damage * SiegeFishing.penetrableBlockMap.get(m);
                        double remainingDamage = 0;
                        //check remaining damage;
                        double health = BlockHandler.blockHealthMapper.get(l.getBlock().getLocation()) * SiegeFishing.breakableBlockMap.get(m);
                        if (health < blockDamage) {
                            remainingDamage = blockDamage - health;
                            health = 0;
                        } else {
                            health = health - blockDamage;
                        }
                        BlockHandler.blockHealthMapper.remove(l.getBlock().getLocation());
                        BlockHandler.blockHealthMapper.put(l.getBlock().getLocation(), health / SiegeFishing.breakableBlockMap.get(m));
                        BlockHandler.blockBreakHandler(l.getBlock().getLocation(), m);
                        damage = damage - blockDamage + remainingDamage;

                    } else {
                        double blockDamage = damage;
                        double remainingDamage = 0;
                        //check remaining damage;
                        double health = BlockHandler.blockHealthMapper.get(l.getBlock().getLocation()) * SiegeFishing.breakableBlockMap.get(m);
                        if (health < blockDamage) {
                            remainingDamage = blockDamage - health;
                            health = 0;
                        } else {
                            health = health - blockDamage;
                        }
                        BlockHandler.blockHealthMapper.remove(l.getBlock().getLocation());
                        BlockHandler.blockHealthMapper.put(l.getBlock().getLocation(), health / SiegeFishing.breakableBlockMap.get(m));
                        BlockHandler.blockBreakHandler(l.getBlock().getLocation(), m);
                        damage = damage - blockDamage + remainingDamage;
                        //block damage
                    }


                    //hitBlock trigger
                    for (MethodNodeData mND : mNDL) {
                        if (mND.trigger.matches("hitBlock")) {
                            MethodHandler.execute(mND, l.clone(), direction.clone(), shooter, extraData);
                        }
                    }

                } else if (value instanceof LivingEntity) {
                    LivingEntity e = (LivingEntity) value;

                    BoundingBox bB = BoundingBoxHandler.getBoundingBox((LivingEntity) e);
                    if (bB != null) {
                        double entityDamage = damage;
                        if (bB.headShotCheck(start.clone(), direction.clone())) {
                            entityDamage *= SiegeFishing.headShotMultiplier;
                            headShotSound = true;
                            if (e instanceof Player) {
                                SoundHandler.playSoundToPlayer(((Player) e), direction.clone().multiply(-1).normalize(), (SoundData) FileType.SOUND.map.get("default_headshot"));
                            }
                        }
                        ((LivingEntity) e).damage(entityDamage, shooter);
                        ((LivingEntity) e).setNoDamageTicks(0);

                        damage *= 0.6;
                    }

                    //hitEntity trigger
                    for (MethodNodeData mND : mNDL) {
                        if (mND.trigger.matches("hitEntity")) {
                            MethodHandler.execute(mND, entitiesHit.get(e).clone(), direction.clone(), shooter, extraData);
                        }
                    }

                    for (ParticleData pD : dLD.hitParticleDataList) {
                        ParticleHandler.spawnParticle(entitiesHit.get(e), pD);
                    }

                    for (SoundData sD : dLD.hitSoundDataList) {
                        SoundHandler.playSoundAtLoc(entitiesHit.get(e), sD);
                    }
                }

                if (damage <= 0) {
                    finalLocation = start.clone().add(direction.clone().normalize().multiply((Double) o));
                    finalDistance = (Double) o;
                    break;
                }
            }
        }
        for (MethodNodeData mND : mNDL) {
            if (mND.trigger.matches("vanish")) {
                MethodHandler.execute(mND, finalLocation, direction.clone(), shooter, extraData);
            }
        }

        for (ParticleData pD : dLD.vanishParticleDataList) {
            ParticleHandler.spawnParticle(finalLocation, pD);
        }

        for (SoundData sD : dLD.vanishSoundDataList) {
            SoundHandler.playSoundAtLoc(finalLocation, sD);
        }

        if (finalLocation.getBlock().getType() != Material.AIR) {
            ParticleHandler.spawnParticle(finalLocation, ParticleHandler.blockDustParticle(finalLocation.getBlock().getType()));
        }

        double trailTransition = SiegeFishing.trailTransitionStart;
        for (double dist = SiegeFishing.trailBias; dist <= finalDistance; dist = dist + SiegeFishing.trailSpacing + trailTransition) {
            trailTransition *= SiegeFishing.trailTransitionFactor;
            Location current = start.clone().add(direction.clone().normalize().multiply(dist));


            for (ParticleData particleData : dLD.trailParticleDataList) {
                ParticleHandler.spawnParticle(current, particleData);
            }

            for (SoundData sD : dLD.trailSoundDataList) {
                SoundHandler.playSoundAtLoc(current, sD);
            }
        }

        for (Object o : finalHitMap.keySet()) {
            if (o instanceof Location) {
                shooter.sendMessage("Hit: " + ((Location) o).getBlock().getType().toString() + " " + finalHitMap.get(o));
            } else if (o instanceof Entity) {
                shooter.sendMessage("Hit: " + ((Entity) o).getName() + " " + finalHitMap.get(o));
            }
        }
        if (headShotSound) {
            if (shooter instanceof Player) {
                SoundHandler.playSoundToPlayer(((Player) shooter), direction.clone().normalize(), (SoundData) FileType.SOUND.map.get("default_headshot"));
            }
        }
    }
}
