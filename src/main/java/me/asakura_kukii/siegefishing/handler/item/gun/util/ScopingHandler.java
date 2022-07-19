package me.asakura_kukii.siegefishing.handler.item.gun.util;

import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PoseType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ScopingHandler {

    public static HashMap<UUID, ItemStack> scopingMap = new HashMap<>();

    public static void toggleScoping(GunData gD, ItemStack iS, int s, PlayerData pD, String state) {
        PosingHandler.updatePlayerPose(pD);

        if (pD.stateCache.get("Scope") && pD.stateCache.get("Carry")) {
            //sprint and then scope
            pD.poseState = PoseType.GUN_SCOPE;
            CarryingHandler.stopSprinting(pD);
        }
        if (pD.stateCache.get("Scope")) {
            initScoping(gD, iS, pD);
        } else {
            breakScoping(pD);
        }

        PosingHandler.updateGunPose(gD, iS, s, pD);
    }

    public static boolean checkScoping(PlayerData pD) {
        return scopingMap.containsKey(pD.p.getUniqueId());
    }

    public static void initScoping(GunData gD, ItemStack iS, PlayerData pD) {
        boolean overrideScope = false;
        boolean scopePotionEffect = gD.scopePotionEffect;
        int scopeZoomLevel = gD.scopeZoomLevel;
        boolean scopeNightVision = gD.scopeNightVision;
        double scopeSpeedCompensation = gD.scopeSpeedCompensation;

        try {
            String cPS = (String) NBTHandler.get(iS, "current", null);
            overrideScope = (boolean) NBTHandler.get(cPS, "oS", Boolean.class);
            if (overrideScope) {
                scopePotionEffect = (boolean) NBTHandler.get(cPS, "sPE", Boolean.class);
                scopeZoomLevel = (int) NBTHandler.get(cPS, "sZL", Integer.class);
                scopeNightVision = (boolean) NBTHandler.get(cPS, "sNV", Boolean.class);
                scopeSpeedCompensation = (double) NBTHandler.get(cPS, "sSC", Double.class);
            }
        } catch (Exception ignored) {
        }

        float scopeSpeed = (float) (scopeSpeedCompensation + 0.2);
        if (scopeSpeed <= -1) {
            scopeSpeed = -1;
        }
        if (scopeSpeed >= 1) {
            scopeSpeed = 1;
        }

        if (scopePotionEffect) {
            PotionEffect pES = new PotionEffect(PotionEffectType.SLOW, 100000000, scopeZoomLevel, false, false, false);
            PotionEffect pEN = new PotionEffect(PotionEffectType.NIGHT_VISION, 100000000, 1, false, false, false);
            pD.p.setWalkSpeed(scopeSpeed);
            if (scopeZoomLevel > 0) {
                pD.p.addPotionEffect(pES);
            }
            if (scopeNightVision) {
                pD.p.addPotionEffect(pEN);
            }
            scopingMap.put(pD.p.getUniqueId(), iS);
        }
    }

    public static void breakScoping(PlayerData pD) {
        if (scopingMap.containsKey(pD.p.getUniqueId())) {
            ItemStack iS = scopingMap.get(pD.p.getUniqueId());
            boolean overrideScope = false;
            boolean scopePotionEffect = false;
            int scopeZoomLevel = 0;
            boolean scopeNightVision = false;

            try {
                String cPS = (String) NBTHandler.get(iS, "current", null);
                overrideScope = (boolean) NBTHandler.get(cPS, "oS", Boolean.class);
                if (overrideScope) {
                    scopePotionEffect = (boolean) NBTHandler.get(cPS, "sPE", Boolean.class);
                    scopeZoomLevel = (int) NBTHandler.get(cPS, "sZL", Integer.class);
                    scopeNightVision = (boolean) NBTHandler.get(cPS, "sNV", Boolean.class);
                }
            } catch (Exception ignored) {
            }

            if (scopePotionEffect) {
                pD.p.setWalkSpeed(0.2f);
                if (Objects.requireNonNull(pD.p.getPotionEffect(PotionEffectType.SLOW)).getAmplifier() == scopeZoomLevel) {
                    pD.p.removePotionEffect(PotionEffectType.SLOW);
                }
                if (scopeNightVision) {
                    pD.p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
                scopingMap.remove(pD.p.getUniqueId());
            }
        } else {
            //force remove
            pD.p.setWalkSpeed(0.2f);
            pD.p.removePotionEffect(PotionEffectType.SLOW);
            pD.p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

        pD.stateCache.remove("Scope");
        pD.stateCache.put("Scope", false);
    }
}
