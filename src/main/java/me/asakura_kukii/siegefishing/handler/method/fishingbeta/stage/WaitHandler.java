package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.BaitData;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.FishRegionData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.random.WeightedRandomPick;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WaitHandler extends StageHandler{
    @Override
    public void update(FishingTaskData fTD) {
        //count timer
        if (!fTD.hookLoc.clone().add(new Vector(0, -0.5, 0)).getBlock().getType().equals(Material.WATER)) {
            fTD.fTS = StageType.THROWN;
        }
        fTD.timer++;

        fTD.hookVel = fTD.hookVel.multiply(0.8);
        fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);

        //send hot bar msg
        sendHotBarMsg(fTD);


        //check time
        ParticleHandler.spawnParticleAtLoc(fTD.hookLoc, (ParticleData) FileType.PARTICLE.map.get("default_splash_small"), false);
        if (fTD.timer < fTD.waitTime) {
            return;
        }

        fTD.pD.activeBaitData = new BaitData("", "", FileType.BAIT);

        //generate fish
        List<FishData> fishDataList = new ArrayList<>();
        List<Double> weightList = new ArrayList<>();
        List<Double> modifiedWeightList = new ArrayList<>();

        //foreach fish region to add fish percent map
        for (FileData fD : FileType.FISH_REGION.map.values()) {
            FishRegionData fRD = (FishRegionData) fD;
            double grayValue = fRD.regionValue.getRelativeValueAt(fTD.hookLoc.getChunk().getX(), fTD.hookLoc.getChunk().getZ());
            if (grayValue < 0.8) continue;
            fishDataList.addAll(fRD.percentMap.keySet());
            weightList.addAll(fRD.percentMap.values());
        }

        int index = 0;
        for (FishData fD : fishDataList) {
            double weight = weightList.get(index);
            if (fD.rarityLevel > 2) {
                weight = weight * (1 + fTD.totalLuckBoost);
            }
            modifiedWeightList.add(weight);
            index ++;
        }
        //random pick one using weight map
        WeightedRandomPick wRP = new WeightedRandomPick(modifiedWeightList);
        //initialize fish
        fTD.fD = fishDataList.get(wRP.getIndex());
        fTD.vitality = fTD.difficulty * fTD.refVitality;
        fTD.difficulty = fTD.fD.rarityLevel * fTD.levelDifficultyStep + fTD.difficultyMin;
        //reset timer, next stage
        fTD.timer = 0;
        fTD.fTS = StageType.HOOKED;
        if (fTD.pD.p.isInWater()) {
            fTD.dragged = true;
        }
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
