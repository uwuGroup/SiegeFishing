package me.asakura_kukii.siegefishing;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.asakura_kukii.siegefishing.handler.nonitem.method.projectile.ProjectileHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.InputHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.block.BlockHandler;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import me.asakura_kukii.siegefishing.utility.nms.ProtocolLibHandler;
import me.asakura_kukii.siegefishing.listener.*;
import me.asakura_kukii.siegefishing.loader.*;
import me.asakura_kukii.siegefishing.loader.ConfigIO;
import me.asakura_kukii.siegefishing.loader.common.FileHandler;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

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

	public static Chunk referenceChunk;


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

				if (referenceChunk != null) {

					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.getVehicle() instanceof Boat) {
							p.sendMessage(p.getPassengers().toString());
							if (p.getPassengers().isEmpty()) {

								ArmorStand aS = (ArmorStand) PositionHandler.generateArmorStandStack(p, p.getUniqueId(),2);

								ItemStack boatItem = new ItemStack(Material.COOKIE);
								ItemMeta iM = boatItem.getItemMeta();
								iM.setCustomModelData(2);
								boatItem.setItemMeta(iM);
								aS.getEquipment().setHelmet(boatItem);


							} else {
								PositionHandler.rotateArmorStandStack(p, p.getVehicle().getLocation().getYaw(), (float) 0);
							}
						} else {
							PositionHandler.deleteArmorStandStack(p);
							continue;
						}
					}




					for (Entity e : referenceChunk.getWorld().getEntities()) {
						if (!(e instanceof Boat)) {
							continue;
						}
						for (Entity passenger : e.getPassengers()) {
							//if (passenger instanceof ArmorStand) {
							//	passenger.setRotation(e.getLocation().getYaw(), 0);
							//}
						}
					}

				}
			}
		}.runTaskTimer(SiegeFishing.pluginInstance, 0, 0));
	}

}