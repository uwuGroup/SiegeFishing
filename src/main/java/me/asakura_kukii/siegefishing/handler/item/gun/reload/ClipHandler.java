package me.asakura_kukii.siegefishing.handler.item.gun.reload;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.util.PosingHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.ScopingHandler;
import me.asakura_kukii.siegefishing.handler.item.hand.HandHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PoseType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ClipHandler extends Reload {
    public static HashMap<UUID, Integer> clipReloadSlotMap = new HashMap<>();
    public static HashMap<UUID, GunData> clipReloadGunDataMap = new HashMap<>();
    public static HashMap<UUID, ItemStack> clipReloadItemStackMap = new HashMap<>();

    public void reload(GunData gD, ItemStack iS, int s, PlayerData pD) {
        if (pD.gunReserve != -1) {
            iS = GunData.setReserve(gD, iS, pD.gunReserve);
            pD.gunReserve = -1;
        }

        int totalTicks = (int) Math.ceil(gD.reloadDelay * 20);
        pD.poseState = PoseType.GUN_HOLD;
        pD.stateCache.put("Reload", true);
        if (pD.stateCache.get("Scope")) {
            ScopingHandler.breakScoping(pD);
        }
        pD.stateCache.remove("Scope");
        pD.stateCache.put("Scope", false);

        ItemStack finalIS = PosingHandler.updateGunPose(gD, iS, s, pD);
        HandHandler.safeUpdateHand(pD);
        clipReloadGunDataMap.put(pD.uuid, gD);
        clipReloadItemStackMap.put(pD.uuid, iS);
        clipReloadSlotMap.put(pD.uuid, s);
        Reload.reloadingMap.put(pD.uuid, new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= totalTicks) {
                    pD.stateCache.put("Reload", false);
                    ItemStack mIS = GunData.setReserve(gD, finalIS, gD.bulletCount);
                    PosingHandler.updatePlayerPose(pD);
                    PosingHandler.updateGunPose(gD, mIS, s, pD);
                    HandHandler.safeUpdateHand(pD);
                    Reload.reloadingMap.remove(pD.uuid).cancel();
                }
                ticks++;
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 0, 1));
    }
    //StateHandler.updateGunPoseIndexToHandItemStack(pD.p.getInventory().getHeldItemSlot(), pD);
    public void breakReload(PlayerData pD) {
        Reload.reloadingMap.remove(pD.uuid).cancel();
        PosingHandler.updatePlayerPose(pD);
        PosingHandler.updateGunPose(clipReloadGunDataMap.get(pD.uuid), clipReloadItemStackMap.get(pD.uuid), clipReloadSlotMap.get(pD.uuid), pD);
        //StateHandler.updateGunPoseIndexToHandItemStack(pD.p.getInventory().getHeldItemSlot(), pD);
        clipReloadGunDataMap.remove(pD.uuid);
        clipReloadItemStackMap.remove(pD.uuid);
        clipReloadSlotMap.remove(pD.uuid);
        pD.stateCache.put("Reload", false);
    }
}