package me.asakura_kukii.siegefishing.main;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.utility.colorcode.ColorCode;
import me.asakura_kukii.siegefishing.utility.command.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static File configFile;
    private FileConfiguration config;
    private YamlConfiguration CommentYamlConfiguration;
    private static Main main;
    public static boolean DEBUG = false;
    public boolean saveTheConfig = false;
    public static Main getInstance() {
        return main;
    }
    public static File siegeWeaponDataFolder;
    public static String pluginPrefix = ChatColor.translateAlternateColorCodes('&',"&8[&cSiegeFishing&8] &f");
    public static String consolePluginPrefix = "[SiegeFishing]->>";
    public static String pluginName = "SiegeFishing";


    @Override
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(this.getDataFolder(), "config.yml");
            if (!this.getDataFolder().exists())
                this.getDataFolder().mkdirs();
            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);

    }

    public void reloadValues() {
        if(!this.getDataFolder().exists()) {
            if(this.getDataFolder().mkdirs()) {
                getServer().getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + pluginPrefix + "Creating data folder" + ColorCode.ANSI_WHITE);
            }
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
            reloadConfig();
        }
        siegeWeaponDataFolder = getDataFolder();
        SiegeFishing.SiegeWeaponLoader(getServer(), siegeWeaponDataFolder, pluginName, pluginPrefix, consolePluginPrefix, this);
    }

    @Override
    public void onEnable() {
        main = this;
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null){
            getServer().getConsoleSender().sendMessage(ColorCode.ANSI_RED + consolePluginPrefix + "Missing ProtocolLib, disabling..." + ColorCode.ANSI_WHITE);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        reloadConfig();
        SiegeFishing.SiegeWeaponEventRegister(this);
        getServer().getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + consolePluginPrefix + "Enabling SiegeFishing" + ColorCode.ANSI_WHITE);
        reloadValues();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ColorCode.ANSI_GREEN + consolePluginPrefix + "SiegeFishing disabled" + ColorCode.ANSI_WHITE);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<String>();
        if (args.length > 0) {
            s = CommandHandler.onTabComplete(sender, command, alias, args);
            return s;
        }
        return s;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("SiegeFishing")) {
            if (args.length > 0) {
                return CommandHandler.onCommand(sender, command, label, args);
            }
            return true;
        }
        return true;
    }

    @Override
    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }
        return this.config;
    }

    @Override
    public void saveConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

