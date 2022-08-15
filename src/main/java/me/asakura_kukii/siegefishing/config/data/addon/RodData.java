package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.method.achievement.AchievementUtil;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RodData extends ItemData {
    public double avgWaitTime = 600;
    public double luckBoost = 0;
    public double maxPressure = 20;
    public double swingVelocity = 0.4;
    public double rodEndBiasX = 0;
    public double rodEndBiasY = 0;
    public double rodEndBiasZ = 0;
    public ParticleData stringParticleData = new ParticleData("", "", FileType.PARTICLE);
    public ParticleData hookParticleData = new ParticleData("", "", FileType.PARTICLE);

    public RodData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }
    //public HashMap<, Double>

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra) {

        ItemMeta iM = iS.getItemMeta();
        if (iM != null) {

            List<String> loreList = iM.getLore();


            List<String> modifiedLoreList = new ArrayList<>();
            for (String s : loreList) {
                if (s.contains("%luck_boost%")) {
                    s = s.replaceAll("%luck_boost%", String.format("%.2f", luckBoost));
                }
                if (s.contains("%max_pressure%")) {
                    s = s.replaceAll("%max_pressure%", String.format("%.2f", maxPressure));
                }
                if (s.contains("%wait_time%")) {
                    s = s.replaceAll("%wait_time%", String.format("%.2f", avgWaitTime / 20.0));
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
            case ITEM_LEFT_CLICK:
                if (iST.equals(InputSubType.TRIGGER)) cancel = handleLeftClick(triggerSlot, iKT, iST, pD, iS);
                break;
            case ITEM_RIGHT_CLICK:
                if (iST.equals(InputSubType.HOLD)) cancel = handleRightClick(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleLeftClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        if (FishingTaskData.fishingTaskMap.containsKey(pD.p.getUniqueId())) {
            FishingTaskData fTD = FishingTaskData.fishingTaskMap.get(pD.p.getUniqueId());
            SoundHandler.playSoundAtLoc(fTD.pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_retrieve"));
            fTD.finish();
        } else {
            if (pD.energy < 0.1) {
                try {
                    AchievementUtil.broadCast(pD.p, ExtraStringListData.random("default_energy"), 16);
                } catch (Exception ignored) {
                }
                return true;
            }
            FishingTaskData fTD = new FishingTaskData(pD, this, iS);
            if (fTD.timer == 0) {
                SoundHandler.playSoundAtLoc(fTD.pD.p.getLocation(), (SoundData) FileType.SOUND.map.get("default_throw"));
            }
        }
        return true;
    }

    public boolean handleRightClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        if (FishingTaskData.fishingTaskMap.containsKey(pD.p.getUniqueId())) {
            FishingTaskData fTD = FishingTaskData.fishingTaskMap.get(pD.p.getUniqueId());
            if (pD.p.isSneaking()) {
                fTD.operateString(true);
            } else {
                fTD.operateString(false);
            }
        } else {
            sendHotBarMsg(pD);
        }
        return true;
    }

    public void sendHotBarMsg(PlayerData pD) {
        List<String> hotBarFormat = ExtraStringListData.getList("default_fishing_hotbar");
        String format = hotBarFormat.get(0);
        if (format.contains("%bait_identifier%")) {
            if (pD.activeBaitData.identifier.equalsIgnoreCase("")) {
                format = "";
            } else {
                format = format.replaceAll("%bait_identifier%", pD.activeBaitData.identifier);
            }
        }
        if (format.contains("%bait_time_boost%")) {
            format = format.replaceAll("%bait_time_boost%", pD.activeBaitData.timeBoost + "");
        }
        if (format.contains("%bait_luck_boost%")) {
            format = format.replaceAll("%bait_luck_boost%", pD.activeBaitData.luckBoost + "");
        }
        pD.p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(FormatHandler.format(format, false)));
    }
}
