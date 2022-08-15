package me.asakura_kukii.siegefishing.handler.method.shovel;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.*;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import me.asakura_kukii.siegefishing.utility.random.WeightedRandomPick;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class ShovelTaskData {

    public static HashMap<UUID, ShovelTaskData> shovelTaskDataMap = new HashMap<>();

    public PlayerData pD;
    public ShovelData sD;
    public Random r;
    public Location current;
    public Location target;
    public int waitTimer = 300;
    public int waitTime = 300;
    public int successCounter = 3;
    public final double rayTraceDistance = 5;
    public static List<UUID> killNext = new ArrayList<>();

    public ShovelTaskData(PlayerData pD, ShovelData sD) {
        this.pD = pD;
        this.sD = sD;
        this.r = new Random();
        this.successCounter = (int) Math.ceil(r.nextDouble() * 4 + 2);
        initiate();
    }

    public static void killLast() {
        for (UUID uuid : killNext) {
            shovelTaskDataMap.remove(uuid);
        }
        killNext.clear();
    }

    public void initiate() {
        RayTraceResult rTR = pD.p.rayTraceBlocks(rayTraceDistance, FluidCollisionMode.ALWAYS);
        if (rTR == null) {
            //msg1
            return;
        }
        Block hitBlock = rTR.getHitBlock();
        if (hitBlock == null || hitBlock.isLiquid() || !sD.digMaterialList.contains(hitBlock.getType())) {
            //msg2
            return;
        }
        Material m = hitBlock.getLocation().add(new Vector(0, 1, 0)).getBlock().getType();
        if (!m.equals(Material.AIR)) {
            //msg3
            return;
        }
        current = rTR.getHitBlock().getLocation().clone().add(new Vector(2 * (r.nextDouble() - 0.5) * 0.3, 0.5, 2 * (r.nextDouble() - 0.5)));
        target = rTR.getHitBlock().getLocation();
        try {
            SoundHandler.playSoundAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (SoundData) FileType.SOUND.map.get("default_dig"));
            ParticleHandler.spawnParticleAtLoc(rTR.getHitPosition().toLocation(pD.p.getWorld()).clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_smoke"), false);
        } catch (Exception ignored) {
        }
        shovelTaskDataMap.put(pD.p.getUniqueId(), this);
        searchSurround();
    }

    public void searchSurround() {
        List<Location> possibleLocation = new ArrayList<>();
        Location search = target.clone();
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                if (i == 0 && j == 0) continue;
                Location l = search.clone().add(new Vector(i, 0, j));
                if (checkValid(l)) possibleLocation.add(l);
            }
        }
        Random r = new Random();
        int index = (int) Math.floor(r.nextDouble() * possibleLocation.size());
        if (index < 0) index = 0;
        if (index > possibleLocation.size() - 1) index = possibleLocation.size() - 1;
        target = possibleLocation.get(index);
    }

    public void waitInteract() {
        waitTimer--;
        if (waitTimer == 0) {
            kill();
        }
    }

    public void kill() {
        killNext.add(pD.p.getUniqueId());
    }

    public void success() {
        List<FishData> fishDataList = new ArrayList<>();
        List<Double> weightList = new ArrayList<>();
        List<Double> modifiedWeightList = new ArrayList<>();

        for (FileData fD : FileType.CRAB_REGION.map.values()) {
            CrabRegionData cRD = (CrabRegionData) fD;
            double grayValue = cRD.regionValue.getRelativeValueAt(current.getChunk().getX(), current.getChunk().getZ());
            if (grayValue < 0.8) continue;
            fishDataList.addAll(cRD.percentMap.keySet());
            weightList.addAll(cRD.percentMap.values());
        }

        int index = 0;
        for (FishData fD : fishDataList) {
            double weight = weightList.get(index);
            modifiedWeightList.add(weight);
            index ++;
        }
        //random pick one using weight map
        WeightedRandomPick wRP = new WeightedRandomPick(modifiedWeightList);
        //initialize fish
        FishData fD = fishDataList.get(wRP.getIndex());
        ItemStack iS = ItemData.getItemStack(fD, pD, 1, 0);
        Item i = Objects.requireNonNull(current.clone().add(new Vector(0, 1.0, 0)).getWorld()).dropItem(current.clone().add(new Vector(0, 1.0, 0)), iS);
        i.setVelocity(new Vector(0, 0.3, 0));
        try {
            SoundHandler.playSoundAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (SoundData) FileType.SOUND.map.get("default_success"));
        } catch (Exception ignored) {
        }

        if (!pD.unlockFishIdentifierMap.containsKey(fD.identifier)) {
            try {
                ParticleHandler.spawnParticleAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_success_discover"),false);
            } catch (Exception ignored) {
            }
            pD.unlockFishIdentifierMap.put(fD.identifier, 1);
        } else {
            try {
                ParticleHandler.spawnParticleAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_success"),false);
            } catch (Exception ignored) {
            }
            int amount = pD.unlockFishIdentifierMap.get(fD.identifier);
            pD.unlockFishIdentifierMap.remove(fD.identifier);
            pD.unlockFishIdentifierMap.put(fD.identifier, amount + 1);
        }

        if ((boolean) NBTHandler.get(iS, "special", Boolean.class)) {
            if (!pD.specialFishIdentifierMap.containsKey(fD.identifier)) {
                pD.specialFishIdentifierMap.put(fD.identifier, 1);
            } else {
                int amount = pD.specialFishIdentifierMap.get(fD.identifier);
                pD.specialFishIdentifierMap.remove(fD.identifier);
                pD.specialFishIdentifierMap.put(fD.identifier, amount + 1);
            }
        }
        kill();
    }

    public void update() {
        if (successCounter == 0) {
            try {
                ParticleHandler.spawnParticleAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_success_discover"), false);
            } catch (Exception ignored) {
            }
            success();
            kill();
        }
        Location realTargetLocation = target.clone().add(0.5, 0.5, 0.5);
        if (current.clone().distance(realTargetLocation.clone()) < 0.4) {
            waitInteract();
            return;
        }


        Vector targetDirection = realTargetLocation.clone().toVector().subtract(current.clone().toVector()).normalize();
        Vector randomDirection = new Vector(2 * (r.nextDouble() - 0.5) * 0.3, 0, 2 * (r.nextDouble() - 0.5));
        if (randomDirection.getX() != 0 && randomDirection.getZ() != 0) {
            randomDirection = randomDirection.normalize();
        }
        Vector targetVel = targetDirection.clone().multiply(0.2);
        Vector randomVel = randomDirection.clone().multiply(0.2);
        Vector totalVel = targetVel.clone().add(randomVel.clone());
        current.add(totalVel);

        try {
            double hint = r.nextDouble();
            if (hint > 0.7) {
                ParticleHandler.spawnParticleAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_crab"), false);
            }
        } catch (Exception ignored) {
        }
    }

    public void interact() {
        RayTraceResult rTR = pD.p.rayTraceBlocks(rayTraceDistance, FluidCollisionMode.ALWAYS);
        if (rTR == null) {
            //msg1
            return;
        }

        Block hitBlock = rTR.getHitBlock();
        if (hitBlock == null || hitBlock.isLiquid()) {
            //msg2
            return;
        }
        Material m = hitBlock.getLocation().add(new Vector(0, 1, 0)).getBlock().getType();
        if (!m.equals(Material.AIR)) {
            //msg3
            return;
        }
        Location interactLocation = rTR.getHitBlock().getLocation();
        try {
            SoundHandler.playSoundAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (SoundData) FileType.SOUND.map.get("default_dig"));
        } catch (Exception ignored) {
        }
        if (waitTimer != 0 && interactLocation.getBlockX() == target.getBlockX() && interactLocation.getBlockY() == target.getBlockY() && interactLocation.getBlockZ() == target.getBlockZ()) {
            //refresh timer and change target
            waitTimer = waitTime;
            try {
                SoundHandler.playSoundAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (SoundData) FileType.SOUND.map.get("default_success"));
                ParticleHandler.spawnParticleAtLoc(current.clone().add(new Vector(0, 0.5, 0)), (ParticleData) FileType.PARTICLE.map.get("default_smoke"), false);
            } catch (Exception ignored) {
            }
            searchSurround();
            successCounter--;
        } else {
            try {
                ParticleHandler.spawnParticleAtLoc(rTR.getHitPosition().toLocation(pD.p.getWorld()).clone(), (ParticleData) FileType.PARTICLE.map.get("default_smoke"), false);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean checkValid(Location l) {
        if (l.getBlock().isLiquid() || l.getBlock().isPassable()) return false;
        if (!l.clone().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.AIR)) return false;
        return true;
    }
}
