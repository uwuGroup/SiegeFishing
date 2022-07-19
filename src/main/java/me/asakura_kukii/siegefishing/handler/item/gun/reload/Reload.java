package me.asakura_kukii.siegefishing.handler.item.gun.reload;

import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public abstract class Reload {
    public static HashMap<UUID, BukkitTask> reloadingMap = new HashMap<UUID, BukkitTask>();

    public static boolean checkReloading(PlayerData pD) {
        return reloadingMap.containsKey(pD.p.getUniqueId());
    }

    public static void breakReloading(PlayerData pD) {
        if (GunData.getData(pD.p.getInventory().getItemInOffHand()) != null) {
            GunData gD = GunData.getData(pD.p.getInventory().getItemInOffHand());
            if (Reload.checkReloading(pD)) {
                gD.rT.r.breakReload(pD);
            }
        }
    }

    public static Reload getReloadingHandler(String s) {
        if (s.matches("clip")) {
            return new ClipHandler();
        }

        if (s.matches("pump")) {
            return new PumpHandler();
        }

        if (s.matches("bolt")) {
            return new BoltHandler();
        }
        return null;
    }

    public abstract void reload(GunData gD, ItemStack iS, int s, PlayerData pD);

    public abstract void breakReload(PlayerData pD);
}
