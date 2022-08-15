package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.config.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.handler.method.achievement.AchievementUtil;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static me.asakura_kukii.siegefishing.SiegeFishing.consolePluginPrefix;
import static me.asakura_kukii.siegefishing.SiegeFishing.server;

public class FishSessionData extends FileData {
    public World world;
    public ImageValueData temperatureValue;
    public ImageMapData regionNameMap;
    public double temperatureBiasNoon;
    public double temperatureBiasMidnight;
    public Integer midnightGameTick = 17000;
    //random event

    public Integer currentHour;
    public Integer currentMinute;
    public Integer currentTemperatureBias;

    public Integer announcementTick = 1 * 60 * 20;
    public Integer currentRecycleTick = 18000;
    public Integer currentBackUpTick = 100;

    public FishSessionData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public void updateFishSession() {
        currentRecycleTick--;
        currentBackUpTick--;
        announcementTick--;
        if (currentRecycleTick <= 0) {
            currentRecycleTick = 18000;
            FileIO.saveAllOnDemand();
            server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_WHITE + "Saved player data" + fT.typeName);
        }
        if (currentBackUpTick <= 0) {
            currentBackUpTick = 36000;
            FileIO.backUpPlayerData();
        }
        if (announcementTick <= 0) {
            announcementTick = 1 * 60 * 20;
            try {
                String s = ExtraStringListData.random("announcement");
                for (Player p : world.getPlayers()) {
                    p.sendMessage(FormatHandler.format(s, false));
                }
            } catch (Exception ignored) {
            }

        }
        updateTime();
        for (Player p : world.getPlayers()) {
            PlayerData pD = PlayerDataHandler.getPlayerData(p);
            AchievementUtil.checkAchievement(pD);
            p.setExp((float) pD.energy);
            p.setLevel(0);
            p.setFoodLevel(18);
            p.setHealth(20);
        }
    }

    public void updateTime() {
        int currentGameDayTick = (int) world.getTime();
        int currentDayTick = 0;
        if (currentGameDayTick > midnightGameTick) {
            currentDayTick = currentGameDayTick - midnightGameTick;
        } else {
            currentDayTick = 24000 - midnightGameTick + currentGameDayTick;
        }
        currentHour = (int) Math.floor(currentDayTick / 1000.0);
        currentMinute = (int) Math.floor((currentDayTick - currentHour * 1000.0) * 60.0 / 1000.0);
    }

    public String getHourString() {
        if (currentHour.toString().length() < 2) {
            return "0" + currentHour;
        } else {
            return "" + currentHour;
        }
    }

    public String getMinuteString() {
        if (currentMinute.toString().length() < 2) {
            return "0" + currentMinute;
        } else {
            return "" + currentMinute;
        }
    }
}
