package me.asakura_kukii.siegefishing.handler.nonitem.player;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {
    public UUID uuid;
    public Player p;
    public HashMap<String, Boolean> stateCache = new HashMap<>();
    public HashMap<String, Integer> keyHoldRegister = new HashMap<>();
    public PoseType poseState = PoseType.NORMAL;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }
}
