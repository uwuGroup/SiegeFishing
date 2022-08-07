package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SiegePlayerListener implements org.bukkit.event.Listener {
    public static HashMap<String, BukkitTask> holdDetectorForContinuousTrigger = new HashMap<>();
    public static List<UUID> inputBlockMap = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerHandler.loadPlayerData(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerHandler.unloadPlayerData(p);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        PlayerHandler.restorePlayerData(p);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //Checking PlayerData
        PlayerData pD = PlayerHandler.getPlayerData(p);

        try {
            if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                //interactLeftInput
                if (!inputBlockMap.contains(p.getUniqueId())) {
                    if (InputHandler.keyEvent(p, "InteractLeft", "On", false, false, false, -1)) {
                        e.setCancelled(true);
                    }
                }
            }
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                e.setCancelled(nonToggleEventHandler("InteractRight", (Entity) p, true, false, -1));
            }
        } catch (Error | Exception ignored) {
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        toggleEventHandler("Airborne", p, !((Entity) p).isOnGround(), false, false, -1);
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        Player p = (Player) e.getPlayer();
        toggleEventHandler("Sprint", p, p.isSprinting(), true, true, -1);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        toggleEventHandler("Sneak", p, p.isSneaking(), true, true, -1);
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        e.setCancelled(nonToggleEventHandler("Swap", p, true, false, -1));
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        e.setCancelled(nonToggleEventHandler("Restore", p, true, true, e.getPreviousSlot()));
        e.setCancelled(nonToggleEventHandler("Initiate", p, true, true, e.getNewSlot()));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        inputBlockMap.add(p.getUniqueId());
        UUID uuid = p.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                inputBlockMap.remove(uuid);
                cancel();
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 2, 1);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(nonToggleEventHandler("InteractRight", (Entity) p, true, false, -1));
    }

    public void toggleEventHandler(String s, Entity e, boolean state, boolean isInput, boolean noHold, int slotOfHand) {
        try {
            Player p = (Player) e;
            //Checking PlayerData
            PlayerData pD = PlayerHandler.getPlayerData(p);
            //UPDATING STATE
            pD.stateCache.remove(s);
            pD.stateCache.put(s, !state);
            if (isInput) {
                if (!state) {//check state on
                    InputHandler.keyEvent(p, s, "On", false, !noHold, false, slotOfHand);
                }
                if (state) {//check state off
                    InputHandler.keyEvent(p, s, "Off", false, false, false, slotOfHand);
                }
            }
        } catch (Exception ignored) {
        }

    }

    public boolean nonToggleEventHandler(String s, Entity e, boolean isInput, boolean noHold, int slotOfHand) {
        try {
            Player p = (Player) e;
            //Checking PlayerData
            PlayerData pD = PlayerHandler.getPlayerData(p);
            //UPDATING STATE

            boolean cancelEvent = false;

            if (isInput) {
                if (InputHandler.keyEvent(p, s, "Trigger", false, false, false, slotOfHand)) {
                    cancelEvent = true;
                }
            }

            if (!noHold) {
                //interactRightHoldFilter
                if (holdDetectorForContinuousTrigger.containsKey(p.getUniqueId() + s)) {
                    holdDetectorForContinuousTrigger.get(p.getUniqueId() + s).cancel();
                    holdDetectorForContinuousTrigger.remove(p.getUniqueId() + s);
                } else {
                    //interactRightInput
                    if (isInput) {
                        if (InputHandler.keyEvent(p, s, "On", false, true, true, slotOfHand)) {
                            cancelEvent = true;
                        }
                    }
                }
                pD.stateCache.remove(s);
                pD.stateCache.put(s,true);
                holdDetectorForContinuousTrigger.put(p.getUniqueId() + s, new BukkitRunnable() {
                    int runTime = 0;
                    @Override
                    public void run() {
                        if (runTime == 5) {
                            holdDetectorForContinuousTrigger.get(p.getUniqueId() + s).cancel();
                            holdDetectorForContinuousTrigger.remove(p.getUniqueId() + s);
                            pD.stateCache.remove(s);
                        }
                        runTime++;
                    }
                }.runTaskTimer(SiegeFishing.pluginInstance, 0, 1));
            }
            return cancelEvent;
        } catch (Exception ignored) {
        }
        return false;
    }
}

