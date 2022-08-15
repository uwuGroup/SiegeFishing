package me.asakura_kukii.siegefishing.utility.argument.tab;

import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static me.asakura_kukii.siegefishing.SiegeFishing.pluginName;

public class TabHandler {
    public static List<String> noTabNext(List<String> sL, Argument argument) {
        if (argument.nextString() != null) {
            sL.clear();
        }
        return sL;
    }

    public static List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        Argument argument = new Argument(args);
        List<String> sL = new ArrayList<>();
        String s = argument.nextString();
        if (s == null) {
            return new ArrayList<>();
        }
        switch (s) {
            case "item":
                if (sender.hasPermission(pluginName + ".item")) {
                    sL.addAll(TabItem.tabItem(sender, argument));
                    return noTabNext(sL, argument);
                }

            case "file":
                if (sender.hasPermission(pluginName + ".file")) {
                    sL.addAll(TabFile.tabFile(sender, argument));
                    return noTabNext(sL, argument);
                }
            case "reload":
                if (sender.hasPermission(pluginName + ".reload")) {
                    return noTabNext(sL, argument);
                }
            default:
                if (sender.hasPermission(pluginName + ".item") && Argument.completeString("item", s)) sL.add("item");
                if (sender.hasPermission(pluginName + ".file") && Argument.completeString("file", s)) sL.add("file");
                if (sender.hasPermission(pluginName + ".reload") && Argument.completeString("reload", s)) sL.add("reload");
                if (Argument.completeString("show", s)) sL.add("show");
                if (Argument.completeString("back", s)) sL.add("back");
                return noTabNext(sL, argument);
        }
    }
}
