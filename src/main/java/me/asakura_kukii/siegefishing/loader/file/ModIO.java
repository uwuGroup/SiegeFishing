package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.handler.item.mod.ModData;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ModIO extends FileIO {

    public ModIO() {}

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        ModData mD = new ModData(identifier, fN);

        String displayName = "";
        Material material = Material.GOLDEN_AXE;
        Integer customModelIndex = 0;
        List<String> loreList = new ArrayList<>();

        int modSlot = 0;
        int modCustomModelIndex = 0;
        int modCustomModelIndexMultiplier = 0;

        double damageMultiplier = 1.00;
        double horizontalRecoilMultiplier = 1.00;
        double verticalRecoilMultiplier = 1.00;
        double accurateTimeMultiplier = 1.00;
        double inaccuracyMultiplier = 1.00;
        double rangeMultiplier = 1.00;
        boolean silenceGun = false;

        Boolean overrideScope = false;
        Boolean scopePotionEffect = false;
        Integer scopeZoomLevel = 0;
        Boolean scopeNightVision = false;
        Double scopeSpeedCompensation = 1.0;

        //reading...
        mD.displayName = ChatColor.translateAlternateColorCodes('&', (String) FormatHandler.checkConfigurationFormat(fC, fN, "displayName", displayName, false));
        mD.material = (Material) FormatHandler.checkConfigurationFormat(fC, fN, "material", material, true);
        mD.customModelIndex = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "customModelIndex", customModelIndex, true);
        mD.loreList = FormatHandler.coloredStringListCreator(FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "loreList", loreList, false), String.class));

        mD.modSlot = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "modSlot", modSlot, true);
        mD.modCustomModelIndex = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "modCustomModelIndex", modCustomModelIndex, true);
        mD.modCustomModelIndexMultiplier = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "modCustomModelIndexMultiplier", modCustomModelIndexMultiplier, true);

        mD.damageMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "damageMultiplier", damageMultiplier, true);
        mD.horizontalRecoilMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "horizontalRecoilMultiplier", horizontalRecoilMultiplier, true);
        mD.verticalRecoilMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "verticalRecoilMultiplier", verticalRecoilMultiplier, true);
        mD.accurateTimeMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "accurateTimeMultiplier", accurateTimeMultiplier, true);
        mD.inaccuracyMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "inaccuracyMultiplier", inaccuracyMultiplier, true);
        mD.rangeMultiplier = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "rangeMultiplier", rangeMultiplier, true);
        mD.silenceGun = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "silenceGun", silenceGun, true);

        mD.overrideScope = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "overrideScope", overrideScope, true);
        mD.scopePotionEffect = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "scopePotionEffect", scopePotionEffect, true);
        mD.scopeZoomLevel = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "scopeZoomLevel", scopeZoomLevel, true);
        mD.scopeNightVision = (Boolean) FormatHandler.checkConfigurationFormat(fC, fN, "scopeNightVision", scopeNightVision, true);
        mD.scopeSpeedCompensation = (Double) FormatHandler.checkConfigurationFormat(fC, fN, "scopeSpeedCompensation", scopeSpeedCompensation, true);

        return mD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {

    }
}
