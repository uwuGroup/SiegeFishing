package me.asakura_kukii.siegefishing;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.FishSessionData;
import me.asakura_kukii.siegefishing.config.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.handler.inventory.SiegeInventoryListener;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.SiegePlayerListener;
import me.asakura_kukii.siegefishing.handler.region.SiegeRegionListener;
import me.asakura_kukii.siegefishing.utility.file.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;

public class SiegeFishing{
	public static Server server = null;
	public static Plugin pluginInstance = null;
	public static File pluginFolder = null;
	public static File playerDataFolder;
	public static String pluginName = null;
	public static String pluginPrefix = null;
	public static String consolePluginPrefix = null;
	public static HashMap<Plugin, BukkitTask> updaterRegister = new HashMap<>();

	public static void eventRegister(Plugin p) {
		Bukkit.getPluginManager().registerEvents(new SiegePlayerListener(), p);
		Bukkit.getPluginManager().registerEvents(new SiegeRegionListener(), p);
		Bukkit.getPluginManager().registerEvents(new SiegeInventoryListener(), p);
	}

	public static void onEnable(Server s, File pF, String pN, String pP, String cPP, Plugin p) {

		if (server != null) {
			FileIO.saveAll();
		}

		server = s;
		pluginInstance = p;
		pluginFolder = pF;
		playerDataFolder = FileUtil.loadSubfolder("playerData");
		pluginName = pN;
		pluginPrefix = pP;
		consolePluginPrefix = cPP;

		FileIO.loadAll();

		InputHandler.keyMapper();
		updater();
	}

	public static void onDisable() {
		FileIO.saveAll();
	}

	public static void updater() {
		if (updaterRegister.containsKey(pluginInstance)) {
			updaterRegister.get(pluginInstance).cancel();
			updaterRegister.remove(pluginInstance);
		}
		updaterRegister.put(pluginInstance, new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.getLogger().info(FileType.PLAYER_DATA.map.keySet().toString());
				for (FileData fD : FileType.FISH_SESSION.map.values()) {
					FishSessionData fSD = (FishSessionData) fD;
					fSD.updateFishSession();
				}

			}
		}.runTaskTimer(SiegeFishing.pluginInstance, 0, ConfigData.refreshDelay));
	}

}