package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ThrowHandler extends StageHandler{
    @Override
    public void update(FishingTaskData fTD) {
        //if in water, next stage
        if (fTD.hookLoc.getBlock().isLiquid() && fTD.hookLoc.getBlock().getType().equals(Material.WATER)) {
            fTD.fTS = StageType.FLOAT;
            return;
        }

        if (fTD.hookVel.length() > 0.5) {
            fTD.hookVel = fTD.hookVel.normalize().multiply(0.5);
        }

        //collision check
        RayTraceResult rTR = Objects.requireNonNull(fTD.hookLoc.clone().getWorld()).rayTraceBlocks(fTD.hookLoc.clone(), fTD.hookVel.clone(), fTD.hookVel.clone().length(), FluidCollisionMode.NEVER, true);
        if (rTR != null) {
            Vector bounceDirection = Objects.requireNonNull(rTR.getHitBlockFace()).getDirection().normalize();
            Vector bouncePosition = rTR.getHitPosition();
            Vector d = bounceDirection.clone().multiply(fTD.hookVel.clone().dot(bounceDirection.clone()));
            Vector e = fTD.hookVel.clone().subtract(d.clone()).multiply(1);
            Vector f = d.clone().multiply(-1).multiply(0);
            Vector bounceVelocity = e.clone().add(f.clone());
            fTD.hookLoc = bouncePosition.toLocation(Objects.requireNonNull(fTD.hookLoc.getWorld()));
            fTD.hookVel = bounceVelocity;
        } else {
            fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);
        }

        //distance control
        if (fTD.distance > fTD.maxDistance * 0.5) {
            Vector g = fTD.hookLoc.clone().toVector().subtract(fTD.rodEndLoc.clone().toVector()).normalize();
            if (g.clone().dot(fTD.hookVel.clone()) > 0) {
                Vector h = g.clone().normalize().multiply(fTD.hookVel.clone().dot(g));
                double attenuationFactor = (fTD.maxDistance - fTD.distance) / (0.5 * fTD.maxDistance);
                if (attenuationFactor < 0) attenuationFactor = 0;
                fTD.hookVel = fTD.hookVel.clone().subtract(h.clone()).add(h.clone().multiply(attenuationFactor)).clone();
            }
        }


        //gravity
        fTD.hookVel.add(fTD.hookAcc);
    }
}
