package me.asakura_kukii.siegefishing.handler.player.input;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InputHandler {
    public static HashMap<String, List<String>> gunKeyMap = new HashMap<>();
    public static HashMap<String, List<String>> modKeyMap = new HashMap<>();
    public static HashMap<String, List<String>> handKeyMap = new HashMap<>();
    public static HashMap<String, List<String>> toolKeyMap = new HashMap<>();
    public static void keyMapper() {
        gunKeyMap.clear();
        gunKeyMap.put("onInteractRightOn", Arrays.asList("onScopeOn","onModify"));
        gunKeyMap.put("onSneakOn", Collections.singletonList("onFullAutoOn"));
        gunKeyMap.put("onSneakOff", Collections.singletonList("onFullAutoOff"));
        gunKeyMap.put("onInteractLeftOn", Collections.singletonList("onFire"));
        gunKeyMap.put("onSprintOn", Collections.singletonList("onCarryOn"));
        gunKeyMap.put("onSprintOff", Collections.singletonList("onCarryOff"));
        gunKeyMap.put("onDropTrigger", Collections.singletonList("onReload"));
        gunKeyMap.put("onViewInventoryOn", Collections.singletonList("onRestore"));
        gunKeyMap.put("onViewInventoryOff", Collections.singletonList("onInitiate"));
        gunKeyMap.put("onRestoreTrigger", Collections.singletonList("onRestore"));
        gunKeyMap.put("onInitiateTrigger", Collections.singletonList("onInitiate"));

        modKeyMap.clear();
        modKeyMap.put("onInteractRightOn", Collections.singletonList("onModify"));
        modKeyMap.put("onRestoreTrigger", Collections.singletonList("onRestore"));
        modKeyMap.put("onInitiateTrigger", Collections.singletonList("onInitiate"));

        handKeyMap.clear();
        handKeyMap.put("onRestoreTrigger", Collections.singletonList("onRestore"));
        handKeyMap.put("onInitiateTrigger", Collections.singletonList("onInitiate"));
        handKeyMap.put("onViewInventoryOn", Collections.singletonList("onRestore"));
        handKeyMap.put("onViewInventoryOff", Collections.singletonList("onInitiate"));

        handKeyMap.put("onInteractRightOn", Collections.singletonList("onUpdate"));
        handKeyMap.put("onSneakOn", Collections.singletonList("onUpdate"));
        handKeyMap.put("onSneakOff", Collections.singletonList("onUpdate"));
        handKeyMap.put("onSprintOn", Collections.singletonList("onUpdate"));
        handKeyMap.put("onSprintOff", Collections.singletonList("onUpdate"));
        handKeyMap.put("onDropTrigger", Collections.singletonList("onUpdate"));

        toolKeyMap.clear();
        toolKeyMap.put("onInteractRightOn", Collections.singletonList("onModify"));
        toolKeyMap.put("onRestoreTrigger", Collections.singletonList("onRestore"));
        toolKeyMap.put("onInitiateTrigger", Collections.singletonList("onInitiate"));
    }

    public static boolean keyEvent(int triggerSlot, InputKeyType iKT, Player p, InputSubType iST, boolean checkArmorSlot, boolean checkHold, boolean checkOff) {
        if (Bukkit.getOnlinePlayers().contains(p)) {
            boolean cancel = false;
            PlayerData pD = PlayerDataHandler.getPlayerData(p);
            for (int i = 41; i >= 36; i--) {
                if (!checkArmorSlot && i == 39) {
                    break;
                }
                ItemStack iS;
                int slot;
                if (i == 41) {
                    if (triggerSlot == -1 || triggerSlot > 40) {
                        triggerSlot = p.getInventory().getHeldItemSlot();
                    }
                    iS = p.getInventory().getItem(triggerSlot);
                    slot = triggerSlot;
                } else {
                    iS = p.getInventory().getItem(i);
                    slot = i;
                }
                if (iS == null) {
                    continue;
                }
                if (NBTHandler.hasPluginCompoundTag(iS) && ItemData.getItemData(iS) != null) {
                    ItemData iD = ItemData.getItemData(iS);
                    cancel = iD.trigger(slot, iKT, iST, pD, iS);
                }
            }
            //2nd round, update hand if there is any
            if (checkHold) {
                keyHoldChecker(triggerSlot, iKT, p, checkOff);
                PlayerDataHandler.getPlayerData(p).keyHoldRegister.remove(iKT.name());
                PlayerDataHandler.getPlayerData(p).keyHoldRegister.put(iKT.name(), 0);
            }
            return cancel;
        } else {
            return false;
        }
    }

    public static void keyHoldChecker(int triggerSlot, InputKeyType iKT, Player p, boolean checkOff) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().contains(p)) {

                    PlayerData pD = PlayerDataHandler.getPlayerData(p);
                    if (pD.stateCache.containsKey(iKT.name()) && pD.stateCache.get(iKT.name())) {
                        int holdTick = pD.keyHoldRegister.get(iKT.name());
                        pD.keyHoldRegister.remove(iKT.name());
                        holdTick ++;
                        pD.keyHoldRegister.put(iKT.name(), holdTick);
                        keyEvent(triggerSlot, iKT, p, InputSubType.HOLD,false, false, checkOff);
                    } else {
                        pD.keyHoldRegister.remove(iKT.name());
                        if (checkOff) {
                            keyEvent(triggerSlot, iKT, p, InputSubType.HOLD_END,false, false, false);
                        }
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 0, 1);
    }
}
