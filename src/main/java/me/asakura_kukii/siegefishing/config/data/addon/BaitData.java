package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.shovel.ShovelTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BaitData extends ItemData {

    public double timeBoost = 0.0;
    public double luckBoost = 0.0;
    public ParticleData particleData = new ParticleData("", "", FileType.PARTICLE);
    public SoundData soundData = new SoundData("", "", FileType.SOUND);
    public BaitData(String identifier, String fileName, FileType fT) {
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
                if (s.contains("%time_boost%")) {
                    s = s.replaceAll("%time_boost%", String.format("%.2f", timeBoost*100));
                }
                if (s.contains("%luck_boost%")) {
                    s = s.replaceAll("%luck_boost%", String.format("%.2f", luckBoost));
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
            case ITEM_RIGHT_CLICK:
                if (iST.equals(InputSubType.TRIGGER) || iST.equals(InputSubType.EXTRA)) cancel = handleRightClick(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleRightClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        ItemStack modifiedIS = iS.clone();
        if (modifiedIS.getAmount() > 1) {
            modifiedIS.setAmount(modifiedIS.getAmount() - 1);
            pD.p.getInventory().setItem(triggerSlot, modifiedIS);
        } else {
            pD.p.getInventory().setItem(triggerSlot, new ItemStack(Material.AIR));
        }
        pD.activeBaitData = this;
        try {
            ParticleHandler.spawnParticleAtLoc(pD.p.getLocation().clone().add(new Vector(0, 1.0, 0)), particleData, false);
            SoundHandler.playSoundToPlayer(pD.p, new Vector(0,0,0), soundData);
        } catch (Exception ignored) {

        }
        return true;
    }
}
