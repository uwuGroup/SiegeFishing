package me.asakura_kukii.siegefishing.utility.argument.command;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import me.asakura_kukii.siegefishing.handler.method.inventory.*;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.handler.player.SiegeMountHandler;
import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NMSHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static me.asakura_kukii.siegefishing.SiegeFishing.*;


public class CommandHandler {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("");
        sender.sendMessage(pluginPrefix + "Issued argument:");
        sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
        Argument argument = new Argument(args);

        if (args.length == 0) {
            //open menu;
            if (sender instanceof Player) {
                SiegeBasicMenu sBM = new SiegeBasicMenu(((Player) sender));
                sBM.show((Player) sender);
                return true;
            }
        }

        String s = argument.nextString();
        if (s == null) {
            sender.sendMessage(pluginPrefix + ChatColor.RED + "Missing sub-argument");
            return true;
        }
        switch (s) {
            case "item":
                if (sender instanceof Player) {
                    if (sender.hasPermission(pluginName + ".item")) {
                        CommandItem.handleItem(sender, argument);
                    } else {
                        sender.sendMessage(pluginPrefix + ChatColor.RED + "Permission error - Missing permission");
                        return true;
                    }
                } else {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Command sender error - Player only");
                    return true;
                }
                break;
            case "file":
                if (sender instanceof Player) {
                    if (sender.hasPermission(pluginName + ".file")) {
                        CommandFile.handleFile(sender, argument);
                    } else {
                        sender.sendMessage(pluginPrefix + ChatColor.RED + "Permission error - Missing permission");
                        return true;
                    }
                } else {
                    CommandFile.handleFile(sender, argument);
                    return true;
                }
                break;
            case "reload":
                if (sender instanceof Player) {
                    if (sender.hasPermission(pluginName + ".reload")) {
                        handleReload(sender, argument);
                    } else {
                        sender.sendMessage(pluginPrefix + ChatColor.RED + "Permission error - Missing permission");
                        return true;
                    }
                } else {
                    handleReload(sender, argument);
                    return true;
                }
                break;
            case "show":
                if (sender instanceof Player) {
                    handleShow(sender, argument);
                } else {
                    return true;
                }
                break;
            case "back":
                if (sender instanceof Player) {
                    handleBack(sender, argument);
                } else {
                    return true;
                }
                break;
            case "adminBal":
                if (sender instanceof Player && sender.hasPermission(pluginName + ".reload")) {
                    List<PlayerData> pDL = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerData pD = PlayerDataHandler.getPlayerData(p);
                        pDL.add(pD);
                    }
                    pDL.sort(new Comparator<PlayerData>() {
                        @Override
                        public int compare(PlayerData o1, PlayerData o2) {
                            if (o2.balance - o1.balance > 0) return 1;
                            if (o2.balance - o1.balance == 0) return 0;
                            if (o2.balance - o1.balance < 0) return -1;
                            return 0;
                        }
                    });
                    for (int i = 0; i < 10; i++) {
                        if (pDL.size() > i) ((Player) sender).sendMessage(pDL.get(i).p.getName() + " " + pDL.get(i).balance);
                    }
                }
                break;
            case "adminPer":
                if (sender instanceof Player && sender.hasPermission(pluginName + ".reload")) {
                    List<PlayerData> pDL = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerData pD = PlayerDataHandler.getPlayerData(p);
                        pDL.add(pD);
                    }
                    pDL.sort(new Comparator<PlayerData>() {
                        @Override
                        public int compare(PlayerData o1, PlayerData o2) {
                            if (o2.totalPercent - o1.totalPercent > 0) return 1;
                            if (o2.totalPercent - o1.totalPercent == 0) return 0;
                            if (o2.totalPercent - o1.totalPercent < 0) return -1;
                            return 0;
                        }
                    });
                    for (int i = 0; i < 10; i++) {
                        if (pDL.size() > i) ((Player) sender).sendMessage(pDL.get(i).p.getName() + " " + pDL.get(i).totalPercent);
                    }
                }
                break;
        }
        return true;
    }

    public static void handleReload(CommandSender sender, Argument argument) {
        Main.getInstance().reloadValues();
        sender.sendMessage(pluginPrefix + "Plugin reloaded");
    }

    public static void handleShow(CommandSender sender, Argument argument) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            try {
                String s = "&8@description#&7「&8&l" + p.getName() + "&7」&7展示了 ";
                ItemStack iS = p.getInventory().getItemInMainHand();
                BaseComponent format = new TextComponent(FormatHandler.format(s, false));
                ItemMeta iM = iS.getItemMeta();
                if (iM == null) {
                    return;
                }
                BaseComponent itemName = new TextComponent(FormatHandler.format("&f&l" + ChatColor.stripColor(iM.getDisplayName()), false));
                BaseComponent[] itemStackComponent = new BaseComponent[]{new TextComponent(NMSHandler.itemToJson(iS))};
                itemName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackComponent));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(format, itemName);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static void handleBack(CommandSender sender, Argument argument) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player p = (Player) sender;
        p.sendMessage(FormatHandler.format("&8@description#&7「&8&l" + p.getName() + "&7」&7背上了手中的物品", false));
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        pD.backItemStackList.put(0, p.getInventory().getItemInMainHand());
        SiegeMountHandler.communicate(pD);
    }


    public static String commandPainter(String[] args) {
        StringBuilder cSB = new StringBuilder();
        cSB.append(ChatColor.GRAY).append("/sf ");
        int cSBI = 0;
        for (String s : args) {
            if(cSBI == 0) {
                cSB.append(ChatColor.RED);
            }
            if(cSBI == 1) {
                cSB.append(ChatColor.WHITE);
            }
            if(cSBI == 2) {
                cSB.append(ChatColor.GOLD);
            }
            if(cSBI == 3) {
                cSB.append(ChatColor.LIGHT_PURPLE);
            }
            if(cSBI == 4) {
                cSB.append(ChatColor.AQUA);
            }
            if(cSBI == 5) {
                cSB.append(ChatColor.GREEN);
            }
            if(cSBI == 6) {
                cSB.append(ChatColor.BLUE);
            }
            if(cSBI == 7) {
                cSB.append(ChatColor.DARK_RED);
            }
            cSB.append(s).append(" ");
            cSBI++;
        }
        return cSB.toString();
    }
}
