package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler {
    public static PlayerData loadPlayerData(Player p) {
        String fN = FileType.PLAYER_DATA.folder.getName() + "." + p.getUniqueId() + "+" + p.getName() + ".yml";
        PlayerData pD = (PlayerData) FileIO.loadOnDemand(p.getUniqueId().toString(), fN, FileType.PLAYER_DATA);
        pD.p = p;
        return pD;
    }

    public static void restorePlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        pD.stateCache.put("Scope", false);
        pD.stateCache.put("FullAuto", false);
        pD.stateCache.put("Reload", false);
    }

    public static void savePlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        FileIO.saveFile(pD, true);
    }

    public static void unloadPlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        savePlayerData(p);
        FileIO.unloadOnDemand((FileData) pD);
    }

    public static PlayerData getPlayerData(Player p) {
        if (!FileType.PLAYER_DATA.map.containsKey(p.getUniqueId().toString())) {
            loadPlayerData(p);
        }
        return (PlayerData) FileType.PLAYER_DATA.map.get(p.getUniqueId().toString());
    }
}
