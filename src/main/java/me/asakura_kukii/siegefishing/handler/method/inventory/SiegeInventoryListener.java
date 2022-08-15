package me.asakura_kukii.siegefishing.handler.method.inventory;

import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class SiegeInventoryListener implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
    }



    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        p.closeInventory();
        SiegeInventory.openingInventoryMap.remove(p.getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.closeInventory();
        SiegeInventory.openingInventoryMap.remove(p.getUniqueId());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (SiegeInventory.openingInventoryMap.containsKey(p.getUniqueId())) {
            SiegeInventory sI = SiegeInventory.openingInventoryMap.get(p.getUniqueId());
            boolean cancel = false;
            for (int i : e.getInventorySlots()) {
                if (!sI.movableSlot.contains(i)) cancel = true;
            }
            sI.interact(null, null);
            e.setCancelled(cancel);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (SiegeInventory.openingInventoryMap.containsKey(p.getUniqueId())) {
            SiegeInventory sI = SiegeInventory.openingInventoryMap.get(p.getUniqueId());
            sI.interact(e.getClick(), e.getSlot());
            if (!sI.movableSlot.contains(e.getSlot())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getPlayer();
        if (SiegeInventory.changingInventory.contains(p.getUniqueId())) {
            SiegeInventory.changingInventory.remove(p.getUniqueId());
        } else {
            if (SiegeInventory.openingInventoryMap.containsKey(p.getUniqueId())) {
                SiegeInventory sI = SiegeInventory.openingInventoryMap.get(p.getUniqueId());
                sI.close();
            }
            SiegeInventory.openingInventoryMap.remove(p.getUniqueId());
        }
    }
}

