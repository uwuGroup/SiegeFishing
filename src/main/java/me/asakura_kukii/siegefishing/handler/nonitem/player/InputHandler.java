package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
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

    public static boolean keyEvent(Player p, String key, String state, boolean checkArmorSlot, boolean checkHold, boolean checkOff, int slotOfHand) {
        if (Bukkit.getOnlinePlayers().contains(p)) {
            PlayerData pD = PlayerHandler.getPlayerData(p);
            String trigger = "on" + key + state;

            int handSlot = -1;

            boolean cancel = false;
            //1st round, calculate skill item
            for (int i = 41; i >= 36; i--) {
                if (!checkArmorSlot && i == 39) {
                    break;
                }
                ItemStack iS;
                int slot;

                if (i == 41) {
                    if (slotOfHand == -1 || slotOfHand > 40) {
                        slotOfHand = p.getInventory().getHeldItemSlot();
                    }
                    iS = p.getInventory().getItem(slotOfHand);
                    slot = slotOfHand;
                } else {
                    iS = p.getInventory().getItem(i);
                    slot = i;
                }

                if (iS == null) {
                    continue;
                }
                ItemStack clonedIS = iS.clone();
                if (NBTHandler.hasPluginCompoundTag(iS)) {
                    p.sendMessage(trigger + iS.getItemMeta().getDisplayName());
                }
            }
            //2nd round, update hand if there is any
            if (checkHold) {
                keyHoldChecker(key, p, checkOff, slotOfHand);
                PlayerHandler.getPlayerData(p).keyHoldRegister.remove(key);
                PlayerHandler.getPlayerData(p).keyHoldRegister.put(key, 0);
            }
            return cancel;
        } else {
            return false;
        }
    }

    public static void keyHoldChecker(String key, Player p, boolean checkOff, int slotOfHand) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().contains(p)) {
                    if (PlayerHandler.getPlayerData(p).stateCache.containsKey(key) && PlayerHandler.getPlayerData(p).stateCache.get(key)) {
                        int holdTick = PlayerHandler.getPlayerData(p).keyHoldRegister.get(key);
                        PlayerHandler.getPlayerData(p).keyHoldRegister.remove(key);
                        holdTick ++;
                        PlayerHandler.getPlayerData(p).keyHoldRegister.put(key,holdTick);
                        keyEvent(p, key, "Hold",false, false, checkOff, slotOfHand);
                    } else {
                        PlayerHandler.getPlayerData(p).keyHoldRegister.remove(key);
                        if (checkOff) {
                            keyEvent(p, key, "Off",false, false, false, slotOfHand);
                        }
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(SiegeFishing.pluginInstance, 0, 1);
    }
}
