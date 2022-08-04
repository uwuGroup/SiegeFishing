package me.asakura_kukii.siegefishing.utility.argument.tab;

import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

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
                sL.addAll(TabItem.tabItem(sender, argument));
                return noTabNext(sL, argument);
            case "file":
                sL.addAll(TabFile.tabFile(sender, argument));
                return noTabNext(sL, argument);
            case "reload":
                return noTabNext(sL, argument);
            default:
                if (Argument.completeString("item", s)) sL.add("item");
                if (Argument.completeString("file", s)) sL.add("file");
                if (Argument.completeString("reload", s)) sL.add("reload");

                return noTabNext(sL, argument);
        }
    }
}
