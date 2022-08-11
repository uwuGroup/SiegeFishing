package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerData extends FileData {
    public Player p;
    public HashMap<String, Boolean> stateCache = new HashMap<>();
    public HashMap<String, Integer> keyHoldRegister = new HashMap<>();

    public List<ItemStack> unlockFishList = new ArrayList<>();
    public List<String> unlockFishIdentifierList = new ArrayList<>();
    public List<ItemStack> favouredFishList = new ArrayList<>();
    public List<String> unlockRegionNameList = new ArrayList<>();
    public Location lastLocation;
    public String lastRegionName = "";
    public String hotBarMsg = "";
    public PlayerData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
