package me.asakura_kukii.siegefishing.main;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.utility.argument.tab.TabHandler;
import me.asakura_kukii.siegefishing.utility.file.FileUtil;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import me.asakura_kukii.siegefishing.utility.argument.command.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin {

    public static File dataFolder;
    public static File tempFolder;
    private static Main main;
    public static Main getInstance() {
        return main;
    }

    public static String pluginPrefix = ChatColor.translateAlternateColorCodes('&',"&8[&cSiegeFishing&8] &f");
    public static String consolePluginPrefix = "[SiegeFishing]->>";
    public static String pluginName = "SiegeFishing";


    @Override
    public void reloadConfig() {
        dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            getServer().getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + pluginPrefix + "Creating data folder" + ColorHandler.ANSI_WHITE);
        }
        tempFolder = new File(dataFolder, "temp");
        if (tempFolder.exists() && tempFolder.isDirectory()) {
            for (File tempFile : Objects.requireNonNull(tempFolder.listFiles())) {
                tempFile.delete();
            }
        } else {
            tempFolder.delete();
            tempFolder.mkdir();
        }
    }

    public void reloadValues() {
        reloadConfig();
        SiegeFishing.onEnable(getServer(), dataFolder, pluginName, pluginPrefix, consolePluginPrefix, this);
    }

    @Override
    public void onEnable() {
        main = this;
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null){
            getServer().getConsoleSender().sendMessage(ColorHandler.ANSI_RED + consolePluginPrefix + "Missing ProtocolLib, disabling..." + ColorHandler.ANSI_WHITE);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        SiegeFishing.eventRegister(this);
        getServer().getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + "Enabling " + pluginName + ColorHandler.ANSI_WHITE);
        reloadValues();
    }

    @Override
    public void onDisable() {
        SiegeFishing.onDisable();
        getServer().getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + pluginName + " disabled" + ColorHandler.ANSI_WHITE);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<String>();
        if (args.length > 0) {
            s = TabHandler.onTab(sender, command, alias, args);
            return s;
        }
        return s;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("SiegeFishing")) {
            return CommandHandler.onCommand(sender, command, label, args);
        }
        return true;
    }
}

