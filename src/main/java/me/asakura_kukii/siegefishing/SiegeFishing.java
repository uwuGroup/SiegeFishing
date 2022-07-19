package me.asakura_kukii.siegefishing;

import me.asakura_kukii.siegefishing.handler.nonitem.method.projectile.ProjectileHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.block.BlockHandler;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.utility.nms.ProtocolLibHandler;
import me.asakura_kukii.siegefishing.listener.*;
import me.asakura_kukii.siegefishing.loader.*;
import me.asakura_kukii.siegefishing.loader.ConfigIO;
import me.asakura_kukii.siegefishing.loader.common.FileHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.HashMap;

public class SiegeFishing extends JavaPlugin {
	public static Server server = null;
	public static Plugin pluginInstance = null;
	public static File pluginFolder = null;
	public static File playerDataFolder;
	public static String pluginName = null;
	public static String pluginPrefix = null;
	public static String consolePluginPrefix = null;

	public static HashMap<Plugin, BukkitTask> updaterRegister = new HashMap<>();
	public static HashMap<String, Location> locationRegister = new HashMap<>();
	public static double blockCheckSpacing = 0.6;
	public static double trailSpacing = 0.3;
	public static double trailBias = 2;
	public static double trailTransitionStart = 1;
	public static double trailTransitionFactor = 0.6;
	public static boolean switchShiftAndRightKey = false;
	public static double scopeRecoilMultiplier = 0.5;
	public static double scopeInaccuracyMultiplier = 0.5;
	public static double headShotMultiplier = 20;
	public static boolean forceUpdateWhenNormalSemiFire = true;
	public static HashMap<Material, Double> breakableBlockMap = new HashMap<>();
	public static HashMap<Material, Material> replaceableBlockMap = new HashMap<>();
	public static HashMap<Material, Double> penetrableBlockMap = new HashMap<>();

	public static String potentialNormal = "";
	public static String potentialGenerate = "";
	public static String potentialUpdate = "";
	public static String potentialVisionNormal = "";
	public static String potentialVisionGenerate = "";
	public static String potentialVisionUpdate = "";


	public static void SiegeWeaponEventRegister(Plugin p) {
		Bukkit.getPluginManager().registerEvents(new SiegeWeaponListener(), p);
	}

	public static void SiegeWeaponLoader(Server s, File pF, String pN, String pP, String cPP, Plugin p) {
		server = s;
		pluginInstance = p;
		pluginFolder = pF;
		playerDataFolder = FileHandler.loadSubfolder("playerData");
		pluginName = pN;
		pluginPrefix = pP;
		consolePluginPrefix = cPP;

		FileIO.fileStatusMapper.clear();
		FileIO.fileMessageMapper.clear();
		FileIO.invalidFileNameList.clear();
		BlockHandler.blockHealthMapper.clear();

		FileIO.loadAll();
		ConfigIO.LoadConfig();
		PlayerIO.LoadAll();

		InputHandler.keyMapper();
		updater();

		ProtocolLibHandler.initProtocolLibHandler();
	}

	public static void updater() {
		if (updaterRegister.containsKey(pluginInstance)) {
			updaterRegister.get(pluginInstance).cancel();
			updaterRegister.remove(pluginInstance);
		}
		updaterRegister.put(pluginInstance, new BukkitRunnable() {
			@Override
			public void run() {

				ProjectileHandler.projectileUpdater();


				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayerData pD = PlayerHandler.getPlayerData(p);
					RayTraceResult rTR = p.rayTraceBlocks( 50, FluidCollisionMode.NEVER);
					/*if (rTR != null) {
						ParticleHandler.spawnParticle(rTR.getHitPosition().toLocation(p.getWorld()), ParticleData.particleDataMapper.get("default_laser"));
					}*/

					if (pD.lastLocation != null) {
						if (pD.lastLocation.getWorld() == pD.p.getWorld()) {
							pD.velocity = pD.p.getLocation().toVector().subtract(pD.lastLocation.toVector());
							pD.velocity.setY(pD.p.getVelocity().getY());
						} else {
							pD.velocity = new Vector(0, 0, 0);
						}
					} else {
						pD.velocity = new Vector(0, 0, 0);
					}
					pD.lastLocation = p.getLocation();
				}
			}
		}.runTaskTimer(SiegeFishing.pluginInstance, 0, 1));
	}

}