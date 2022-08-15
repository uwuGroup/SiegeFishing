package me.asakura_kukii.siegefishing.handler.method.boat;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.EntityCollectorData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityContainerData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityShopData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.method.entity.TagEntityDataHandler;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.handler.player.SiegeMountHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.List;

public class BoatListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().getVehicle() != null) {
            if (e.getPlayer().getVehicle().getPassengers().get(0).getUniqueId().equals(e.getPlayer().getUniqueId())) {
                e.getPlayer().getVehicle().remove();
                PlayerData pD = PlayerDataHandler.getPlayerData(e.getPlayer());
                pD.backItemStackList.remove(1);
                SiegeMountHandler.communicate(pD);
            }
        }
    }

    @EventHandler
    public void onBoatDestroyEvent(VehicleDestroyEvent e) {
        if (e.getVehicle() instanceof Boat) {
            e.getVehicle().remove();
            if (e.getVehicle().getPassengers().size() != 0) {
                Entity e1 = e.getVehicle().getPassengers().get(0);
                if (e1 instanceof Player) {
                    e1.getVehicle().remove();
                    PlayerData pD = PlayerDataHandler.getPlayerData(((Player) e1));
                    pD.backItemStackList.remove(1);
                    SiegeMountHandler.communicate(pD);
                }
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeftBoat(VehicleExitEvent e) {
        LivingEntity entity = e.getExited();
        if (entity instanceof Player && e.getVehicle() instanceof Boat && e.getVehicle().getPassengers().get(0).getUniqueId().equals(entity.getUniqueId())) {
            if (FishingTaskData.fishingTaskMap.containsKey(((Player) entity).getUniqueId())) {
                e.setCancelled(true);
            } else {
                e.getVehicle().remove();
                PlayerData pD = PlayerDataHandler.getPlayerData(((Player) entity));
                pD.backItemStackList.remove(1);
                SiegeMountHandler.communicate(pD);
            }
        }
    }
}