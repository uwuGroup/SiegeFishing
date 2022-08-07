package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.config.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import org.bukkit.World;

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

    public FishSessionData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public void updateFishSession() {
        updateTime();

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
