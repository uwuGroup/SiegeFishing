package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import org.bukkit.inventory.ItemStack;

public class RodData extends ItemData {
    public double avgWaitTime = 600;
    public double luckBoost = 0;
    public double maxPressure = 20;
    public double maxSwingDistance = 9;
    public double swingVelocity = 0.4;
    public double rodEndBiasX = 0;
    public double rodEndBiasY = 0;
    public double rodEndBiasZ = 0;
    public ParticleData stringParticleData = new ParticleData("", "", FileType.PARTICLE);
    public ParticleData hookParticleData = new ParticleData("", "", FileType.PARTICLE);

    public RodData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int level) {
        return iS;
    }

    @Override
    public boolean trigger(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        boolean cancel = false;
        switch (iKT) {
            case ITEM_LEFT_CLICK:
                if (iST.equals(InputSubType.TRIGGER)) cancel = handleLeftClick(triggerSlot, iKT, iST, pD, iS);
                break;
            case ITEM_RIGHT_CLICK:
                if (iST.equals(InputSubType.HOLD)) cancel = handleRightClick(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleLeftClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        if (FishingTaskData.fishingTaskMap.containsKey(pD.p.getUniqueId())) {
            FishingTaskData fTD = FishingTaskData.fishingTaskMap.get(pD.p.getUniqueId());
            fTD.kill();
        } else {
            FishingTaskData fTD = new FishingTaskData(pD, this, iS);
        }
        return true;
    }

    public boolean handleRightClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        if (FishingTaskData.fishingTaskMap.containsKey(pD.p.getUniqueId())) {
            FishingTaskData fTD = FishingTaskData.fishingTaskMap.get(pD.p.getUniqueId());
            if (pD.p.isSneaking()) {
                fTD.operateString(true);
            } else {
                fTD.operateString(false);
            }
        }
        return true;
    }
}
