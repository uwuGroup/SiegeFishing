package me.asakura_kukii.siegefishing.utility.inventory;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.data.common.ItemData;
import me.asakura_kukii.siegefishing.handler.fishing.FishHookHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (SiegeInventory.openingInventoryMap.containsKey(p.getUniqueId())) {
            ItemStack iS = e.getCurrentItem();
            SiegeInventory sI = SiegeInventory.openingInventoryMap.get(p.getUniqueId());
            if (NBTHandler.hasPluginCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
                if (NBTHandler.get(iS, "type", String.class) == "button") {
                    if (NBTHandler.get(iS, "id", String.class) == "last") {
                        sI.lastPage();
                    } else {
                        sI.nextPage();
                    }
                    sI.showInventoryToPlayer(p);
                    e.setCancelled(true);
                }
            }
            e.setCancelled(!sI.movable);
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
            SiegeInventory.openingInventoryMap.remove(p.getUniqueId());
        }
    }
}

