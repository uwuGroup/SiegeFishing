package me.asakura_kukii.siegefishing.utility.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownController {
    private static Map<UUID, Long> cdMap = new HashMap<>();

    public boolean isCding(UUID uuid, long tick){
        Long cd = cdMap.get(uuid);
        return cd != null && tick > cd;
    }

    public void cd(UUID uuid, long cd){
        cdMap.put(uuid, cd);
    }
}
