package me.asakura_kukii.siegefishing.handler.method.fishingbeta;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.*;
import me.asakura_kukii.siegefishing.config.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.config.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.achievement.AchievementUtil;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage.StageType;
import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class FishingTaskData {
    public static HashMap<UUID, FishingTaskData> fishingTaskMap = new HashMap<>();
    public static List<UUID> killNext = new ArrayList<>();

    public final int refVitality = 200;     //df1 vitality of fish with difficulty 1
    public final double stdDistance = 8;    //std distance for fish to approach
    public final double maxDistance = 11;   //max distance for player to lengthen the string
    public final double minDistance = 4;
    public final double getDistance = 6;    //get distance for player to get the fish
    public final double runAwayVelocity = 0.1;
    public final double randomVelocity = 0.1;
    public final double playerMaxVelocity = 0.015;
    public final double failPressure = 0.3;
    public final double levelDifficultyStep = 0.6;
    public final double difficultyMin = 1.3;

    public final double lengthenDistancePerTick= 0.15;
    public final double shortenDistancePerTick = 0.05;
    public final double lengthenPressureFactor = 0.4;
    public final double shortenPressureFactor = 0.5;

    public Player p;
    public ItemStack iS;
    public PlayerData pD;
    public RodData rD;
    public FishData fD;
    public StageType fTS = StageType.THROWN;
    public HashMap<Boolean, BukkitTask> runningTaskMap = new HashMap<>();
    public Random r = new Random();
    public boolean dragged = false;
    public double difficulty = 1;

    public double totalLuckBoost = 0.0;

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
    public int soundTimer = 0;

    //hooked
    public Vector randomVel = new Vector(0, 0, 0);
    public double vitality;

    public double distanceSuccessPercentage = 0;
    public double vitalitySuccessPercentage = 0;

    public List<String> hotBarFormat = new ArrayList<>();

    public static void killLast() {
        for (UUID uuid : killNext) {
            fishingTaskMap.remove(uuid);
        }
        killNext.clear();
    }

    public FishingTaskData(PlayerData pD, RodData rD, ItemStack iS) {
        this.p = pD.p;
        this.pD = pD;
        this.rD = rD;
        this.iS = iS;
        this.hookLoc = PositionHandler.locRelativeToLivingEntitySight(pD.p, rD.rodEndBiasX, rD.rodEndBiasY, rD.rodEndBiasZ);
        this.hookVel = pD.p.getLocation().getDirection().clone().normalize().multiply(rD.swingVelocity);
        this.hotBarFormat = ExtraStringListData.getList("default_fishing_hotbar");

        double rG = r.nextGaussian() + 1;
        if (rG < 0.25) rG = 0.25;
        if (rG > 1.75) rG = 1.75;

        double timeMultiplier = 1;
        try {
            ImageValueData iV = (ImageValueData) FileType.IMAGE_VALUE.map.get("temp");
            double t = iV.getRelativeValueAt(pD.p.getLocation().getChunk().getX(), pD.p.getLocation().getChunk().getZ());
            double tPercent = (t - iV.valueMin) / (iV.valueMax - iV.valueMin);
            timeMultiplier = 1.5 - tPercent;
        } catch (Exception ignored) {
        }


        this.waitTime = (int) Math.floor(rG * rD.avgWaitTime * (1.0 - pD.activeBaitData.timeBoost) * timeMultiplier);
        this.totalLuckBoost = (rD.luckBoost + 1) * (pD.activeBaitData.luckBoost + 1);
        fishingTaskMap.put(pD.p.getUniqueId(), this);
    }

    public boolean finish() {
        double d = r.nextDouble();
        if (d > distanceSuccessPercentage * vitalitySuccessPercentage) {
            try {
                if (fTS.equals(StageType.HOOKED)) AchievementUtil.broadCast(pD.p, ExtraStringListData.random("default_fishing_failure_normal"), 32);
                SoundHandler.playSoundAtLoc(hookLoc, (SoundData) FileType.SOUND.map.get("default_failure"));
            } catch (Exception ignored) {
            }
            kill();
            return false;
        }


        pD.p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
        pD.energy = pD.energy - 0.1;

        ItemStack iS = ItemData.getItemStack(fD, pD, 1, 0);
        Item i = Objects.requireNonNull(hookLoc.getWorld()).dropItem(hookLoc, iS);
        i.setVelocity(pD.p.getLocation().toVector().subtract(hookLoc.toVector()).normalize().multiply(0.5).add(new Vector(0, 0.3, 0)));
        try {
            SoundHandler.playSoundAtLoc(hookLoc, (SoundData) FileType.SOUND.map.get("default_success"));
        } catch (Exception ignored) {
        }

        if (!pD.unlockFishIdentifierMap.containsKey(fD.identifier)) {
            try {
                ParticleHandler.spawnParticleAtLoc(hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_success_discover"),false);
                AchievementUtil.broadCast(pD.p, ExtraStringListData.random("default_fishing_discover"), 0);
            } catch (Exception ignored) {
            }
            pD.unlockFishIdentifierMap.put(fD.identifier, 1);
        } else {
            try {
                ParticleHandler.spawnParticleAtLoc(hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_success"),false);
            } catch (Exception ignored) {
            }
            int amount = pD.unlockFishIdentifierMap.get(fD.identifier);
            pD.unlockFishIdentifierMap.remove(fD.identifier);
            pD.unlockFishIdentifierMap.put(fD.identifier, amount + 1);
        }

        if ((boolean) NBTHandler.get(iS, "special", Boolean.class)) {
            try {
                AchievementUtil.broadCast(pD.p, ExtraStringListData.random("default_fishing_special"), 0);
            } catch (Exception ignored) {
            }
            if (!pD.specialFishIdentifierMap.containsKey(fD.identifier)) {
                pD.specialFishIdentifierMap.put(fD.identifier, 1);
            } else {
                int amount = pD.specialFishIdentifierMap.get(fD.identifier);
                pD.specialFishIdentifierMap.remove(fD.identifier);
                pD.specialFishIdentifierMap.put(fD.identifier, amount + 1);
            }
        }

        kill();
        return true;
    }

    public void kill() {
        for (BukkitTask bT : runningTaskMap.values()) {
            bT.cancel();
        }
        killNext.add(pD.p.getUniqueId());
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
        for (double i = ConfigData.trailDistance; i <= distance; i = i + ConfigData.trailDistance) {
            double percentage = i / distance;
            double densityBiasPercentage = Math.pow(percentage, ConfigData.trailDensityBiasFactor);
            double yBiasPercentage = Math.pow((1 - densityBiasPercentage), pressure) - (1 - densityBiasPercentage);
            Location temp = start.clone().add(step.clone().multiply(densityBiasPercentage * distance)).add(new Vector(0, yBiasPercentage * yDistance, 0));
            ParticleHandler.spawnParticleAtLocForPlayer(pD.p, temp, rD.stringParticleData);
        }
    }

    public void renderBreakString() {
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
            ParticleHandler.spawnParticleAtLocForPlayer(pD.p, temp, (ParticleData) FileType.PARTICLE.map.get("default_string_break"));
        }
    }

    public void operateString(boolean lengthen) {
        boolean stuck = false;
        if (lengthen) {


            stringDistance += lengthenDistancePerTick;
            targetDistance += lengthenDistancePerTick * (1 - lengthenPressureFactor);
            if (stringDistance > maxDistance) {
                stuck = true;
                stringDistance = maxDistance;
            }
            if (targetDistance > maxDistance) {
                stuck = true;
                targetDistance = maxDistance;
            }
            if (stuck) {
                SoundHandler.playSoundAtLoc(pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_stuck"));
            } else {
                SoundHandler.playSoundAtLoc(pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_lengthen"));
            }
        } else {
            stringDistance -= shortenDistancePerTick;
            targetDistance -= shortenDistancePerTick * (1 - shortenPressureFactor);
            if (stringDistance < minDistance) {
                stuck = true;
                stringDistance = minDistance;
            }
            if (targetDistance < minDistance) {
                stuck = true;
                targetDistance = minDistance;
            }
            if (stuck) {
                SoundHandler.playSoundAtLoc(pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_stuck"));
            } else {
                SoundHandler.playSoundAtLoc(pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_shorten"));
            }
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
