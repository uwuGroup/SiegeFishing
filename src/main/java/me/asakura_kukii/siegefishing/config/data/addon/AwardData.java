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
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AwardData extends ItemData {

    public ParticleData particleData = new ParticleData("", "", FileType.PARTICLE);
    public SoundData soundData = new SoundData("","",FileType.SOUND);


    public AwardData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra) {

        Random r = new Random();
        ItemMeta iM = iS.getItemMeta();
        if (iM != null) {
            List<String> loreList = iM.getLore();
            List<String> modifiedLoreList = new ArrayList<>();
            for (String s : loreList) {
                if (s.contains("%identifier%")) {
                    s = s.replaceAll("%identifier%", identifier);
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
                if (iST.equals(InputSubType.TRIGGER) || iST.equals(InputSubType.HOLD)) cancel = handleRightClick(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleRightClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        Location l = pD.p.getLocation().add(new Vector(0, 0.5, 0));
        try {
            ParticleHandler.spawnParticleAtLoc(l, this.particleData, false);
            SoundHandler.playSoundAtLoc(l, this.soundData);
        } catch (Exception ignored) {
        }
        return true;
    }
}
