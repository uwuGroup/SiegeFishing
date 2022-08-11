package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.FishRegionData;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.utility.random.WeightedRandomPick;

import java.util.ArrayList;
import java.util.List;

public class WaitHandler extends StageHandler{
    @Override
    public void update(FishingTaskData fTD) {
        //count timer
        fTD.timer++;
        //check time
        if (fTD.timer < fTD.waitTime) {
            return;
        }
        //generate fish
        List<FishData> fishDataList = new ArrayList<>();
        List<Double> weightList = new ArrayList<>();
        //foreach fish region to add fish percent map
        for (FileData fD : FileType.FISH_REGION.map.values()) {
            FishRegionData fRD = (FishRegionData) fD;
            double grayValue = fRD.regionValue.getRelativeValueAt(fTD.hookLoc.getChunk().getX(), fTD.hookLoc.getChunk().getZ());
            if (grayValue < 0.8) continue;
            fishDataList.addAll(fRD.percentMap.keySet());
            weightList.addAll(fRD.percentMap.values());
        }
        //random pick one using weight map
        WeightedRandomPick wRP = new WeightedRandomPick(weightList);
        //initialize fish
        fTD.fD = fishDataList.get(wRP.getIndex());
        fTD.vitality = fTD.fD.difficulty * fTD.refVitality;
        //reset timer, next stage
        fTD.timer = 0;
        fTD.fTS = StageType.HOOKED;
    }
}
