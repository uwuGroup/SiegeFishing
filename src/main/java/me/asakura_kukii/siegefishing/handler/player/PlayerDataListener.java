package me.asakura_kukii.siegefishing.handler.player;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.input.InputHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PlayerDataListener implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerDataHandler.loadPlayerData(p);
        SiegeMountHandler.communicate(pD);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        SiegeMountHandler.communicate(pD);
        PlayerDataHandler.unloadPlayerData(p);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
    }
}

