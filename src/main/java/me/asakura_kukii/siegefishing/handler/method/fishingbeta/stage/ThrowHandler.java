package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ThrowHandler extends StageHandler{
    @Override
    public void update(FishingTaskData fTD) {
        fTD.timer++;
        //if in water, next stage
        if (fTD.hookLoc.getBlock().isLiquid() && fTD.hookLoc.getBlock().getType().equals(Material.WATER)) {
            fTD.timer = 0;
            fTD.fTS = StageType.FLOAT;
            SoundHandler.playSoundAtLoc(fTD.hookLoc, (SoundData) FileType.SOUND.map.get("default_touch_water"));
            ParticleHandler.spawnParticleAtLoc(fTD.hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_splash"), false);
            ParticleHandler.spawnParticleAtLoc(fTD.hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_splash_white"), false);
            ParticleHandler.spawnParticleAtLoc(fTD.hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_splash_blue"), false);
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
            Vector f = d.clone().multiply(-1).multiply(0.4);
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

        //show hotbar
        sendHotBarMsg(fTD);
    }


    public void sendHotBarMsg(FishingTaskData fTD) {
        String format = fTD.hotBarFormat.get(0);
        if (format.contains("%bait_identifier%")) {
            if (fTD.pD.activeBaitData.identifier.equalsIgnoreCase("")) {
                format = "";
            } else {
                format = format.replaceAll("%bait_identifier%", fTD.pD.activeBaitData.identifier);
            }
        }
        if (format.contains("%bait_time_boost%")) {
            format = format.replaceAll("%bait_time_boost%", fTD.pD.activeBaitData.timeBoost + "");
        }
        if (format.contains("%bait_luck_boost%")) {
            format = format.replaceAll("%bait_luck_boost%", fTD.pD.activeBaitData.luckBoost + "");
        }
        fTD.pD.p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(FormatHandler.format(format, false)));
    }
}
