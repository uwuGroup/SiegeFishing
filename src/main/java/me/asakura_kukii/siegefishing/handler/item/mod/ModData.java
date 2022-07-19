package me.asakura_kukii.siegefishing.handler.item.mod;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

public class ModData extends ItemData {
    public int modSlot;
    public int modCustomModelIndex;
    public int modCustomModelIndexMultiplier;

    public double damageMultiplier;
    public double horizontalRecoilMultiplier;
    public double verticalRecoilMultiplier;
    public boolean silenceGun;
    public double accurateTimeMultiplier;
    public double inaccuracyMultiplier;
    public double rangeMultiplier;

    public boolean overrideScope;
    public boolean scopePotionEffect;
    public int scopeZoomLevel;
    public boolean scopeNightVision;
    public double scopeSpeedCompensation;

    public ModData(String identifier, String fileName){
        this.identifier = identifier;
        this.fileName = fileName;
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level) {
        iS = NBTHandler.set(iS, "type", "mod", false);
        iS = NBTHandler.set(iS, "mS", this.modSlot, false);
        iS = NBTHandler.set(iS, "mCMI", this.modCustomModelIndex, false);
        iS = NBTHandler.set(iS, "mCMIM", this.modCustomModelIndexMultiplier, false);
        iS = NBTHandler.set(iS, "dM", this.damageMultiplier, false);
        iS = NBTHandler.set(iS, "hRM", this.horizontalRecoilMultiplier, false);
        iS = NBTHandler.set(iS, "vRM", this.verticalRecoilMultiplier, false);
        iS = NBTHandler.set(iS, "sG", this.silenceGun, false);
        iS = NBTHandler.set(iS, "aTM", this.accurateTimeMultiplier, false);
        iS = NBTHandler.set(iS, "iM", this.inaccuracyMultiplier, false);
        iS = NBTHandler.set(iS, "rM", this.rangeMultiplier, false);
        iS = NBTHandler.set(iS, "oS", this.overrideScope, false);
        iS = NBTHandler.set(iS, "sPE", this.scopePotionEffect, false);
        iS = NBTHandler.set(iS, "sZL", this.scopeZoomLevel, false);
        iS = NBTHandler.set(iS, "sNV", this.scopeNightVision, false);
        iS = NBTHandler.set(iS, "sSC", this.scopeSpeedCompensation, false);
        return iS;
    }

    public static ModData getData(ItemStack iS) {
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("mod")) {
                if (FileType.MOD.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (ModData) FileType.MOD.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
