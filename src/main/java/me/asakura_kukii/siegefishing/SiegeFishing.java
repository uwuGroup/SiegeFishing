package me.asakura_kukii.siegefishing;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.FishSessionData;
import me.asakura_kukii.siegefishing.config.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.handler.method.boat.BoatListener;
import me.asakura_kukii.siegefishing.handler.method.entity.TagEntityDataListener;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.handler.method.inventory.SiegeInventoryListener;
import me.asakura_kukii.siegefishing.handler.method.shovel.ShovelTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputHandler;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataListener;
import me.asakura_kukii.siegefishing.handler.method.region.SiegeRegionListener;
import me.asakura_kukii.siegefishing.handler.player.input.InputListener;
import me.asakura_kukii.siegefishing.utility.file.FileUtil;
import me.asakura_kukii.siegefishing.utility.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
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

	public static void eventRegister(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new InputListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new BoatListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new TagEntityDataListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new SiegeRegionListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new SiegeInventoryListener(), plugin);
	}

	public static void onEnable(Server s, File pF, String pN, String pP, String cPP, Plugin p) {

		if (server != null) {
			//reloading
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
				for (FileData fD : FileType.FISH_SESSION.map.values()) {
					FishSessionData fSD = (FishSessionData) fD;
					fSD.updateFishSession();
				}
				for (ShovelTaskData sTD : ShovelTaskData.shovelTaskDataMap.values()) {
					sTD.update();
				}
				for (FishingTaskData fTD : FishingTaskData.fishingTaskMap.values()) {
					fTD.update();
				}
				FishingTaskData.killLast();
				ShovelTaskData.killLast();
			}
		}.runTaskTimer(SiegeFishing.pluginInstance, 0, ConfigData.refreshDelay));
	}
}