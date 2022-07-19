package me.asakura_kukii.siegefishing.handler.item.gun;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.item.gun.reload.ReloadType;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialType;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import me.asakura_kukii.siegefishing.handler.item.mod.ModHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GunData extends ItemData {




    //information
    public Vector3D muzzleNormalBias;
    public Vector3D muzzleScopeBias;
    public List<SoundData> soundList;
    public List<ParticleData> muzzleParticleDataList;
    public int muzzleParticleDuration;

    //firing
    public int bulletCount;
    public int bulletCountPerShot;
    public double bulletSpread;
    public double semiAutoFireDelay;//the min time for another bullet to be fired in semi-auto;
    public double accurateTime1;//accurate time #1 designed spread;
    public double accurateTime2;//accurate time #2 perfectly accurate;
    public double inaccuracy0;//inaccuracy of time 0;
    public double inaccuracy1;//inaccuracy of time 1;
    public boolean stepBack;
    public boolean stepReturn;
    public double stepBackFactor;
    public double stepReturnFactor;
    public boolean fullAuto;
    public double fullAutoFireDelay;//the time delay for each shot fired continuously;
    public double inaccuracyStep;//step value;
    public double inaccuracyMax;
    public List<Vector3D> recoilCurve;//recoil curve;

    //mod
    public int modSlotCount;
    public boolean scopePotionEffect;
    public int scopeZoomLevel;
    public boolean scopeNightVision;
    public double scopeSpeedCompensation;
    public ReloadType rT;
    public double reloadDelay;

    //dynamic variables
    public HashMap<UUID, Long> lastShotTimer = new HashMap<>();
    public MethodNodeData mND;

    public GunData(String identifier, String fileName){
        this.identifier = identifier;
        this.fileName = fileName;
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level) {

        List<String> potentialStringList = PotentialHandler.generatePotential(level, PotentialType.WEAPON);
        iS = PotentialHandler.savePotentialToNBT(iS, potentialStringList, PotentialType.WEAPON);

        ItemMeta iM = iS.getItemMeta();
        assert iM != null;
        iM.setCustomModelData(this.customModelIndex * 81);
        iS.setItemMeta(iM);






        iS = NBTHandler.set(iS, "type", "gun", false);
        iS = NBTHandler.set(iS, "reserve", 0, false);
        iS = ModHandler.calculateCurrentData((GunData) this, iS);

        return iS;
    }

    public static GunData getData(ItemStack iS) {
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("gun")) {
                if (FileType.GUN.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (GunData) FileType.GUN.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }

    public static int getReserve(ItemStack iS) {
        return (int) NBTHandler.get(iS, "reserve", Integer.class);
    }

    public static ItemStack setReserve(GunData gD, ItemStack iS, int bulletResidualCount) {
        return NBTHandler.set(iS, "reserve", bulletResidualCount, false);
    }
}
