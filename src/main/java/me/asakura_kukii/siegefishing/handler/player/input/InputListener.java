package me.asakura_kukii.siegefishing.handler.player.input;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InputListener implements org.bukkit.event.Listener {
    public static HashMap<String, BukkitTask> holdDetectorForContinuousTrigger = new HashMap<>();
    public static List<UUID> inputBlockMap = new ArrayList<>();
    public static HashMap<UUID, Integer> itemPickUpMap = new HashMap<>();

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        e.setCancelled(nonToggleInput(-1, InputKeyType.ITEM_CONSUME, p));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //Checking PlayerData
        PlayerData pD = PlayerDataHandler.getPlayerData(p);

        try {
            if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                //interactLeftInput
                if (!inputBlockMap.contains(p.getUniqueId())) {
                    e.setCancelled(nonToggleInput(-1, InputKeyType.ITEM_LEFT_CLICK, (Entity) p));
                }
            }
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(nonToggleInput(-1, InputKeyType.ITEM_RIGHT_CLICK, (Entity) p));
            }
        } catch (Error | Exception ignored) {
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        toggleInput(-1, InputKeyType.AIRBORNE, p, !((Entity) p).isOnGround());
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        Player p = (Player) e.getPlayer();
        toggleInput(-1, InputKeyType.SPRINT, p, p.isSprinting());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        toggleInput(-1, InputKeyType.SNEAK, p, p.isSneaking());
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        e.setCancelled(nonToggleInput(-1, InputKeyType.ITEM_SWAP, p));
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        e.setCancelled(nonToggleInput(e.getPreviousSlot(), InputKeyType.ITEM_FINALIZE, p));
        e.setCancelled(nonToggleInput(e.getNewSlot(), InputKeyType.ITEM_INITIATE, p));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
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

    public void toggleInput(int triggerSlot, InputKeyType iKT, Entity e, boolean state) {
        try {
            Player p = (Player) e;
            //Checking PlayerData
            PlayerData pD = PlayerDataHandler.getPlayerData(p);
            //UPDATING STATE
            pD.stateCache.remove(iKT.name());
            pD.stateCache.put(iKT.name(), !state);
            if (iKT.doInput) {
                if (!state) {//check state on
                    InputHandler.keyEvent(triggerSlot, iKT, p, InputSubType.TOGGLE_ON, false, iKT.checkHold, false);
                }
                if (state) {//check state off
                    InputHandler.keyEvent(triggerSlot, iKT, p, InputSubType.TOGGLE_OFF, false, false, false);
                }
            }
        } catch (Exception ignored) {
        }

    }

    public boolean nonToggleInput(int triggerSlot, InputKeyType iKT, Entity e) {
        try {
            Player p = (Player) e;
            //Checking PlayerData
            PlayerData pD = PlayerDataHandler.getPlayerData(p);
            //UPDATING STATE

            boolean cancelEvent = false;

            if (iKT.doInput && !holdDetectorForContinuousTrigger.containsKey(p.getUniqueId() + iKT.name())) {
                if (InputHandler.keyEvent(triggerSlot, iKT, p, InputSubType.TRIGGER, false, false, false)) {
                    cancelEvent = true;
                }
            }

            if (iKT.checkHold) {
                //interactRightHoldFilter
                if (holdDetectorForContinuousTrigger.containsKey(p.getUniqueId() + iKT.name())) {
                    holdDetectorForContinuousTrigger.get(p.getUniqueId() + iKT.name()).cancel();
                    holdDetectorForContinuousTrigger.remove(p.getUniqueId() + iKT.name());
                } else {
                    //interactRightInput
                    if (iKT.doInput) {
                        if (InputHandler.keyEvent(triggerSlot, iKT, p, InputSubType.HOLD_ON, false, true, true)) {
                            cancelEvent = true;
                        }
                    }
                }
                pD.stateCache.remove(iKT.name());
                pD.stateCache.put(iKT.name(), true);
                holdDetectorForContinuousTrigger.put(p.getUniqueId() + iKT.name(), new BukkitRunnable() {
                    int runTime = 0;
                    @Override
                    public void run() {
                        if (runTime == 5) {
                            holdDetectorForContinuousTrigger.get(p.getUniqueId() + iKT.name()).cancel();
                            holdDetectorForContinuousTrigger.remove(p.getUniqueId() + iKT.name());
                            pD.stateCache.remove(iKT.name());
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
