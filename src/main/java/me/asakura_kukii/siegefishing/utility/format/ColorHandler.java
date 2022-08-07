package me.asakura_kukii.siegefishing.utility.format;


import net.md_5.bungee.api.ChatColor;

public class ColorHandler {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String map(String s) {
        if (s == null) {
            return null;
        }
        if(s.contains("&&")) {
            String[] stringSplit = s.split("&&");
            for(String s2 : stringSplit) {
                try {
                    String colorString = s2.substring(0, 6);
                    ChatColor c = ChatColor.of("#" + colorString);
                    s = s.replaceAll("&&" + colorString, c.toString());
                } catch (Exception ignored) {
                }
            }
        }
        return s;
    }

    public static ChatColor gen(int r, int g, int b) {
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;
        String hexR = Integer.toHexString(r);
        if (hexR.length() < 2) {
            hexR = "0" + hexR;
        }
        String hexG = Integer.toHexString(g);
        if (hexG.length() < 2) {
            hexG = "0" + hexG;
        }
        String hexB = Integer.toHexString(b);
        if (hexB.length() < 2) {
            hexB = "0" + hexB;
        }
        String colorCode = "#" + hexR + hexG + hexB;
        return ChatColor.of(colorCode);
    }
}
