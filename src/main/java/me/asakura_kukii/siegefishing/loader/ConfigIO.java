package me.asakura_kukii.siegefishing.loader;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import me.asakura_kukii.siegefishing.loader.common.format.common.Format;
import me.asakura_kukii.siegefishing.loader.common.format.common.FormatType;
import me.asakura_kukii.siegefishing.utility.colorcode.ColorCode;
import me.asakura_kukii.siegefishing.main.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class ConfigIO {

    public static void LoadConfig() {
        if (Main.configFile.exists()) {
            FileConfiguration fC = YamlConfiguration.loadConfiguration(Main.configFile);
            FileIO.fileStatusMapper.put(Main.configFile.getName(), true);
            Initialize(fC, Main.configFile.getName());
        }
    }

    //this is a crap

    public static void Initialize(FileConfiguration fC, String fN) {
        FileIO.fileMessageMapper.put(fN, "");

        String identifier = "";
        identifier = (String) FormatHandler.checkConfigurationFormat(fC, fN, "identifier", identifier, true);
        if (FileIO.fileStatusMapper.get(fN)) {
            FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + "Loading file [" + fN + "]\n");

            double blockCheckSpacing = 0.6;
            double trailSpacing = 0.3;
            double trailBias = 2.0;
            double trailTransitionStart = 1.0;
            double trailTransitionFactor = 0.6;
            boolean switchShiftAndRightKey = false;
            double scopeRecoilMultiplier = 0.5;
            double scopeInaccuracyMultiplier = 0.5;
            double headShotMultiplier = 20;
            boolean forceUpdateWhenNormalSemiFire = true;
            List<String> breakableBlockList = new ArrayList<>();
            List<String> replaceableBlockList = new ArrayList<>();
            List<String> penetrableBlockList = new ArrayList<>();
            HashMap<Material, Double> breakableBlockMap = new HashMap<>();
            HashMap<Material, Material> replaceableBlockMap = new HashMap<>();
            HashMap<Material, Double> penetrableBlockMap = new HashMap<>();

            String potentialNormal = "";
            String potentialGenerate = "";
            String potentialUpdate = "";
            String potentialVisionNormal = "";
            String potentialVisionGenerate = "";
            String potentialVisionUpdate = "";
            //reading...
            blockCheckSpacing = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "blockCheckSpacing", blockCheckSpacing, true);
            trailSpacing = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "trailSpacing", trailSpacing, true);
            trailBias = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "trailBias", trailBias, true);
            trailTransitionStart = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "trailTransitionStart", trailTransitionStart, true);
            trailTransitionFactor = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "trailTransitionFactor", trailTransitionFactor, true);
            switchShiftAndRightKey = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "switchShiftAndRightKey", switchShiftAndRightKey, true);
            scopeRecoilMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "scopeRecoilMultiplier", scopeRecoilMultiplier, true);
            scopeInaccuracyMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "scopeInaccuracyMultiplier", scopeInaccuracyMultiplier, true);
            headShotMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "headShotMultiplier", headShotMultiplier, true);
            forceUpdateWhenNormalSemiFire = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "forceUpdateWhenNormalSemiFire", forceUpdateWhenNormalSemiFire, true);
            breakableBlockMap = FormatHandler.breakableBlockMapCreator(fC, fN, "breakableBlockMap", FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "breakableBlockMap", breakableBlockList, false), String.class));
            replaceableBlockMap = FormatHandler.replaceableBlockMapCreator(fC, fN, "replaceableBlockMap", FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "replaceableBlockMap", replaceableBlockList, false), String.class));
            penetrableBlockMap = FormatHandler.breakableBlockMapCreator(fC, fN, "penetrableBlockMap", FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "penetrableBlockMap", penetrableBlockList, false), String.class));

            potentialNormal = (String) Format.get(fC, fN, "potentialNormal", potentialNormal, FormatType.COLORED_STRING, true);
            potentialGenerate = (String) Format.get(fC, fN, "potentialGenerate", potentialGenerate, FormatType.COLORED_STRING, true);
            potentialUpdate = (String) Format.get(fC, fN, "potentialUpdate", potentialUpdate, FormatType.COLORED_STRING, true);
            potentialVisionNormal = (String) Format.get(fC, fN, "potentialVisionNormal", potentialVisionNormal, FormatType.COLORED_STRING, true);
            potentialVisionGenerate = (String) Format.get(fC, fN, "potentialVisionGenerate", potentialVisionGenerate, FormatType.COLORED_STRING, true);
            potentialVisionUpdate = (String) Format.get(fC, fN, "potentialVisionUpdate", potentialVisionUpdate, FormatType.COLORED_STRING, true);



            if (FileIO.fileStatusMapper.get(fN)) {
                SiegeFishing.blockCheckSpacing = blockCheckSpacing;
                SiegeFishing.trailSpacing = trailSpacing;
                SiegeFishing.trailBias = trailBias;
                SiegeFishing.trailTransitionStart = trailTransitionStart;
                SiegeFishing.trailTransitionFactor = trailTransitionFactor;
                SiegeFishing.switchShiftAndRightKey = switchShiftAndRightKey;
                SiegeFishing.scopeRecoilMultiplier = scopeRecoilMultiplier;
                SiegeFishing.scopeInaccuracyMultiplier = scopeInaccuracyMultiplier;
                SiegeFishing.headShotMultiplier = headShotMultiplier;
                SiegeFishing.forceUpdateWhenNormalSemiFire = forceUpdateWhenNormalSemiFire;
                SiegeFishing.breakableBlockMap = breakableBlockMap;
                SiegeFishing.replaceableBlockMap = replaceableBlockMap;
                SiegeFishing.penetrableBlockMap = penetrableBlockMap;

                SiegeFishing.potentialNormal = potentialNormal;
                SiegeFishing.potentialGenerate = potentialGenerate;
                SiegeFishing.potentialUpdate = potentialUpdate;
                SiegeFishing.potentialVisionNormal = potentialVisionNormal;
                SiegeFishing.potentialVisionGenerate = potentialVisionGenerate;
                SiegeFishing.potentialVisionUpdate = potentialVisionUpdate;

                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + SiegeFishing.consolePluginPrefix + ColorCode.ANSI_WHITE + "Loaded config [" + identifier + "]");
            } else {
                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + SiegeFishing.consolePluginPrefix + ColorCode.ANSI_RED + "Failed when loading config [" + identifier + "]");
                SiegeFishing.server.getConsoleSender().sendMessage(ColorCode.ANSI_RED + SiegeFishing.consolePluginPrefix + "The plugin configuration couldn't be interpreted\n\n" + FileIO.fileMessageMapper.get(fN) + ColorCode.ANSI_WHITE);
            }
        }
    }
}
