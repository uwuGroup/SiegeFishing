package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class HandIO extends FileIO {

    public HandIO() {}

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        HandData hD = new HandData(identifier, fN);

        String displayName = "";
        Material material = Material.AMETHYST_SHARD;
        Integer customModelIndex = 0;
        List<String> loreList = new ArrayList<>();

        //reading...
        hD.displayName = ChatColor.translateAlternateColorCodes('&', (String) FormatHandler.checkConfigurationFormat(fC, fN, "displayName", displayName, false));
        hD.material = (Material) FormatHandler.checkConfigurationFormat(fC, fN, "material", material, true);
        hD.customModelIndex = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "customModelIndex", customModelIndex, true);
        hD.loreList = FormatHandler.coloredStringListCreator(FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "loreList", loreList, false), String.class));

        return hD;

    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {
        HandData hD = new HandData("default", "");
        hD.customModelIndex = 0;
        hD.displayName = ChatColor.translateAlternateColorCodes('&', "&8&lA GOOD OLD FOLK");
        hD.material = Material.AMETHYST_SHARD;
        hD.loreList = new ArrayList<>();
        hD.loreList.add(ChatColor.translateAlternateColorCodes('&', "&6&lIt's a hand, maybe yours"));
        fT.map.put("default", hD);
    }
}
