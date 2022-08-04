package me.asakura_kukii.siegefishing;

import me.asakura_kukii.siegefishing.data.addon.FishSessionData;
import me.asakura_kukii.siegefishing.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.data.basic.ImageValueData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.io.loader.common.Loader;
import me.asakura_kukii.siegefishing.io.util.FileUtil;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.utility.inventory.SiegeInventoryListener;
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

	public static void eventRegister(Plugin p) {
		Bukkit.getPluginManager().registerEvents(new SiegeFishingListener(), p);
		Bukkit.getPluginManager().registerEvents(new SiegeInventoryListener(), p);
	}

	public static void onEnable(Server s, File pF, String pN, String pP, String cPP, Plugin p) {
		server = s;
		pluginInstance = p;
		pluginFolder = pF;
		playerDataFolder = FileUtil.loadSubfolder("playerData");
		pluginName = pN;
		pluginPrefix = pP;
		consolePluginPrefix = cPP;

		Loader.loadAll();

		InputHandler.keyMapper();
		updater();
	}

	public static void onDisable() {
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
			}
		}.runTaskTimer(SiegeFishing.pluginInstance, 0, ConfigData.refreshDelay));
	}

}