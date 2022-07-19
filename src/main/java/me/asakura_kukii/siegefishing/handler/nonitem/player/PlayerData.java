package me.asakura_kukii.siegefishing.handler.nonitem.player;

import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {
    public UUID uuid;
    public Player p;
    //state register
    public HashMap<String, Boolean> stateCache = new HashMap<>();
    //state will contain many player state, including Carry, Scope and FullAuto
    //Scope and FullAuto will only change when holding a gun
    //While Carry will change at all times
    //while when holding a new gun, FullAuto must be set to false manually.
    //key holding register
    public HashMap<String, Integer> keyHoldRegister = new HashMap<>();
    public HashMap<String, Integer> gunModificationCache = new HashMap<>();
    public HandData hand;
    public Integer gunReserve = -1;
    public PoseType poseState = PoseType.NORMAL;

    public Location lastLocation;
    public org.bukkit.util.Vector velocity;
    //0 is normal pose
    //1 is aiming
    //2 is running
    /*
    public double velocity;
    public List<Location> locationList = new ArrayList<>();
    public double angularVelocity;
    */

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }
}
