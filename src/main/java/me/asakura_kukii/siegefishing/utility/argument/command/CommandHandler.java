package me.asakura_kukii.siegefishing.utility.argument.command;

import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.asakura_kukii.siegefishing.SiegeFishing.*;


public class CommandHandler {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("");
        sender.sendMessage(pluginPrefix + "Issued argument:");
        sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
        Argument argument = new Argument(args);

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
            case "hookTest":
                if (sender.hasPermission(pluginName + ".hookTest")) {
                    handleHook(sender, argument);
                } else {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Permission error - Missing permission");
                    return true;
                }
                break;
            case "hookBetaTest":

                break;
            default:
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Invalid sub-argument");
                return true;
        }
        return true;
    }

    private static void handleHook(CommandSender sender, Argument argument) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pluginPrefix + ChatColor.RED + "Only Player can execute this command");
            return;
        }
        Player player = (Player) sender;

        FishingSession session = FishingSession.newSession(player, new ItemStack(Material.AIR));
        session.start();
    }

    public static void handleReload(CommandSender sender, Argument argument) {
        Main.getInstance().reloadValues();
        sender.sendMessage(pluginPrefix + "Plugin reloaded");
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
