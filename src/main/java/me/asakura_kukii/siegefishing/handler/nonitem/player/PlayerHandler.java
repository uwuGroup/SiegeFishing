package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.loader.PlayerIO;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler {
    public static HashMap<UUID, PlayerData> playerDataMapper = new HashMap<>();

    public static void PlayerDataCreator(Player p) {
        if (!PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
            PlayerData pD = new PlayerData(p.getUniqueId());
            pD.p = p;
            pD.stateCache = new HashMap<>();
            pD.stateCache.put("Sneak", false);
            pD.stateCache.put("Sprint", false);
            pD.stateCache.put("Airborne", false);
            pD.stateCache.put("Scope", false);
            pD.stateCache.put("Carry", false);
            pD.stateCache.put("FullAuto", false);
            pD.stateCache.put("Reload", false);
            PlayerHandler.playerDataMapper.put(p.getUniqueId(), pD);
        }
    }

    public static void restorePlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        pD.stateCache.put("Scope", false);
        pD.stateCache.put("FullAuto", false);
        pD.stateCache.put("Reload", false);
    }

    public static void PlayerDataDestroyer(Player p) {
        PlayerHandler.playerDataMapper.remove(p.getUniqueId());
    }

    public static PlayerData getPlayerData(Player p) {
        if (!PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
            PlayerIO.Load(p);
        }
        return PlayerHandler.playerDataMapper.get(p.getUniqueId());
    }
}
