package me.asakura_kukii.siegefishing.loader.file;

import me.asakura_kukii.siegefishing.loader.common.FileData;
import me.asakura_kukii.siegefishing.handler.item.tool.ToolData;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class ToolIO extends FileIO{

    public ToolIO() {};

    @Override
    public FileData loadData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        ToolData tD = new ToolData(identifier, fN);

        String displayName = "";
        Material material = Material.PRISMARINE_SHARD;
        Integer customModelIndex = 0;
        List<String> loreList = new ArrayList<>();

        String usage = "";

        //reading...
        tD.displayName = ChatColor.translateAlternateColorCodes('&', (String) FormatHandler.checkConfigurationFormat(fC, fN, "displayName", displayName, false));
        tD.material = (Material) FormatHandler.checkConfigurationFormat(fC, fN, "material", material, true);
        tD.customModelIndex = (Integer) FormatHandler.checkConfigurationFormat(fC, fN, "customModelIndex", customModelIndex, true);
        tD.loreList = FormatHandler.coloredStringListCreator(FormatHandler.castList(FormatHandler.checkConfigurationFormat(fC, fN, "loreList", loreList, false), String.class));
        tD.usage = (String) FormatHandler.checkConfigurationFormat(fC, fN, "usage", usage, false);

        return tD;
    }

    @Override
    public HashMap<String, Object> loadSubData(FileConfiguration fC, String fN, FileType fT, String identifier, File folder) {
        return null;
    }

    @Override
    public void loadDefault(FileType fT) {
        ToolData tD = new ToolData("default", "");
        tD.customModelIndex = 1;
        tD.displayName = ChatColor.translateAlternateColorCodes('&', "&8&lMODDING TOOL");
        tD.material = Material.PRISMARINE_SHARD;
        tD.loreList = new ArrayList<>();
        tD.loreList.add(ChatColor.translateAlternateColorCodes('&', "&6&lIt's a tool, for modding"));
        tD.usage = "modding";
        fT.map.put("default", tD);
    }
}
