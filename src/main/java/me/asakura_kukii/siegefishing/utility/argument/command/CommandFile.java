package me.asakura_kukii.siegefishing.utility.argument.command;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class CommandFile {

    public static void handleFile(CommandSender sender, Argument argument) {
        String s = argument.nextString();
        if (s == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing sub-argument");
            return;
        }
        switch (s) {
            case "list":
                handleFileList(sender, argument);
                break;
            default:
                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid sub-argument");
                break;
        }
    }

    public static void handleFileList(CommandSender sender, Argument argument) {
        String type = argument.nextString();
        if (type == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing file type");
            return;
        }
        if (FileType.getFileTypeFromName(type) == null && !type.equals("invalid")) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid file type");
            return;
        }
        FileType fT;
        if (type.equals("invalid")) {
            fT = null;
            listInvalid(sender);
        } else {
            fT = FileType.getFileTypeFromName(type);
            assert fT != null;
            listFile(sender, fT);
        }
    }

    public static void listFile(CommandSender sender, FileType fT) {
        HashMap<String, FileData> map = fT.map;
        if (map.keySet().size() == 0) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "NO FILE");
        } else if (map.keySet().size() == 1) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + map.size() + " FILE LISTED:");
        } else {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + map.size() + " FILES LISTED:");
        }
        for (String s : map.keySet()) {
            sender.sendMessage(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + " FILE_NAME: " + ChatColor.WHITE + map.get(s).fileName + ChatColor.GRAY + " NAME: " + ChatColor.WHITE + map.get(s).identifier);
        }
    }

    public static void listInvalid(CommandSender sender) {
        List<String> invalidFileNameList = FileIO.invalidFileNameList;
        if (invalidFileNameList.size() == 0) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "NO ITEM");
        } else if (invalidFileNameList.size() == 1) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + invalidFileNameList.size() + " ITEM LISTED:");
        } else {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + invalidFileNameList.size() + " ITEMS LISTED:");
        }
        for (String s : invalidFileNameList) {
            sender.sendMessage(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + " FILE_NAME: " + ChatColor.WHITE + s);
        }
    }
}
