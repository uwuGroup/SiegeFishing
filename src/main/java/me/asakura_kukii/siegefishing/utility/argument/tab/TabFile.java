package me.asakura_kukii.siegefishing.utility.argument.tab;

import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabFile {
    public static List<String> tabFile(CommandSender sender, Argument argument) {
        List<String> sL = new ArrayList<>();
        String s = argument.nextString();
        if (s == null) {
            return sL;
        }
        switch (s) {
            case "list":
                String s1 = argument.nextString();
                if (s1 == null) {
                    return sL;
                }
                sL.addAll(tabType(s1));


                return TabHandler.noTabNext(sL, argument);
            default:
                if (Argument.completeString("list", s)) sL.add("list");
                return TabHandler.noTabNext(sL, argument);
        }
    }

    public static List<String> tabType(String s) {
        List<String> sL = new ArrayList<>();
        if (!FileType.getItemLinkedFileTypeNameList().contains(s)) {
            sL.add("<TYPE>");
            for (String fTN : FileType.getItemLinkedFileTypeNameList()) {
                if (Argument.completeString(fTN, s)) {
                    sL.remove("<TYPE>");
                    sL.add(fTN);
                }
            }
            if (Argument.completeString("invalid", s)) {
                sL.remove("<TYPE>");
                sL.add("invalid");
            }
        }
        return sL;
    }
}
