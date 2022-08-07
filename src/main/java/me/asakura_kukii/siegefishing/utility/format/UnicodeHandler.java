package me.asakura_kukii.siegefishing.utility.format;

import me.asakura_kukii.siegefishing.config.data.basic.UnicodeData;
import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

public class UnicodeHandler {
    public static String map(String s, boolean wrap) {
        if (s == null) {
            return null;
        }
        if(s.contains("@")) {
            String[] stringSplit = s.split("@");
            boolean wrapStatus = false;
            for(String s2 : stringSplit) {
                if(s2.contains("#")) {
                    String[] s2SplitList = s2.split("#");
                    String s2Split = s2SplitList[0];
                    if (!s2Split.contains("^")) {
                        for (FileData fD : FileType.UNICODE.map.values()) {
                            UnicodeData uD = (UnicodeData) fD;
                            if (uD.map.containsKey(s2Split)) {
                                s = s.replace("@" + s2Split + "#", (String) uD.map.get(s2Split));
                                if (wrap && !wrapStatus && uD.pictureHeightMap != null && uD.pictureHeightMap.containsKey(s2Split)) {
                                    int height = uD.pictureHeightMap.get(s2Split);
                                    int lineCount = (int) Math.ceil(height * 2.0 / 18.00) - 1;
                                    StringBuilder sBuilder = new StringBuilder(s);
                                    for (int i = 0; i < lineCount; i++) {
                                        sBuilder.append("\n");
                                    }
                                    s = sBuilder.toString();
                                    wrapStatus = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return s;
    }
}
