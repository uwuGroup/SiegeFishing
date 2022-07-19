package me.asakura_kukii.siegefishing.utility.boundingbox;

import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class BoundingBoxHandler {

    public static BoundingBox getBoundingBox(LivingEntity e) {
        switch (e.getType()) {
            case PLAYER:
            case SKELETON:
            case ILLUSIONER:
            case CREEPER:
            case DROWNED:
            case HUSK:
            case PILLAGER:
            case STRAY:
            case VILLAGER:
            case VINDICATOR:
            case WANDERING_TRADER:
            case WITCH:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
            case PIGLIN:
            case ZOMBIFIED_PIGLIN:
                return (BoundingBox) new HumanoidBoundingBox(e);
            case ENDERMAN: return (BoundingBox) new HumanoidBoundingBox(e, 2.4, 0.3, 0.5, 0.3, 0.2);
            case WITHER_SKELETON: return (BoundingBox) new HumanoidBoundingBox(e, 2.0, 0.3, 0.5, 0.3, 0.2);
        }
        return null;
    }

    public static boolean twoPointCylinderCheck(Location start, Vector direction, Location point1, Location point2, double radius) {
        Vector startToOne = point1.clone().toVector().subtract(start.clone().toVector());
        Vector startToTwo = point2.clone().toVector().subtract(start.clone().toVector());
        Vector twoToOne = point1.clone().toVector().subtract(point2.clone().toVector());
        Vector directionClone = direction.clone().normalize();
        double a = startToOne.clone().subtract(directionClone.clone().multiply(startToOne.dot(directionClone.clone()))).length();
        double b = startToTwo.clone().subtract(directionClone.clone().multiply(startToTwo.dot(directionClone.clone()))).length();
        double twoOneDistanceOnLine = twoToOne.clone().dot(directionClone.clone());
        double c = twoToOne.clone().subtract(directionClone.clone().multiply(twoToOne.dot(directionClone.clone()))).length();
        double h;
        if (c == 0) {
            h = a;
        } else {
            h = Math.sqrt((a + b + c) * (a + b - c) * (a - b + c) * (-a + b + c)) / (2 * c);
        }
        double compensation = Math.abs(twoOneDistanceOnLine) * radius / twoToOne.length();
        return !(Math.sqrt(a * a - h * h) > c + compensation) && !(Math.sqrt(b * b - h * h) > c + compensation) && !(h > radius);
    }

    public static Location twoPointCylinderCheck(Location start, Vector direction, Location point1, Location point2, double height, double radius) {
        Vector startToOne = point1.clone().toVector().subtract(start.clone().toVector());
        Vector startToTwo = point2.clone().toVector().subtract(start.clone().toVector());
        Vector twoToOne = point1.clone().toVector().subtract(point2.clone().toVector());
        Vector directionClone = direction.clone().normalize();
        double a = startToOne.clone().subtract(directionClone.clone().multiply(startToOne.dot(directionClone.clone()))).length();
        double b = startToTwo.clone().subtract(directionClone.clone().multiply(startToTwo.dot(directionClone.clone()))).length();
        double twoOneDistanceOnLine = twoToOne.clone().dot(directionClone.clone());
        double c = twoToOne.clone().subtract(directionClone.clone().multiply(twoToOne.dot(directionClone.clone()))).length();
        double h;
        if (c == 0) {
            h = a;
        } else {
            h = Math.sqrt((a + b + c) * (a + b - c) * (a - b + c) * (-a + b + c)) / (2 * c);
        }
        double compensation = Math.abs(twoOneDistanceOnLine) * radius / twoToOne.length();
        double d;
        if (Math.sqrt(a * a - h * h) > c) {
            d = -Math.sqrt(b * b - h * h);
        } else {
            d = Math.sqrt(b * b - h * h);
        }
        Location hitLocation = point2.clone();

        if (!(Math.sqrt(a * a - h * h) > c + compensation) && !(Math.sqrt(b * b - h * h) > c + compensation) && !(h > radius)) {
            d = d * twoToOne.length() / c;

            hitLocation.add(twoToOne.clone().normalize().multiply(d));
            Vector relativeRight = directionClone.clone().crossProduct(twoToOne.clone()).normalize();
            double e = startToTwo.clone().dot(relativeRight);
            if (e < 0) {
                hitLocation.add(relativeRight.clone().normalize().multiply(h));
            } else {
                hitLocation.add(relativeRight.clone().normalize().multiply(-h));
            }
            Location anchor = hitLocation.clone();
            double f = Math.sqrt(radius * radius - h * h);
            f = f * twoToOne.length() / c;
            hitLocation.add(directionClone.clone().multiply(-f));
            double g = hitLocation.clone().toVector().subtract(anchor.clone().toVector()).dot(twoToOne.clone().normalize());
            Vector hitVector = hitLocation.clone().toVector().subtract(anchor.clone().toVector());
            if (d + g > height && g != 0) {
                if (g > 0) {
                    hitVector.multiply((height - d) / g);
                }
            } else if (d + g < 0 && g != 0) {
                if (g < 0) {
                    hitVector.multiply(d / (- g));
                }
            }
            ParticleHandler.spawnParticle(anchor.clone().add(hitVector), (ParticleData) FileType.PARTICLE.map.get("default_blood"));
            return anchor.clone().add(hitVector).clone();
        } else {
            return null;
        }
    }
}
