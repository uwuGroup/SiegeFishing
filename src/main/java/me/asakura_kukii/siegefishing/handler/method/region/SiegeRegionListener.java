package me.asakura_kukii.siegefishing.handler.method.region;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.config.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class SiegeRegionListener implements org.bukkit.event.Listener {
    public final String findMessage = ChatColor.WHITE + "" + ChatColor.BOLD + "已发现";
    public final String arriveMessage = ChatColor.WHITE + "";

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String m = FormatHandler.format(e.getMessage(), true);
        e.setMessage(m);
        for (FileData fD : FileType.IMAGE_MAP.map.values()) {
            ImageMapData iMD = (ImageMapData) fD;
            if (e.getPlayer().getWorld().equals(iMD.referenceChunk.getWorld())) {
                String s = iMD.getRegionNameAt(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ());
                String format = s + ChatColor.RESET + "<%1$s> %2$s";
                e.setFormat(format);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        Location l = pD.lastLocation;
        if (l == null) {
            pD.lastLocation = p.getLocation();
        }
        String regionName = "";
        for (FileData fD : FileType.IMAGE_MAP.map.values()) {
            ImageMapData iMD = (ImageMapData) fD;
            if (Objects.equals(l.getWorld(), iMD.referenceChunk.getWorld())) {
                regionName = iMD.getRegionNameAt(l.getChunk().getX(), l.getChunk().getZ());
            }
        }

        if (!pD.unlockRegionNameMap.containsKey(regionName)) {
            pD.unlockRegionNameMap.put(regionName, 1);
            p.sendTitle(regionName, findMessage, 10, 70, 20);
        } else {
            int amount = pD.unlockRegionNameMap.get(regionName);
            if (!regionName.equals(pD.lastRegionName)) {
                pD.unlockRegionNameMap.remove(regionName);
                pD.unlockRegionNameMap.put(regionName, amount + 1);
                p.sendTitle(arriveMessage, regionName, 10, 70, 20);
            }
        }
        pD.lastRegionName = regionName;
        pD.lastLocation = p.getLocation();
    }
}

