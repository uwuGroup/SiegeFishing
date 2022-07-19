package me.asakura_kukii.siegefishing.utility.format;

import me.asakura_kukii.siegefishing.loader.common.FileType;

public class UnicodeHandler {
    public static String map(String s) {
        if(s.contains("@")) {
            String[] stringSplit = s.split("@");
            for(String s2 : stringSplit) {
                if(s2.contains("#")) {
                    String[] s2SplitList = s2.split("#");
                    String s2Split = s2SplitList[0];
                    if (!s2Split.contains("^")) {
                        if (FileType.UNICODE.subMap.containsKey(s2Split)) {
                            s = s.replace("@" + s2Split + "#", (String) FileType.UNICODE.subMap.get(s2Split));
                        }
                    }
                }
            }
        }
        return s;
    }

    public static String numberStringToUnicode(String s, int length) {
        char[] charList = s.toCharArray();
        if (length > charList.length) {
            StringBuilder sBuilder = new StringBuilder(s);
            for (int i = 0; i < length - charList.length; i++) {
                sBuilder.insert(0, "_");
            }
            s = sBuilder.toString();
        }
        charList = s.toCharArray();

        for (char c: charList) {
            try {
                s = s.replace("" + c, (String) FileType.UNICODE.subMap.get("u" + c));
            } catch(Exception ignored) {
            }
        }

        return s;
    }
}
