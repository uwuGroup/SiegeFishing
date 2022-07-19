package me.asakura_kukii.siegefishing.handler.item.gun;

import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.gun.fire.FiringHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.CarryingHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.ModdingHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.PosingHandler;
import me.asakura_kukii.siegefishing.handler.item.gun.util.ScopingHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.StateHandler;
import org.bukkit.inventory.ItemStack;

public class GunHandler {

    public static boolean triggerGun(GunData gD, ItemStack iS, int s, PlayerData pD, String state, int slotOfHand) {
        StateHandler.updatePlayerStateCacheWhenTriggering(pD, state);
        //firing handler
        if(state.matches("onFire") && !pD.stateCache.get("Carry") && !pD.stateCache.get("Reload")) {
            FiringHandler.initiateFiring(gD, iS, s, pD, state);
            return true;
        }
        //scoping handler
        if(state.matches("onScopeOn") && !pD.stateCache.get("Reload")) {
            ScopingHandler.toggleScoping(gD, iS, s, pD, state);
            return true;
        }
        if(state.matches("onScopeOff") && !pD.stateCache.get("Reload")) {
            ScopingHandler.toggleScoping(gD, iS, s, pD, state);
            return true;
        }
        //carrying handler
        if(state.matches("onCarryOn") && !pD.stateCache.get("Reload")) {
            CarryingHandler.toggleCarrying(gD, iS, s, pD, state);
            return true;
        }
        if(state.matches("onCarryOff") && !pD.stateCache.get("Reload")) {
            CarryingHandler.toggleCarrying(gD, iS, s, pD, state);
            return true;
        }
        if(state.matches("onRestore")) {
            PosingHandler.restoreGunPose(gD, iS, s, pD, state);
            return false;
        }
        if(state.matches("onInitiate")) {
            PosingHandler.initGunPose(gD, iS, s, pD, state);
            return false;
        }
        if(state.matches("onReload")) {
            if (!pD.stateCache.get("Reload")) {
                gD.rT.r.reload(gD, iS, s, pD);
            } else {
                gD.rT.r.breakReload(pD);
            }
            return true;
        }
        if(state.matches("onModify")) {
            ModdingHandler.initModding(gD, iS, s, pD, state);
            return true;
        }
        return false;
    }
}
