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
    public Location lastLocation;
    public String lastRegionName = "";
    public String hotBarMsg = "";

    public double balance = 0;
    public double energy = 1;
    public BaitData activeBaitData = new BaitData("", "", FileType.BAIT);
    public HashMap<String, Integer> unlockFishIdentifierMap = new HashMap<>();
    public HashMap<String, Integer> specialFishIdentifierMap = new HashMap<>();
    public HashMap<String, Integer> unlockRegionNameMap = new HashMap<>();
    public List<String> unlockBoatIdentifierList = new ArrayList<>();
    public List<String> unlockRodIdentifierList = new ArrayList<>();
    public List<String> unlockFoodIdentifierList = new ArrayList<>();
    public List<String> unlockBaitIdentifierList = new ArrayList<>();

    public List<String> unlockAchievementList = new ArrayList<>();
    public List<String> giveAchievementList = new ArrayList<>();

    public HashMap<Integer, ItemStack> fishBox = new HashMap<>();
    public HashMap<Integer, ItemStack> backItemStackList = new HashMap<>();

    public double fishPercent = 0;
    public double boatPercent = 0;
    public double foodPercent = 0;
    public double regionPercent = 0;
    public double totalPercent = 0;

    public PlayerData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
}
