package me.asakura_kukii.siegefishing.utility.argument.command;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import me.asakura_kukii.siegefishing.handler.method.inventory.SiegeInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandItem {

    public static void handleItem(CommandSender sender, Argument argument) {
        String s = argument.nextString();
        if (s == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing sub-argument");
            return;
        }
        switch (s) {
            case "list":
                handleItemList(sender, argument);
                break;
            case "view":
                handleItemView(sender, argument);
                break;
            case "give":
                handleItemGive(sender, argument);
                break;
            default:
                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid sub-argument");
                break;
        }
    }

    public static void handleItemGive(CommandSender sender, Argument argument) {
        String type = argument.nextString();
        if (type == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing item type");
            return;
        }
        if (FileType.getItemLinkedFileTypeFromName(type) == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid item type");
            return;
        }
        FileType fT = FileType.getItemLinkedFileTypeFromName(type);

        String identifier = argument.nextString();
        if (identifier == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing item identifier");
            return;
        }
        assert fT != null;
        if (!fT.map.containsKey(identifier)) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid item identifier");
            return;
        }

        Player p = (Player) sender;
        PlayerData pD = PlayerDataHandler.getPlayerData(p);

        ItemData iD = (ItemData) fT.map.get(identifier);
        ItemData.sendItemStack(iD, pD,1);
        sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SUCCESSFULLY SEND ITEM");
    }

    public static void handleItemList(CommandSender sender, Argument argument) {
        String type = argument.nextString();
        if (type == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing item type");
            return;
        }
        if (FileType.getItemLinkedFileTypeFromName(type) == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid item type");
            return;
        }
        FileType fT = FileType.getItemLinkedFileTypeFromName(type);
        assert fT != null;
        listItem(sender, fT);
    }

    public static void handleItemView(CommandSender sender, Argument argument) {
        String type = argument.nextString();
        if (type == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Missing item type");
            return;
        }
        if (FileType.getItemLinkedFileTypeFromName(type) == null) {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "Invalid item type");
            return;
        }
        FileType fT = FileType.getItemLinkedFileTypeFromName(type);
        assert fT != null;
        viewItem(sender, fT);
    }

    public static void listItem(CommandSender sender, FileType fT) {
        HashMap<String, FileData> map = fT.map;
        if (map.keySet().size() == 0) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "NO ITEM");
        } else if (map.keySet().size() == 1) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + map.size() + " ITEM LISTED:");
        } else {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + map.size() + " ITEMS LISTED:");
        }
        for (String s : map.keySet()) {
            sender.sendMessage(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + " FILE_NAME: " + ChatColor.WHITE + map.get(s).fileName + ChatColor.GRAY + " NAME: " + ChatColor.WHITE + map.get(s).identifier);
        }
    }

    public static void viewItem(CommandSender sender, FileType fT) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (FileData fD : FileType.getSortedFileDataList(fT)) {
            itemStackList.add(ItemData.getItemStack((ItemData) fD, null, 1, 0));
        }
        SiegeInventory itemInventory = new SiegeInventory(ChatColor.WHITE + "" + ChatColor.BOLD + "VIEW: " + fT.typeName, itemStackList, true);
        itemInventory.showInventoryToPlayer((Player) sender);
        sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SUCCESSFULLY VIEW TYPE");
    }
}
