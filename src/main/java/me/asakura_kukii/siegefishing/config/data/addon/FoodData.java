package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.basic.PotionData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodData extends ItemData {

    public double energy;
    public PotionData potionData;

    public FoodData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra) {

        ItemMeta iM = iS.getItemMeta();
        if (iM != null) {

            List<String> loreList = iM.getLore();
            if (loreList == null) loreList = new ArrayList<>();
            List<String> modifiedLoreList = new ArrayList<>();
            for (String s : loreList) {
                if (s.contains("%identifier%")) {
                    s = s.replaceAll("%identifier%", identifier);
                }
                if (s.contains("%energy%")) {
                    s = s.replaceAll("%energy%", String.format("%.2f", energy*100));
                }
                modifiedLoreList.add(s);
            }

            iM.setLore(FormatHandler.format(modifiedLoreList));
            iS.setItemMeta(iM);

        }
        return iS;
    }

    @Override
    public boolean trigger(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        boolean cancel = false;
        switch (iKT) {
            case ITEM_CONSUME:
                if (iST.equals(InputSubType.TRIGGER)) cancel = handleConsume(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleConsume(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        pD.energy = pD.energy + this.energy;
        if (pD.energy > 1) {
            pD.energy = 1;
        }
        return false;
    }
}
