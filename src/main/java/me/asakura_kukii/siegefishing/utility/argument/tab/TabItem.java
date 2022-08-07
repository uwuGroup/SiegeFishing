package me.asakura_kukii.siegefishing.utility.argument.tab;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.utility.argument.Argument;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabItem {
    public static List<String> tabItem(CommandSender sender, Argument argument) {
        List<String> sL = new ArrayList<>();
        String s = argument.nextString();
        if (s == null) {
            return sL;
        }
        switch (s) {
            case "list":
            case "view":
                String s1 = argument.nextString();
                if (s1 == null) {
                    return sL;
                }
                sL.addAll(tabType(s1));


                return TabHandler.noTabNext(sL, argument);
            case "give":
                String s2 = argument.nextString();
                if (s2 == null) {
                    return sL;
                }
                sL.addAll(tabType(s2));


                if (!tabType(s2).isEmpty()) {
                    return sL;
                }
                FileType fT = FileType.getFileTypeFromName(s2);
                String s3 = argument.nextString();
                if (s3 == null) {
                    return sL;
                }
                assert fT != null;
                sL.addAll(tabIdentifier(fT, s3));


                return TabHandler.noTabNext(sL, argument);
            default:
                if (Argument.completeString("list", s)) sL.add("list");
                if (Argument.completeString("view", s)) sL.add("view");
                if (Argument.completeString("give", s)) sL.add("give");
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
        }
        return sL;
    }

    public static List<String> tabIdentifier(FileType fT, String s) {
        List<String> sL = new ArrayList<>();
        assert fT != null;
        if (!fT.map.containsKey(s)) {
            sL.add("<IDENTIFIER>");
            for (FileData fD : fT.map.values()) {
                if (Argument.completeString(fD.identifier, s)) {
                    sL.remove("<IDENTIFIER>");
                    sL.add(fD.identifier);
                }
            }
        }
        return sL;
    }
}
