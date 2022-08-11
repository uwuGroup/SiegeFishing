package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishData extends ItemData {
    public double spawnTimeMin;
    public double spawnTimeMax;
    public double weightMin;
    public double weightMax;
    public double difficulty;
    public double annoyingFactor;
    public List<String> collectorDescriptionList;
    public List<String> possibleConversationList;

    public FishData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int level) {

        Random r = new Random();
        double d = r.nextDouble();
        double w = weightMin + d * (weightMax - weightMin);
        String e = getRandomFishEmotion();

        NBTHandler.set(iS, "weight", w, false);
        NBTHandler.set(iS, "emotion", e, false);

        ItemMeta iM = iS.getItemMeta();
        if (iM != null) {
            List<String> loreList = iM.getLore();
            List<String> modifiedLoreList = new ArrayList<>();
            if (loreList != null) {
                for (String s : loreList) {
                    if (s.contains("%jin%")) {
                        s = s.replaceAll("%jin%", number2ChineseCharacter(getRandomFishWeight(w).get(0)));
                    }
                    if (s.contains("%liang%")) {
                        s = s.replaceAll("%liang%", number2ChineseCharacter(getRandomFishWeight(w).get(1)));
                    }
                    if (s.contains("%kg%")) {
                        double d2 = getRandomFishWeight(w).get(2);
                        DecimalFormat df = new DecimalFormat("0.00");
                        s = s.replaceAll("%kg%", df.format(d2));
                    }
                    if (s.contains("%emotion%")) {
                        s = s.replaceAll("%emotion%", e);
                    }
                    if (s.contains("%identifier%")) {
                        s = s.replaceAll("%identifier%", identifier);
                    }
                    modifiedLoreList.add(s);
                }

                iM.setLore(FormatHandler.format(modifiedLoreList));
            }

            iS.setItemMeta(iM);
        }
        return iS;
    }

    @Override
    public boolean trigger(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        return false;
    }

    public static String getRandomFishEmotion() {
        if (FileType.EXTRA_STRING_LIST.map.containsKey("fish_emotion")) {
            ExtraStringListData eSLD = (ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get("fish_emotion");
            int i = eSLD.extraStringList.size();
            Random r = new Random();
            return FormatHandler.format(eSLD.extraStringList.get(r.nextInt(i)), false);
        }
        return "";
    }

    public static List<Double> getRandomFishWeight(double d) {
        int jin = (int) Math.floor(d / 0.5);
        int liang = (int) Math.floor((d - jin * 0.5) / 0.05);
        List<Double> weightList = new ArrayList<>();
        weightList.add((double) jin);
        weightList.add((double) liang);
        weightList.add(d);
        return weightList;
    }

    public static String number2ChineseCharacter(double d) {
        int index = (int) Math.floor(d - 10 * Math.floor(d / 10));
        String[] chineseCharacter = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return chineseCharacter[index];
    }
}
