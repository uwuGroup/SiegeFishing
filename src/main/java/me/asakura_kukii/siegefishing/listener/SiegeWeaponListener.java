package me.asakura_kukii.siegefishing.listener;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.hand.HandHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.handler.item.mod.ModData;
import me.asakura_kukii.siegefishing.handler.item.gun.fire.FiringHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.fire.FullAutoHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.PosingHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.ScopingHandler;
import me.asakura_kukii.siegefishing.handler.item.tool.ToolHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.StateHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.reload.Reload;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SiegeWeaponListener implements org.bukkit.event.Listener {
    public static HashMap<String, BukkitTask> holdDetectorForContinuousTrigger = new HashMap<>();
    public static List<UUID> inputBlockMap = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Checking PlayerData
        PlayerData pD = PlayerHandler.getPlayerData(e.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        /*if(e.getEntity() instanceof Player && e.getDamage() >= ((Player) e.getEntity()).getHealth()) {
            Player killer = null;
            if (e instanceof EntityDamageByEntityEvent) {
                Entity en = ((EntityDamageByEntityEvent) e).getDamager();
                if (en instanceof Player) {
                    Player p = (Player) en;
                    killer = p;
                    if() {

                    }
                }
            }*/
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        //Checking PlayerData
        PlayerData pD = PlayerHandler.getPlayerData(p);

        if (FullAutoHandler.checkFullAutoFiring(pD)) {
            FullAutoHandler.breakUpdateFullAutoFiring(pD);
        }
        if (Reload.checkReloading(pD)) {
            Reload.breakReloading(pD);
        }
        if (ScopingHandler.checkScoping(pD)) {
            ScopingHandler.breakScoping(pD);
        }
        if (FiringHandler.checkReserveCache(pD)) {
            FiringHandler.updateReserveToItemStack(pD);
        }
        if (StateHandler.checkGunHandState(pD)) {
            PosingHandler.resetGunPose(pD);
        }
        PlayerHandler.PlayerDataDestroyer(p);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData pD = PlayerHandler.getPlayerData(p);

        if (FullAutoHandler.checkFullAutoFiring(pD)) {
            FullAutoHandler.breakUpdateFullAutoFiring(pD);
        }
        if (Reload.checkReloading(pD)) {
            Reload.breakReloading(pD);
        }
        if (ScopingHandler.checkScoping(pD)) {
            ScopingHandler.breakScoping(pD);
        }
        if (FiringHandler.checkReserveCache(pD)) {
            FiringHandler.updateReserveToItemStack(pD);
        }
        if (StateHandler.checkGunHandState(pD)) {
            PosingHandler.resetGunPose(pD);
        }
        PlayerHandler.restorePlayerData(p);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //Checking PlayerData
        PlayerData pD = PlayerHandler.getPlayerData(e.getPlayer());

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
    public void onGlide(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            toggleEventHandler("Glide", p, p.isGliding(), false, true, -1);
        }
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

        if (HandData.getData(p.getInventory().getItemInMainHand()) != null || ModData.getData(p.getInventory().getItemInMainHand()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            PlayerData pD = PlayerHandler.getPlayerData(p);
            p.sendMessage(e.getAction().name());
            if (e.getClickedInventory()!=null) {
                if (Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.CREATIVE || Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.CRAFTING || Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.PLAYER) {

                    p.sendMessage(e.getCurrentItem().toString());
                    p.sendMessage(e.getCursor().toString());
                    p.sendMessage(e.getResult().toString());
                    p.sendMessage(e.getRawSlot() + "");
                    p.sendMessage(e.getHotbarButton() + "");
                    if (HandHandler.checkHand(pD, p.getInventory().getHeldItemSlot()) == 2 || HandHandler.checkHand(pD, 40) == 1) {
                        if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
                            if (e.getHotbarButton() == p.getInventory().getHeldItemSlot() || e.getHotbarButton() == -1 || e.getSlot() == p.getInventory().getHeldItemSlot() || e.getSlot() == 40) {
                                e.setCancelled(true);
                            }
                        } else {
                            if (e.getSlot() == p.getInventory().getHeldItemSlot() || e.getSlot() == 40) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        //Cancel if firing
        if (FullAutoHandler.checkFullAutoFiring(pD)) {
            e.setCancelled(true);
            return;
        }
        //Break reload
        if (Reload.checkReloading(pD)) {
            Reload.breakReloading(pD);
        }
        e.setCancelled(nonToggleEventHandler("Restore", p, true, true, e.getPreviousSlot()));
        e.setCancelled(nonToggleEventHandler("Initiate", p, true, true, e.getNewSlot()));
    }

    @EventHandler
    public void onManipulateArmorStand(PlayerArmorStandManipulateEvent e) {
        Player p = (Player) e.getPlayer();
        PlayerData pD = PlayerHandler.getPlayerData(p);
        ItemStack iS = e.getArmorStandItem();
        if (GunData.getData(e.getArmorStandItem()) != null) {
            GunData gD = GunData.getData(e.getArmorStandItem());
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                if (pD.stateCache.containsKey("Sneak") && !pD.stateCache.get("Sneak")) {
                    ToolHandler.removeAllMod(gD, pD, iS, e.getRightClicked());
                } else {
                    PlayerHandler.restorePlayerData(p);
                    //TODO: init
                    //PosingHandler.initGunPose(pD, e.getArmorStandItem(), p.getInventory().getHeldItemSlot());
                    e.getRightClicked().getEquipment().setItem(e.getSlot(), new ItemStack(Material.AIR));
                }
            }
            //Init next gun
            e.setCancelled(true);
        }
    }

    @EventHandler
    //TODO: FIX PICK UP
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            //Checking PlayerData
            PlayerData pD = PlayerHandler.getPlayerData(p);
            if (HandData.getData(e.getItem().getItemStack()) != null) {
                e.getItem().setItemStack(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        //Checking PlayerData
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

        p.sendMessage(e.getPlayer().getInventory().getItemInMainHand().toString());



        //TODO:COULD FAIL SYSTEM
        if (HandData.getData(e.getPlayer().getInventory().getItemInMainHand()) != null || HandData.getData(e.getItemDrop().getItemStack()) != null  || GunData.getData(e.getPlayer().getInventory().getItemInOffHand()) != null) {
            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), e.getItemDrop().getItemStack());
            if (FullAutoHandler.checkFullAutoFiring(pD)) {
                FullAutoHandler.breakUpdateFullAutoFiring(pD);
            }
            nonToggleEventHandler("Drop", p, true, true, -1);
            e.getItemDrop().remove();
            //StateHandler.updateGunPoseIndexToHandItemStack(p.getInventory().getHeldItemSlot(), pD);
        }

    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(nonToggleEventHandler("InteractRight", (Entity) p, true, false, -1));

        if (HandData.getData(p.getInventory().getItemInMainHand()) != null || ModData.getData(p.getInventory().getItemInMainHand()) != null) {
            e.setCancelled(true);
        }
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

