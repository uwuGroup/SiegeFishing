package me.asakura_kukii.siegefishing.utility.coodinate;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.asakura_kukii.siegefishing.utility.patch.ModifiedArmorStand;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.hypot;

public class PositionHandler {



    public static Entity generateArmorStandStack(Player p, UUID uuid, int length) {
        if (p.isInWater()) {
            p.setVelocity(new Vector(0, 1, 0));
            return null;
        } else  {
            UUID previous = uuid;
            for (int i = 0; i < length; i++) {
                Entity e = Bukkit.getServer().getEntity(previous);
                if (e == null) {
                    break;
                }

                ArmorStand aS = (ArmorStand) ModifiedArmorStand.SpawnModifiedArmorStand(e.getLocation());
                aS.setMarker(true);
                e.addPassenger(aS);
                p.sendMessage("added one to" + e.getName() + e.getPassengers());
                previous = aS.getUniqueId();
            }
            return Bukkit.getServer().getEntity(previous);
        }
    }

    public static void rotateArmorStandStack(Entity e, Float yaw, Float pitch) {
        for (Entity p : e.getPassengers()) {
            if (p instanceof ArmorStand) {
                p.setRotation(yaw, pitch);
                rotateArmorStandStack(p, yaw, pitch);
            }
        }
    }

    public static void deleteArmorStandStack(Entity e) {
        for (Entity p : e.getPassengers()) {
            if (p instanceof ArmorStand) {
                deleteArmorStandStack(p);
            }
        }
        if (e instanceof ArmorStand) {
            e.remove();
        }
    }

    public static EulerAngle convertVectorToEulerAngle(Vector vec) {
        double yaw = -atan2(vec.getX(), vec.getZ());
        double pitch = -atan2(vec.getY(), hypot(vec.getX(), vec.getZ()));
        return new EulerAngle(pitch, yaw, 0);
    }

    public static Location locRelativeToLivingEntitySight(LivingEntity entity, Double x, Double y, Double z) {
        Vector rightVector = rightVectorToLivingEntitySight(entity).multiply(x);
        Vector upVector = upVectorToLivingEntitySight(entity).multiply(y);
        Vector locVector = entity.getEyeLocation().getDirection().normalize().multiply(z).add(rightVector).add(upVector);
        return entity.getEyeLocation().add(locVector);
    }

    public static Location locRelativeToLivingEntitySightUsingAngle(LivingEntity entity, Double thetaBias, Double pitchBias, Double distanceMultiplier) {
        Vector rightVector = rightVectorToLivingEntitySight(entity).multiply(Math.tan(thetaBias * Math.PI));
        Vector upVector = upVectorToLivingEntitySight(entity).multiply(Math.tan(pitchBias * Math.PI));
        Vector locVector = entity.getEyeLocation().getDirection().normalize().add(rightVector).add(upVector).normalize();
        return entity.getEyeLocation().add(locVector.multiply(distanceMultiplier));
    }

    public static Vector VecRelativeToLivingEntitySight(LivingEntity entity, Double thetaBias, Double pitchBias, Double thetaMultiplier, Double pitchMultiplier) {
        double theta = Math.atan2(entity.getEyeLocation().getDirection().getX(), entity.getEyeLocation().getDirection().getZ()) * thetaMultiplier;
        double pitch = (-entity.getEyeLocation().getPitch()) / 180 * Math.PI * pitchMultiplier;
        theta -= (Math.PI * thetaBias);
        pitch += (Math.PI * pitchBias);
        double factor = Math.cos(pitch) / Math.abs(Math.cos(pitch));
        double x = 1*Math.sin(theta) * factor;
        double z = 1*Math.cos(theta) * factor;
        double y = Math.abs(Math.sqrt(x*x+z*z)/Math.cos(pitch))*Math.sin(pitch);
        return new Vector(x,y,z).normalize();
    }

    public static Vector rightVectorToLivingEntitySight(LivingEntity entity) {
         return VecRelativeToLivingEntitySight(entity,0.5,0.0, 1.0, 0.0);
    }

    public static Vector upVectorToLivingEntitySight(LivingEntity entity) {
        return VecRelativeToLivingEntitySight(entity,0.0,0.5, 1.0, 1.0);
    }

    public static Vector frontVectorToLivingEntitySight(LivingEntity entity) {
        return entity.getLocation().getDirection().normalize();
    }

    public static Vector frontVectorToLivingEntitySightWithoutPitch(LivingEntity entity) {
        return VecRelativeToLivingEntitySight(entity,0.0,0.0, 1.0, 0.0);
    }

    public static Vector upVectorToLivingEntitySightWithoutPitch(LivingEntity entity) {
        return new Vector(0,1,0);
    }
}
