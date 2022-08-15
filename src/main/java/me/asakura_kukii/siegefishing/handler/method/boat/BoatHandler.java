package me.asakura_kukii.siegefishing.handler.method.boat;

import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.BoatData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.SiegeMountHandler;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

public class BoatHandler {
    public static void generateBoat(PlayerData pD, BoatData bD) {
        if (pD.p.getVehicle() != null) return;
        RayTraceResult rTR = pD.p.rayTraceBlocks(6, FluidCollisionMode.ALWAYS);
        if (rTR != null) {
            if (rTR.getHitBlock().isLiquid() && rTR.getHitBlock().getType().equals(Material.WATER)) {
                Location l = rTR.getHitPosition().toLocation(pD.p.getWorld());
                l.setDirection(pD.p.getLocation().getDirection());
                l.setPitch(pD.p.getLocation().getPitch());
                l.setYaw(pD.p.getLocation().getYaw());
                Boat boat = (Boat) pD.p.getWorld().spawnEntity(l, EntityType.BOAT, true);
                boat.setWoodType(TreeSpecies.ACACIA);
                boat.addPassenger(pD.p);
                ItemStack iS = ItemData.getItemStack(bD, pD, 1, 0);
                ItemMeta iM = iS.getItemMeta();
                assert iM != null;
                iM.setCustomModelData(bD.boatCustomModelData);
                iS.setItemMeta(iM);

                pD.backItemStackList.remove(1);
                pD.backItemStackList.put(1, iS);
                SiegeMountHandler.communicate(pD);
            }
        }
    }
}
