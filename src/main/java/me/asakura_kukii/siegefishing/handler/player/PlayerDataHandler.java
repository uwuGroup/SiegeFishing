package me.asakura_kukii.siegefishing.handler.player;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import org.bukkit.entity.Player;

public class PlayerDataHandler {
    public static PlayerData loadPlayerData(Player p) {
        String fN = FileType.PLAYER_DATA.folder.getName() + "." + p.getUniqueId() + "+" + p.getName() + ".yml";
        PlayerData pD = (PlayerData) FileIO.loadOnDemand(p.getUniqueId().toString(), fN, FileType.PLAYER_DATA);
        pD.p = p;
        pD.lastLocation = p.getLocation();
        if (!pD.fT.map.containsKey(pD.identifier)) {
            pD.fT.map.put(pD.identifier, pD);
            savePlayerData(p);
        }
        return pD;
    }

    public static void unloadPlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        savePlayerData(p);
        FileIO.unloadOnDemand((FileData) pD);
    }

    public static void savePlayerData(Player p) {
        PlayerData pD = getPlayerData(p);
        FileIO.saveFile(pD, true);
    }

    public static PlayerData getPlayerData(Player p) {
        if (!FileType.PLAYER_DATA.map.containsKey(p.getUniqueId().toString())) {
            loadPlayerData(p);
        }
        return (PlayerData) FileType.PLAYER_DATA.map.get(p.getUniqueId().toString());
    }
}
