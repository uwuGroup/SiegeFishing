package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.ExtraStringListData;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.achievement.AchievementUtil;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

public class HookedHandler extends StageHandler{

    @Override
    public void update(FishingTaskData fTD) {

        if (fTD.pD.p.isInWater() && !fTD.dragged) {
            fTD.dragged = true;
            try {
                AchievementUtil.broadCast(fTD.pD.p, ExtraStringListData.random("default_fishing_failure_drag"), 32);
                fTD.kill();
            } catch (Exception ignored) {
            }
        }


        //count timer
        fTD.timer--;
        fTD.soundTimer--;
        if (fTD.soundTimer <= 0) {
            fTD.soundTimer = (int) Math.ceil(fTD.r.nextDouble() * 40 + 20);
            try {
                SoundHandler.playSoundAtLoc(fTD.hookLoc, (SoundData) FileType.SOUND.map.get("default_swim"));
            } catch (Exception ignored) {}
        }

        try {
            ParticleHandler.spawnParticleAtLoc(fTD.hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_splash_swim"), false);
        } catch (Exception ignored) {}




        //update free velocity
        updateRandomVel(fTD);

        //run away velocity
        Vector runAwayDirection = getRunAwayDirection(fTD);

        double distancePercentage = fTD.distance / fTD.targetDistance;
        double distancePercentageRelativeToStdDistance = distancePercentage - 1;

        //player motion
        Vector playerVel = fTD.pD.p.getVelocity().add(runAwayDirection.clone().multiply(distancePercentage).multiply(fTD.playerMaxVelocity));
        fTD.pD.p.setVelocity(playerVel.clone());

        //approach std motion
        fTD.hookVel = fTD.hookVel.add(runAwayDirection.multiply(-distancePercentageRelativeToStdDistance).multiply(fTD.runAwayVelocity));

        double pressureFromControlledDistance = 8 * (fTD.distance / fTD.stringDistance - fTD.distance / fTD.targetDistance) + fTD.distance / fTD.targetDistance;
        double pressureValue = fTD.difficulty * pressureFromControlledDistance;
        fTD.pressure = pressureValue;


        //tiredness decreasing velocity, which is (pressure - 1) * difficulty
        double vitalityDamage = pressureValue - fTD.difficulty;
        if (vitalityDamage < 0) vitalityDamage = 0;
        fTD.vitality = fTD.vitality - vitalityDamage;

        //collision check
        if (fTD.hookVel.length() == 0) {
            fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);
            return;
        }
        RayTraceResult rTR = Objects.requireNonNull(fTD.hookLoc.clone().getWorld()).rayTraceBlocks(fTD.hookLoc.clone().add(new Vector(0, -1, 0)), fTD.hookVel.clone(), fTD.hookVel.clone().length(), FluidCollisionMode.NEVER, true);
        if (rTR != null) {
            //bounce, set timer to 0 to recalculate random direction
            fTD.timer = 0;
            Vector bounceDirection = Objects.requireNonNull(rTR.getHitBlockFace()).getDirection().normalize();
            Vector bouncePosition = rTR.getHitPosition();
            Vector d = bounceDirection.clone().multiply(fTD.hookVel.clone().dot(bounceDirection.clone()));
            Vector e = fTD.hookVel.clone().subtract(d.clone()).multiply(1);
            Vector f = d.clone().multiply(-1).multiply(0.4);
            Vector bounceVelocity = e.clone().add(f.clone());
            fTD.hookLoc = bouncePosition.toLocation(Objects.requireNonNull(fTD.hookLoc.getWorld())).add(new Vector(0, 1, 0));
            fTD.hookVel = bounceVelocity;
        } else {
            fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);
        }
        //check pressure and result
        if (fTD.pressure > fTD.rD.maxPressure) {
            try {
                AchievementUtil.broadCast(fTD.pD.p, ExtraStringListData.random("default_fishing_failure_break"), 32);
                SoundHandler.playSoundAtLoc(fTD.hookLoc, (SoundData) FileType.SOUND.map.get("default_failure"));
                SoundHandler.playSoundAtLoc(fTD.hookLoc, (SoundData) FileType.SOUND.map.get("default_break"));
            } catch (Exception ignored) {
            }
            fTD.renderBreakString();
            fTD.kill();
        }
        if (fTD.pressure < fTD.failPressure * fTD.difficulty) {
            try {
                AchievementUtil.broadCast(fTD.pD.p, ExtraStringListData.random("default_fishing_failure_loose"), 32);
                SoundHandler.playSoundAtLoc(fTD.hookLoc, (SoundData) FileType.SOUND.map.get("default_failure"));
            } catch (Exception ignored) {
            }
            fTD.timer = 0;
            fTD.kill();
        }

