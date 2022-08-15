package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtraStringListData extends FileData {
    public List<String> extraStringList = new ArrayList<>();

    public ExtraStringListData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    public String random() {
        Random r = new Random();
        if (extraStringList.size() == 0) return "";
        int index = (int) Math.floor(extraStringList.size() * r.nextDouble());
        if (index < 0) index = 0;
        if (index > extraStringList.size() - 1) index = extraStringList.size() - 1;
        return extraStringList.get(index);
    }

    public boolean contains(String s) {
        return extraStringList.contains(s);
    }

    public String get(int index) {
        if (index < 0) return "";
        if (index > extraStringList.size() - 1) return "";
        return extraStringList.get(index);
    }

    public static String get(String name, int index) {
        if (FileType.EXTRA_STRING_LIST.map.containsKey(name)) {
            return ((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get(name)).get(index);
        }
        return "";
    }

    public static List<String> getList(String name) {
        if (FileType.EXTRA_STRING_LIST.map.containsKey(name)) {
            return ((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get(name)).extraStringList;
        }
        return new ArrayList<>();
    }

    public static boolean contains(String name, String s) {
        if (FileType.EXTRA_STRING_LIST.map.containsKey(name)) {
            return ((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get(name)).contains(s);
        }
        return false;
    }

    public static String random(String name) {
        if (FileType.EXTRA_STRING_LIST.map.containsKey(name)) {
            return ((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get(name)).random();
        }
        return "";
    }
}
