package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class FloatHandler extends StageHandler{
    @Override
    public void update(FishingTaskData fTD) {
        //float motion
        fTD.hookAcc = new Vector(0,0,0);
        fTD.hookVel = new Vector(0, 0.04, 0);
        fTD.hookLoc = fTD.hookLoc.clone().add(fTD.hookVel);
        //out of water, next stage
        if (!fTD.hookLoc.getBlock().isLiquid() && !fTD.hookLoc.getBlock().getType().equals(Material.WATER)) {
            fTD.hookVel = new Vector(0, 0, 0);
            fTD.fTS = StageType.WAIT;
        }
    }
}
