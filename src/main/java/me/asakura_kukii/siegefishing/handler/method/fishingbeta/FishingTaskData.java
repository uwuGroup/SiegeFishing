package me.asakura_kukii.siegefishing.handler.method.fishingbeta;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.*;
import me.asakura_kukii.siegefishing.config.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage.StageType;
import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class FishingTaskData {
    public static HashMap<UUID, FishingTaskData> fishingTaskMap = new HashMap<>();

    public final int refVitality = 120;     //df1 vitality of fish with difficulty 1
    public final double stdDistance = 6;    //std distance for fish to approach
    public final double maxDistance = 9;   //max distance for player to lengthen the string
    public final double minDistance = 3;
    public final double getDistance = 4;    //get distance for player to get the fish
    public final double runAwayVelocity = 0.1;
    public final double randomVelocity = 0.1;
    public final double playerMaxVelocity = 0.015;
    public final double failPressure = 0.3;

    public Player p;
    public ItemStack iS;
    public PlayerData pD;
    public RodData rD;
    public FishData fD;
    public StageType fTS = StageType.THROWN;
    public HashMap<Boolean, BukkitTask> runningTaskMap = new HashMap<>();
    public Random r = new Random();

    public Location rodEndLoc;
    public Location hookLoc;
    public Vector hookVel;
    public Vector hookAcc = new Vector(0, -0.02, 0);

    public double distance;
    public double targetDistance = stdDistance;
    public double stringDistance = stdDistance;

    public double pressure;

    //wait
    public int waitTime;
    public int timer = 0;

    //hooked
    public Vector randomVel = new Vector(0, 0, 0);
    public double vitality;

    public double distanceSuccessPercentage = 0;
    public double vitalitySuccessPercentage = 0;

    public FishingTaskData(PlayerData pD, RodData rD, ItemStack iS) {
        this.p = pD.p;
        this.pD = pD;
        this.rD = rD;
        this.iS = iS;
        this.hookLoc = PositionHandler.locRelativeToLivingEntitySight(pD.p, rD.rodEndBiasX, rD.rodEndBiasY, rD.rodEndBiasZ);
        this.hookVel = pD.p.getLocation().getDirection().clone().normalize().multiply(rD.swingVelocity);

        double rG = r.nextGaussian() + 1;
        if (rG < 0.25) rG = 0.25;
        if (rG > 1.75) rG = 1.75;
        this.waitTime = (int) Math.floor(rG * rD.avgWaitTime);

        fishingTaskMap.put(pD.p.getUniqueId(), this);
    }

    public boolean finish() {
        double d = r.nextDouble();
        if (d > distanceSuccessPercentage * vitalitySuccessPercentage) {
            kill();
            return false;
        }
        ItemStack iS = ItemData.getItemStack(fD, pD, 1, 0);
        Item i = Objects.requireNonNull(hookLoc.getWorld()).dropItem(hookLoc, iS);
        i.setVelocity(pD.p.getLocation().toVector().subtract(hookLoc.toVector()).normalize().multiply(0.5).add(new Vector(0, 0.3, 0)));
        kill();
        return true;
    }

    public void kill() {
        for (BukkitTask bT : runningTaskMap.values()) {
            bT.cancel();
        }
        fishingTaskMap.remove(pD.p.getUniqueId());
    }

    public void update() {
        // check online
        if (!Bukkit.getOnlinePlayers().contains(p)) kill();
        if (!p.getInventory().getItemInMainHand().equals(iS)) kill();
        if (!Objects.equals(p.getLocation().getWorld(), hookLoc.getWorld())) kill();
        if (p.getHealth() == 0) kill();


        rodEndLoc = PositionHandler.locRelativeToLivingEntitySight(pD.p, rD.rodEndBiasX, rD.rodEndBiasY, rD.rodEndBiasZ);
        distance = rodEndLoc.clone().distance(hookLoc);
        fTS.sH.update(this);
        render();
    }

    public void render() {
        if (fTS.equals(StageType.HOOKED)) {
            //
        }
        renderHook();
        renderString();
    }

    public void renderHook() {
        ParticleHandler.spawnParticleAtLoc(hookLoc, rD.hookParticleData, false);
    }

    public void renderString() {
        Location biasedHookLoc = hookLoc.clone().add(new Vector(0, 0, 0));
        double distance = biasedHookLoc.distance(rodEndLoc.clone());
        double yDistance = Math.abs(biasedHookLoc.clone().toVector().subtract(rodEndLoc.clone().toVector()).getY());
        Vector step = biasedHookLoc.toVector().subtract(rodEndLoc.clone().toVector()).clone().normalize();
        Location start = rodEndLoc.clone().clone();

        double pressure = 1.4;
        for (double i = 0; i <= distance; i = i + ConfigData.trailDistance) {
            double percentage = i / distance;
            double densityBiasPercentage = Math.pow(percentage, ConfigData.trailDensityBiasFactor);
            double yBiasPercentage = Math.pow((1 - densityBiasPercentage), pressure) - (1 - densityBiasPercentage);
            Location temp = start.clone().add(step.clone().multiply(densityBiasPercentage * distance)).add(new Vector(0, yBiasPercentage * yDistance, 0));
            ParticleHandler.spawnParticleAtLoc(temp, rD.stringParticleData, false);
        }
    }

    public void operateString(boolean lengthen) {
        if (lengthen) {
            stringDistance += 0.05;
            targetDistance += 0.025;
            if (stringDistance > maxDistance) stringDistance = maxDistance;
            if (targetDistance > maxDistance) targetDistance = maxDistance;
        } else {
            stringDistance -= 0.05;
            targetDistance -= 0.025;
            if (stringDistance < minDistance) stringDistance = minDistance;
            if (targetDistance < minDistance) targetDistance = minDistance;
        }
        if (runningTaskMap.containsKey(!lengthen)) {
            runningTaskMap.remove(!lengthen).cancel();
            targetDistance = stringDistance;
        }
        if (runningTaskMap.containsKey(lengthen)) {
            runningTaskMap.remove(lengthen).cancel();
        }
        runningTaskMap.put(lengthen, new BukkitRunnable() {
            @Override
            public void run() {
                targetDistance = stringDistance;
                runningTaskMap.remove(lengthen);
                this.cancel();
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 5, ConfigData.refreshDelay));
    }
}