        fTD.distanceSuccessPercentage = 1 - (fTD.distance - fTD.getDistance) / (fTD.stdDistance - fTD.getDistance);
        if (fTD.distanceSuccessPercentage > 1) fTD.distanceSuccessPercentage = 1;
        if (fTD.distanceSuccessPercentage < 0) fTD.distanceSuccessPercentage = 0;

        fTD.vitalitySuccessPercentage = 1 - (fTD.vitality * fTD.difficulty) / fTD.refVitality;
        if (fTD.vitalitySuccessPercentage > 1) fTD.vitalitySuccessPercentage = 1;
        if (fTD.vitalitySuccessPercentage < 0) fTD.vitalitySuccessPercentage = 0;

        //this part belongs to render
        hotBarMsg(fTD);
    }

    private void updateRandomVel(FishingTaskData fTD) {
        if (fTD.timer <= 0) {
            fTD.timer = (int) Math.ceil(fTD.r.nextDouble() * 20);
            Vector randomDirection = new Vector((fTD.r.nextDouble() - 0.5) * 2, 0, (fTD.r.nextDouble() - 0.5) * 2);
            if (randomDirection.getX() == 0 && randomDirection.getZ() == 0) {
                randomDirection = new Vector(1, 0, 0);
            } else {
                randomDirection = randomDirection.normalize();
            }
            double velocity = fTD.r.nextDouble() * fTD.vitality / (fTD.refVitality * fTD.difficulty) * fTD.difficulty * fTD.randomVelocity;
            fTD.randomVel = randomDirection.multiply(velocity);
        }
        fTD.hookVel = fTD.randomVel;
    }

    private Vector getRunAwayDirection(FishingTaskData fTD) {
        Vector runAwayDirection;
        double deltaX = fTD.hookLoc.getX() - fTD.pD.p.getLocation().getX();
        double deltaZ = fTD.hookLoc.getZ() - fTD.pD.p.getLocation().getZ();
        if (deltaX == 0 && deltaZ == 0) {
            runAwayDirection = new Vector(1, 0, 0).normalize();
        } else {
            runAwayDirection = new Vector(deltaX, 0, deltaZ).normalize();
        }
        return runAwayDirection;
    }

    private void hotBarMsg(FishingTaskData fTD) {

        int distanceIndex = (int) Math.floor(fTD.distanceSuccessPercentage * 16);
        if (distanceIndex > 15) distanceIndex = 15;
        if (distanceIndex < 0) distanceIndex = 0;
        String dS = ExtraStringListData.get("default_fishing_distance_status", distanceIndex);

        StringBuilder pressureBarString = new StringBuilder();
        for (double i = fTD.failPressure * fTD.difficulty; i < fTD.rD.maxPressure; i = i + 0.05) {
            if (i < fTD.pressure) {
                pressureBarString.append(ExtraStringListData.get("default_fishing_pressure_status", 1));
            } else {
                pressureBarString.append(ExtraStringListData.get("default_fishing_pressure_status", 0));
            }
        }
        String pS = pressureBarString.toString();

        int vitalityIndex = 0;
        if (fTD.vitality <= fTD.refVitality * fTD.difficulty * 1) vitalityIndex = 0;
        if (fTD.vitality <= fTD.refVitality * fTD.difficulty * 0.5) vitalityIndex = 1;
        if (fTD.vitality <= fTD.refVitality * fTD.difficulty * 0.2) vitalityIndex = 2;
        if (fTD.vitality <= 0) vitalityIndex = 3;
        String vS =  ExtraStringListData.get("default_fishing_vitality_status", vitalityIndex);
        try {
            String format = fTD.hotBarFormat.get(1);
            if (format.contains("%distance%")) format = format.replaceAll("%distance%", dS);
            if (format.contains("%pressure%")) format = format.replaceAll("%pressure%", pS);
            if (format.contains("%vitality%")) format = format.replaceAll("%vitality%", vS);
            fTD.pD.hotBarMsg = FormatHandler.format(format, false);
            fTD.pD.p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(fTD.pD.hotBarMsg));
        } catch (Exception ignored) {

        }
    }
}
