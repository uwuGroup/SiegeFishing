package me.asakura_kukii.siegefishing.utility.format;

import me.asakura_kukii.siegefishing.data.basic.UnicodeData;
import me.asakura_kukii.siegefishing.data.common.FileData;
import me.asakura_kukii.siegefishing.io.loader.common.FileType;

public class UnicodeHandler {
    public static String map(String s) {
        if (s == null) {
            return null;
        }
        if(s.contains("@")) {
            String[] stringSplit = s.split("@");
            for(String s2 : stringSplit) {
                if(s2.contains("#")) {
                    String[] s2SplitList = s2.split("#");
                    String s2Split = s2SplitList[0];
                    if (!s2Split.contains("^")) {
                        for (FileData fD : FileType.UNICODE.map.values()) {
                            UnicodeData uD = (UnicodeData) fD;
                            if (uD.map.containsKey(s2Split)) {
                                s = s.replace("@" + s2Split + "#", (String) uD.map.get(s2Split));
                            }
                        }
                    }
                }
            }
        }
        return s;
    }
}
