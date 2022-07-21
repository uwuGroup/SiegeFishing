package me.asakura_kukii.siegefishing.utility.command;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.handler.item.mod.ModHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.potential.PotentialType;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.mount.MountHandler;
import me.asakura_kukii.siegefishing.utility.nms.ProtocolLibHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class CommandHandler {
    public static boolean completeString(String arg, String startsWith) {
        if (arg == null || startsWith == null)
            return false;
        return arg.toLowerCase().startsWith(startsWith.toLowerCase());
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("item")) {
            sender.sendMessage("");
            sender.sendMessage(SiegeFishing.pluginPrefix + "Issued command:");
            sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
            if (sender instanceof Player) {
                if (sender.hasPermission("siegefishing.item")) {
                    //ana item XXX
                    if (args.length>=2) {
                        if (args[1].equalsIgnoreCase("list")) {
                            if (args.length>=3) {
                                if (args[2].equalsIgnoreCase("gun")) {
                                    listFile(sender, FileType.GUN);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("hand")) {
                                    listFile(sender, FileType.HAND);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("mod")) {
                                    listFile(sender, FileType.MOD);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("tool")) {
                                    listFile(sender, FileType.TOOL);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("particle")) {
                                    listFile(sender, FileType.PARTICLE);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("fish")) {
                                    listFile(sender, FileType.FISH);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("sound")) {
                                    listFile(sender, FileType.SOUND);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("invalid")) {
                                    MountHandler.refreshMount((Player) sender);
                                    listInvalid(sender);
                                    return true;
                                } else {
                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("give")) {
                            if (args.length>=3) {
                                if (args[2].equalsIgnoreCase("gun")) {
                                    giveItem(sender, args, FileType.GUN);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("hand")) {
                                    giveItem(sender, args, FileType.HAND);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("tool")) {
                                    giveItem(sender, args, FileType.TOOL);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("mod")) {
                                    giveItem(sender, args, FileType.MOD);
                                    return true;
                                } else if (args[2].equalsIgnoreCase("fish")) {
                                    giveItem(sender, args, FileType.FISH);
                                    return true;
                                } else {
                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                                return true;
                            }

                            //123132

                        } else {
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                            return true;
                        }
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                        return true;
                    }
                } else {
                    sender.sendMessage(SiegeFishing.pluginPrefix + "PERMISSION ERROR - Missing permission");
                    return true;
                }
            } else {
                //ana item XXX
                if (args.length>=2) {
                    if (args[1].equalsIgnoreCase("list")) {
                        if (args.length>=3) {
                            if (args[2].equalsIgnoreCase("gun")) {
                                listFile(sender, FileType.GUN);
                                return true;
                            } else if (args[2].equalsIgnoreCase("hand")) {
                                listFile(sender, FileType.HAND);
                                return true;
                            } else if (args[2].equalsIgnoreCase("mod")) {
                                listFile(sender, FileType.MOD);
                                return true;
                            } else if (args[2].equalsIgnoreCase("tool")) {
                                listFile(sender, FileType.TOOL);
                                return true;
                            } else if (args[2].equalsIgnoreCase("particle")) {
                                listFile(sender, FileType.PARTICLE);
                                return true;
                            } else if (args[2].equalsIgnoreCase("sound")) {
                                listFile(sender, FileType.SOUND);
                                return true;
                            } else if (args[2].equalsIgnoreCase("invalid")) {
                                listInvalid(sender);
                                return true;
                            } else {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                                return true;
                            }
                        } else {
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                            return true;
                        }
                    } else if (args[1].equalsIgnoreCase("give")) {
                        if (args.length>=3) {
                            if (args[2].equalsIgnoreCase("gun")) {
                                giveItem(sender, args, FileType.GUN);
                                return true;
                            } else if (args[2].equalsIgnoreCase("hand")) {
                                giveItem(sender, args, FileType.HAND);
                                return true;
                            } else if (args[2].equalsIgnoreCase("tool")) {
                                giveItem(sender, args, FileType.TOOL);
                                return true;
                            } else if (args[2].equalsIgnoreCase("mod")) {
                                giveItem(sender, args, FileType.MOD);
                                return true;
                            } else {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                                return true;
                            }
                        } else {
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                            return true;
                        }
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                        return true;
                    }
                } else {
                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                    return true;
                }
            }
        }
        /*
        if (args[0].equalsIgnoreCase("modify")) {
            sender.sendMessage("");
            sender.sendMessage(SiegeFishing.pluginPrefix + "Issued command:");
            sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
            if (sender instanceof Player) {
                if (sender.hasPermission("siegefishing.item")) {
                    //ana modify XXX
                    if (args.length>=2) {
                        if (GunData.gunDataMapper.containsKey(args[1])) {
                            if (args.length>=3) {
                                try {
                                    int index = Integer.parseInt(args[2]);
                                    int silencer = index / 100;
                                    int grip = index % 100 / 10;
                                    int scope = index % 10;
                                    if (silencer <= 2 && silencer >= 0 && grip <= 2 && grip >= 0 && scope <= 2 && scope >= 0) {
                                        if (args.length==3) {
                                            if(PlayerHandler.playerDataMapper.containsKey(((Player) sender).getUniqueId())) {
                                                GunHandler.modifyGun(GunData.gunDataMapper.get(args[1]), (Player) sender, index);
                                                sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "PLAYER DATA UPDATED");
                                                return true;
                                            } else {
                                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "SERVER ERROR - Invalid player, contact OP");
                                                return true;
                                            }
                                        } else {
                                            if (sender.hasPermission("siegefishing.admin")) {
                                                if (Bukkit.getPlayer(args[3])!=null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[3]))) {
                                                    Player p = Bukkit.getPlayer(args[3]);
                                                    assert p != null;
                                                    if(PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
                                                        GunHandler.modifyGun(GunData.gunDataMapper.get(args[1]), p, index);
                                                        sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "PLAYER DATA UPDATED");
                                                        return true;
                                                    } else {
                                                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                                                        return true;
                                                    }
                                                } else {
                                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage(SiegeFishing.pluginPrefix + "PERMISSION ERROR - Missing permission");
                                                return true;
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid modification");
                                        return true;
                                    }
                                } catch (Exception ignored) {
                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid modification");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing modification");
                                return true;
                            }
                        } else {
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid item name");
                            return true;
                        }
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing item name");
                        return true;
                    }
                } else {
                    sender.sendMessage(SiegeFishing.pluginPrefix + "PERMISSION ERROR - Missing permission");
                    return true;
                }
            } else {
                if (args.length>=2) {
                    if (GunData.gunDataMapper.containsKey(args[1])) {
                        if (args.length>=3) {
                            try {
                                int index = Integer.parseInt(args[2]);
                                int silencer = index / 100;
                                int grip = index % 100 / 10;
                                int scope = index % 10;
                                if (silencer <= 2 && silencer >= 0 && grip <= 2 && grip >= 0 && scope <= 2 && scope >= 0) {
                                    if (args.length>=4) {
                                        if (Bukkit.getPlayer(args[3])!=null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[3]))) {
                                            Player p = Bukkit.getPlayer(args[3]);
                                            assert p != null;
                                            if(PlayerHandler.playerDataMapper.containsKey(p.getUniqueId())) {
                                                GunHandler.modifyGun(GunData.gunDataMapper.get(args[1]), p, index);
                                                sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "PLAYER DATA UPDATED");
                                                return true;
                                            } else {
                                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                                                return true;
                                            }
                                        } else {
                                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(SiegeFishing.pluginPrefix + "PERMISSION ERROR - Missing player");
                                        return true;
                                    }
                                } else {
                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid modification");
                                    return true;
                                }
                            } catch (Exception ignored) {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid modification");
                                return true;
                            }
                        } else {
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing modification");
                            return true;
                        }
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid item name");
                        return true;
                    }
                } else {
                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing item name");
                    return true;
                }
            }

        }*/
        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("");
            sender.sendMessage(SiegeFishing.pluginPrefix + "Issued command:");
            sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
            if (sender instanceof Player) {
                if (sender.hasPermission("siegefishing.reload")) {
                    Main.getInstance().reloadConfig();
                    Main.getInstance().reloadValues();
                    sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "PLUGIN RELOADED");
                    return true;
                } else {
                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "PERMISSION ERROR - Missing permission");
                    return true;
                }
            } else {
                Main.getInstance().reloadConfig();
                Main.getInstance().reloadValues();
                sender.sendMessage(Main.pluginPrefix + "Plugin reloaded");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("util")) {
            sender.sendMessage("");
            sender.sendMessage(SiegeFishing.pluginPrefix + "Issued command:");
            sender.sendMessage(ChatColor.WHITE + ">> " + commandPainter(args));
            if (sender instanceof Player) {
                if (sender.hasPermission("siegefishing.util")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("spawn")) {
                            if (args.length >= 3) {
                                if (args[2].equalsIgnoreCase("modding_table")) {
                                    ModHandler.spawnModdingTable(((Player) sender).getLocation());
                                    sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SUMMONED ENTITY");
                                    return true;
                                }
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid entity");
                                return true;
                            }
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing entity");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("upgrade")) {
                            if (args.length >= 3) {
                                try {
                                    int index = Integer.parseInt(args[2]);
                                    try {
                                        ItemStack iS = ((Player) sender).getInventory().getItemInOffHand();
                                        iS = PotentialHandler.updatePotential(iS, PotentialType.WEAPON, index);
                                        ((Player) sender).getInventory().setItemInOffHand(iS);
                                        return true;
                                    } catch (Exception ignored) {
                                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "ITEM ERROR");
                                        return true;
                                    }
                                } catch (Exception ignored) {
                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid index");
                                    return true;
                                }
                            }
                            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing index");
                            return true;
                        }
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Invalid sub-command");
                        return true;
                    }
                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Missing sub-command");
                    return true;
                }
            } else {
                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "COMMAND ERROR - Only player can run this command");
                return true;
            }
            return true;
        }
        return true;
    }

    public static void listFile(CommandSender sender, FileType fT) {
        if (fT.map.keySet().size() == 0) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "NO ITEM");
        } else if (fT.map.keySet().size() == 1) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + fT.map.size() + " ITEM LISTED:");
        } else {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + fT.map.size() + " ITEMS LISTED:");
        }
        for (String s : fT.map.keySet()) {
            sender.sendMessage(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + " FILE_NAME: " + ChatColor.WHITE + fT.map.get(s).fileName + ChatColor.GRAY + " NAME: " + ChatColor.WHITE + fT.map.get(s).identifier);
        }
    }

    public static void listInvalid(CommandSender sender) {
        if (FileIO.invalidFileNameList.size() == 0) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "NO ITEM");
        } else if (FileIO.invalidFileNameList.size() == 1) {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + FileIO.invalidFileNameList.size() + " ITEM LISTED:");
        } else {
            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "" + FileIO.invalidFileNameList.size() + " ITEMS LISTED:");
        }
        for (String s : FileIO.invalidFileNameList) {
            sender.sendMessage(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + " FILE_NAME: " + ChatColor.WHITE + s);
        }
    }

    public static void giveItem(CommandSender sender, String[] args, FileType fT) {
        if (args.length>=4) {
            if (fT.map.containsKey(args[3])) {
                if (args.length>=5) {
                    if (Bukkit.getPlayer(args[4])!=null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[4]))) {
                        Player p = Bukkit.getPlayer(args[4]);
                        assert p != null;
                        PlayerData pD = PlayerHandler.getPlayerData(p);
                        if (args.length>=6) {
                            try {
                                int amount = Integer.parseInt(args[5]);
                                if (args.length>=7) {
                                    try {
                                        int index = Integer.parseInt(args[6]);
                                        if (args.length>=8) {
                                            try {
                                                int level = Integer.parseInt(args[7]);
                                                if (level >= 1 && level <= 30) {
                                                    ItemData.sendItemStack((ItemData) fT.map.get(args[3]),pD,amount,index,level);
                                                    sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SEND ITEM TO SLOT WITH LEVEL");
                                                } else {
                                                    sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid level");
                                                }
                                            } catch (Exception ignored) {
                                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid level");
                                            }
                                        } else {
                                            ItemData.sendItemStack((ItemData) fT.map.get(args[3]),pD,amount,index);
                                            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SEND ITEM TO SLOT");
                                        }
                                    } catch (Exception ignored) {
                                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid slot");
                                    }
                                } else {
                                    ItemData.sendItemStack((ItemData) fT.map.get(args[3]),pD,amount);
                                    sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SEND ITEM");
                                }
                            } catch (Exception ignored) {
                                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid amount");
                            }
                        } else {
                            ItemData.sendItemStack((ItemData) fT.map.get(args[3]),pD,1);
                            sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SEND ITEM");
                        }
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        PlayerData pD = PlayerHandler.getPlayerData(p);
                        ItemData.sendItemStack((ItemData) fT.map.get(args[3]),pD,1);
                        sender.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "SEND ITEM");
                    } else {
                        sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid player");
                    }
                }
            } else {
                sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Invalid item name");
            }
        } else {
            sender.sendMessage(SiegeFishing.pluginPrefix + ChatColor.RED + "VARIABLE ERROR - Missing item name");
        }
    }

    public static List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<String>();
        if (args.length == 1) {
            if (completeString("reload", args[0])) {
                s.add("reload");
            }
            if (completeString("item", args[0])) {
                s.add("item");
            }
            if (completeString("util", args[0])) {
                s.add("util");
            }
            return s;
        } else {
            /*
            if (args[0].equalsIgnoreCase("modify")) {
                if (args.length == 2) {
                    for (String l : GunData.gunDataMapper.keySet()) {
                        if (completeString(l, args[1])) {
                            s.add(l);
                        }
                    }
                    return s;
                }
                if (GunData.gunDataMapper.containsKey(args[1])) {
                    if (args.length == 3) {
                        for (int i = 0; i <= 2; i++) {
                            for (int j = 0; j <= 2; j++) {
                                for (int k = 0; k <= 2; k++) {
                                    String l = i + "" + j + "" + k;
                                    if (completeString(l, args[2])) {
                                        s.add(l);
                                    }
                                }
                            }
                        }
                        return s;
                    }
                    if (args.length == 4 && sender.hasPermission("siegefishing.admin")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (completeString(p.getName(), args[3])) {
                                s.add(p.getName());
                            }
                        }
                        return s;
                    }
                }
            }*/
            if (args[0].equalsIgnoreCase("util")) {
                if (args.length == 2) {
                    if (completeString("spawn", args[1])) {
                        s.add("spawn");
                    }
                    if (completeString("upgrade", args[1])) {
                        s.add("upgrade");
                    }
                    return s;
                }
                if (args[1].equalsIgnoreCase("spawn")) {
                    if (args.length == 3) {
                        if (completeString("modding_table", args[2])) {
                            s.add("modding_table");
                        }
                        return s;
                    }
                }
                if (args[1].equalsIgnoreCase("upgrade")) {
                    if (args.length == 3) {
                        s.add("<index>");
                        return s;
                    }
                }
            }
            if (args[0].equalsIgnoreCase("item")) {
                if (args.length == 2) {
                    if (completeString("give", args[1])) {
                        s.add("give");
                    }
                    if (completeString("list", args[1])) {
                        s.add("list");
                    }
                    return s;
                }
                if (args[1].equalsIgnoreCase("give")) {
                    if (args.length == 3) {
                        if (completeString("gun", args[2])) {
                            s.add("gun");
                        }
                        if (completeString("hand", args[2])) {
                            s.add("hand");
                        }
                        if (completeString("mod", args[2])) {
                            s.add("mod");
                        }
                        if (completeString("tool", args[2])) {
                            s.add("tool");
                        }
                        return s;
                    }
                    if (args.length == 4) {
                        if (args[2].matches("gun")) {
                            for (String l : FileType.GUN.map.keySet()) {
                                if (completeString(l, args[3])) {
                                    s.add(l);
                                }
                            }
                            return s;
                        }
                        if (args[2].matches("hand")) {
                            for (String l : FileType.HAND.map.keySet()) {
                                if (completeString(l, args[3])) {
                                    s.add(l);
                                }
                            }
                            return s;
                        }
                        if (args[2].matches("mod")) {
                            for (String l : FileType.MOD.map.keySet()) {
                                if (completeString(l, args[3])) {
                                    s.add(l);
                                }
                            }
                            return s;
                        }
                        if (args[2].matches("tool")) {
                            for (String l : FileType.TOOL.map.keySet()) {
                                if (completeString(l, args[3])) {
                                    s.add(l);
                                }
                            }
                            return s;
                        }

                    }
                    if (FileType.GUN.map.containsKey(args[3])) {
                        if (args.length == 5) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (completeString(p.getName(), args[4])) {
                                    s.add(p.getName());
                                }
                            }
                            return s;
                        }
                        if (Bukkit.getPlayer(args[4]) != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[4]))) {
                            if (args.length == 6) {
                                s.add("<amount>");
                                return s;
                            }
                            try {
                                Integer.parseInt(args[5]);
                                if (args.length == 7) {
                                    s.add("<slot>");
                                    return s;
                                }
                                try {
                                    Integer.parseInt(args[6]);
                                    if (args.length == 8) {
                                        s.add("<level>");
                                        return s;
                                    }
                                } catch (Exception ignored) {
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                if (args[1].equalsIgnoreCase("list")) {
                    if (args.length == 3) {
                        if (completeString("gun", args[2])) {
                            s.add("gun");
                        }
                        if (completeString("hand", args[2])) {
                            s.add("hand");
                        }
                        if (completeString("mod", args[2])) {
                            s.add("mod");
                        }
                        if (completeString("tool", args[2])) {
                            s.add("tool");
                        }
                        if (completeString("particle", args[2])) {
                            s.add("particle");
                        }
                        if (completeString("sound", args[2])) {
                            s.add("particle");
                        }
                        if (completeString("invalid", args[2])) {
                            s.add("invalid");
                        }
                        return s;
                    }
                }
            }
        }
        s.add("");
        return s;
    }

    public static String commandPainter(String[] args) {
        StringBuilder cSB = new StringBuilder();
        cSB.append(ChatColor.GRAY).append("/sw ");
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
