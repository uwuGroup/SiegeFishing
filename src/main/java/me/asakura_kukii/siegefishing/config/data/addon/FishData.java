package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FishData extends ItemData {
    public List<String> extraItemLoreList = new ArrayList<>();
    public List<String> extraTypeLoreList = new ArrayList<>();
    public int rarityLevel;
    public double weightMin;
    public double weightMax;
    public List<String> collectorDescriptionList;
    public List<String> possibleConversationList;

    public FishData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra) {

        Random r = new Random();
        double d = r.nextDouble();
        double w = weightMin + d * (weightMax - weightMin);
        String e = getRandomFishEmotion();

        NBTHandler.set(iS, "weight", w, false);
        NBTHandler.set(iS, "emotion", e, false);

        ItemMeta iM = iS.getItemMeta();
        if (iM != null) {
            String displayName = iM.getDisplayName();
            if (displayName.contains("%rarity_color%")) {
                displayName = displayName.replaceAll("%rarity_color%", ExtraStringListData.get("default_fish_rarity_color", rarityLevel));
            }
            iM.setDisplayName(FormatHandler.format(displayName, false));

            List<String> loreList = iM.getLore();
            if (loreList == null) loreList = new ArrayList<>();
            if (extra == 0) {
                loreList.addAll(this.extraItemLoreList);
            } else if (extra == 1) {
                loreList.addAll(this.extraTypeLoreList);
            }


            List<String> modifiedLoreList = new ArrayList<>();
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
                if (s.contains("%rarity_string%")) {
                    s = s.replaceAll("%rarity_string%", ExtraStringListData.get("default_fish_rarity_string", rarityLevel));
                }
                modifiedLoreList.add(s);
            }

            iM.setLore(FormatHandler.format(modifiedLoreList));
            if (checkSpecialEmotion(e)) {
                iM.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                NBTHandler.set(iS, "special", true, false);
            } else {
                NBTHandler.set(iS, "special", false, false);
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
        return ExtraStringListData.random("default_fish_emotion");
    }

    public static boolean checkSpecialEmotion(String s) {
        return ExtraStringListData.contains("default_fish_emotion_special", s);
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
        StringBuilder sB = new StringBuilder();
        Integer i = (int) Math.floor(d);
        for (char c : i.toString().toCharArray()) {
            switch (c) {
                case '0':
                    sB.append("零");
                    break;
                case '1':
                    sB.append("一");
                    break;
                case '2':
                    sB.append("二");
                    break;
                case '3':
                    sB.append("三");
                    break;
                case '4':
                    sB.append("四");
                    break;
                case '5':
                    sB.append("五");
                    break;
                case '6':
                    sB.append("六");
                    break;
                case '7':
                    sB.append("七");
                    break;
                case '8':
                    sB.append("八");
                    break;
                case '9':
                    sB.append("九");
                    break;
            }
        }
        return sB.toString();
    }

    public static List<ItemStack> generateHandBook(PlayerData pD) {
        List<ItemStack> iSL = new ArrayList<>();
        for (String s : FileType.FISH.map.keySet()) {
            ItemStack iS;
            if (pD.unlockFishIdentifierMap.containsKey(s)) {
                iS = getItemStack((ItemData) FileType.FISH.map.get(s), pD, 1, 1);
                ItemMeta iM = iS.getItemMeta();
                List<String> modifiedLoreList = new ArrayList<>();
                if (iM != null && iM.getLore() != null) {
                    for (String s1 : Objects.requireNonNull(iM.getLore())) {
                        if (s1.contains("%count%")) {
                            s1 = s1.replaceAll("%count%", String.valueOf(pD.unlockFishIdentifierMap.get(s)));
                        }
                        modifiedLoreList.add(s1);
                    }
                    iM.setLore(FormatHandler.format(modifiedLoreList));
                    if (pD.specialFishIdentifierMap.containsKey(s)) {
                        iM.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    }
                }
                iS.setItemMeta(iM);

            } else {
                if (FileType.BASIC_ITEM.map.containsKey("default_unknown")) {
                    iS = getItemStack((ItemData) FileType.BASIC_ITEM.map.get("default_unknown"), pD, 1, 0);
                } else {
                    iS = new ItemStack(Material.COOKIE);
                }
            }
            iSL.add(iS);
        }
        return iSL;
    }
}
