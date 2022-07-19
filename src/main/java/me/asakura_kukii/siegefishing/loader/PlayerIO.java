package me.asakura_kukii.siegefishing.loader;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.colorcode.ColorCode;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.loader.common.FileHandler;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerIO {

    public static void LoadAll() {
        PlayerHandler.playerDataMapper.clear();
        for(Player p : Bukkit.getOnlinePlayers()) {
            Load(p);
        }
    }

    public static void Load(Player p) {
        PlayerHandler.PlayerDataCreator(p);
        FileHandler fIO;
        if (!new File(SiegeFishing.playerDataFolder, p.getName() + p.getUniqueId() + ".yml").exists()) {
            updateExtraPlayerData(p);
        }
        fIO = new FileHandler(new File(SiegeFishing.playerDataFolder, p.getName() + p.getUniqueId() + ".yml"));
        FileIO.fileStatusMapper.put(p.getName() + p.getUniqueId() + ".yml", true);
        Initialize(fIO.fC, p);
    }


    public static void Initialize(FileConfiguration fC, Player p) {
        String fN = p.getName() + p.getUniqueId() + ".yml";
        FileIO.fileMessageMapper.put(fN, "");

        if (FileIO.fileStatusMapper.get(fN)) {
            FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + "Loading file [" + fN + "]\n");

            List<String> gunModificationList = new ArrayList<>();
            HandData hand = (HandData) FileType.HAND.map.get("default");
            if (PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
                PlayerHandler.playerDataMapper.get(p.getUniqueId()).gunModificationCache = FormatHandler.gunModificationListCreator(fC, fN, "gunModificationList", FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "gunModificationList", gunModificationList, false), String.class));
                PlayerHandler.playerDataMapper.get(p.getUniqueId()).hand = (HandData) FormatHandler.checkConfigurationFormat(fC, fN, "hand", (HandData) hand, true);
            }
            if (FileIO.fileStatusMapper.get(fN)) {
                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + SiegeFishing.consolePluginPrefix + ColorCode.ANSI_WHITE + "Loaded player data [" + p.getName() + p.getUniqueId() + "]");
            } else {
                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + SiegeFishing.consolePluginPrefix + ColorCode.ANSI_RED + "Failed when loading player data [" + p.getName() + p.getUniqueId() + "]");
                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_RED + SiegeFishing.consolePluginPrefix + "The configuration of [" + ColorCode.ANSI_WHITE + p.getName() + p.getUniqueId() + ColorCode.ANSI_RED + "] couldn't be interpreted\n\n" + FileIO.fileMessageMapper.get(fN) + ColorCode.ANSI_WHITE);
            }
        }
    }

    public static void updateExtraPlayerData(Player p) {
        FileHandler fIO = new FileHandler(new File(SiegeFishing.playerDataFolder, p.getName() + p.getUniqueId() + ".yml"));
        if (PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
            PlayerData pD = PlayerHandler.playerDataMapper.get(p.getUniqueId());
            List<String> gunModificationList = new ArrayList<>();
            for (String s : pD.gunModificationCache.keySet()) {
                gunModificationList.add(s + "^" + pD.gunModificationCache.get(s) / 100 + "^" + pD.gunModificationCache.get(s) % 100 / 10 + "^" + pD.gunModificationCache.get(s) % 10);
            }
            fIO.set("gunModificationList" , gunModificationList);
            fIO.set("hand" , pD.hand.identifier);
            fIO.save();
        }
    }
}
