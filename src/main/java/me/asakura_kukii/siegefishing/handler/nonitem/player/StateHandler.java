//Methods dealing with state changes, including the pose of gun and hand

package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;

public class StateHandler {
    public static boolean checkGunHandState(PlayerData pD) {
        return GunData.getData(pD.p.getInventory().getItemInOffHand()) != null && HandData.getData(pD.p.getInventory().getItemInMainHand()) != null;
    }

    public static void mapStateCache(PlayerData pD, String state) {
        if(state.matches("onCarryOn")) {
            boolean carry = pD.stateCache.get("Carry");
            pD.stateCache.remove("Carry");
            pD.stateCache.put("Carry", !carry);
        }
        if(state.matches("onCarryOff")) {
            pD.stateCache.remove("Carry");
            pD.stateCache.put("Carry", false);
        }

        if(state.matches("onFullAutoOn")) {
            boolean carry = pD.stateCache.get("FullAuto");
            pD.stateCache.remove("FullAuto");
            pD.stateCache.put("FullAuto", !carry);
        }
        if(state.matches("onFullAutoOff")) {
            pD.stateCache.remove("FullAuto");
            pD.stateCache.put("FullAuto", false);
        }
    }

    public static void updatePlayerStateCacheWhenTriggering(PlayerData pD, String state) {
        if(state.matches("onScopeOn") && !pD.stateCache.get("Reload")) {
            boolean scope = pD.stateCache.get("Scope");
            pD.stateCache.remove("Scope");
            pD.stateCache.put("Scope", !scope);
        }
        if(state.matches("onScopeOff")) {
            pD.stateCache.remove("Scope");
            pD.stateCache.put("Scope", false);
        }
    }
}
