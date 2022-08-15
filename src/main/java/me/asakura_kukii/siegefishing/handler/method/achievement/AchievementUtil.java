package me.asakura_kukii.siegefishing.handler.method.achievement;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.ExtraStringListData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.config.data.basic.ImageMapData;
import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.particle.ParticleHandler;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NMSHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AchievementUtil {

    enum Fish {
        FISH_0(0.000001, "fish_0"),
        FISH_1(0.25, "fish_1"),
        FISH_2(0.50, "fish_2"),
        FISH_3(0.75, "fish_3"),
        FISH_4(1.00, "fish_4");
        double d;
        String identifier;
        Fish (double d, String s) {
            this.d = d;
            this.identifier = s;
        }
    }

    enum Boat {
        BOAT_0(0.000001, "boat_0"),
        BOAT_1(0.50, "boat_1"),
        BOAT_2(1.00, "boat_2");
        double d;
        String identifier;
        Boat (double d, String s) {
            this.d = d;
            this.identifier = s;
        }
    }

    enum Food {
        FOOD_0(0.000001, "food_0"),
        FOOD_1(0.50, "food_1"),
        FOOD_2(1.00, "food_2");
        double d;
        String identifier;
        Food (double d, String s) {
            this.d = d;
            this.identifier = s;
        }
    }
    enum Region {
        REGION_0(0.0, "region_0"),
        REGION_1(0.25, "region_1"),
        REGION_2(0.50, "region_2"),
        REGION_3(0.75, "region_3"),
        REGION_4(1.00, "region_4");
        double d;
        String identifier;
        Region (double d, String s) {
            this.d = d;
            this.identifier = s;
        }
    }
    enum Total {
        TOTAL_0(0, "total_0"),
        TOTAL_2(0.5, "total_1"),
        TOTAL_3(1, "total_2");
        double d;
        String identifier;
        Total (double d, String s) {
            this.d = d;
            this.identifier = s;
        }
    }

    public static void broadCast(Player p, String s, int range) {
        if (s.contains("%player%")) s = s.replaceAll("%player%", p.getName());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(p.getLocation()) < range || range == 0) {
                player.sendMessage(FormatHandler.format(s, false));
            }
        }
    }

    public static void checkAchievement(PlayerData pD) {
        int fishTotalTypeCount = FileType.FISH.map.size();
        pD.fishPercent = ((double) pD.unlockFishIdentifierMap.size()) / ((double) fishTotalTypeCount);
        int boatTotalTypeCount = FileType.BOAT.map.size();
        pD.boatPercent = ((double) pD.unlockBoatIdentifierList.size()) / ((double) boatTotalTypeCount);
        int foodTotalTypeCount = FileType.FOOD.map.size();
        pD.foodPercent = ((double) pD.unlockFoodIdentifierList.size()) / ((double) foodTotalTypeCount);
        int regionTotalCount = ((ImageMapData) FileType.IMAGE_MAP.map.get("fish_world")).rgbRegionNameMap.size();
        pD.regionPercent = ((double) pD.unlockRegionNameMap.size()) / ((double) regionTotalCount);

        pD.totalPercent = (pD.fishPercent + pD.boatPercent + pD.foodPercent + pD.regionPercent) / 4;

        List<String> unlockAchievementList = new ArrayList<>();

        for (Fish f : Fish.values()) {
            if (pD.fishPercent >= f.d) {
                unlockAchievementList.add(f.identifier);
            }
        }
        for (Boat b : Boat.values()) {
            if (pD.boatPercent >= b.d) {
                unlockAchievementList.add(b.identifier);
            }
        }
        for (Food f : Food.values()) {
            if (pD.foodPercent >= f.d) {
                unlockAchievementList.add(f.identifier);
            }
        }
        for (Region r : Region.values()) {
            if (pD.regionPercent >= r.d) {
                unlockAchievementList.add(r.identifier);
            }
        }
        for (Total t : Total.values()) {
            if (pD.totalPercent >= t.d) {
                unlockAchievementList.add(t.identifier);
            }
        }

        List<String> newAchievementList = new ArrayList<>(unlockAchievementList);
        newAchievementList.removeAll(pD.unlockAchievementList);

        if (newAchievementList.size() > 0) {
            for (String s : newAchievementList) {
                newAchievement(pD,s);
            }
        }
        pD.unlockAchievementList = unlockAchievementList;
    }

    public static void newAchievement(PlayerData pD, String name) {
        String s = ExtraStringListData.get("default_achievement", 0);
        if (s.contains("%player%")) s = s.replaceAll("%player%", pD.p.getName());
        try {
            ParticleHandler.spawnParticleAtLoc(pD.p.getLocation().clone().add(0, 1, 0), (ParticleData) FileType.PARTICLE.map.get("default_achievement"), false);
            SoundHandler.playSoundAtLoc(pD.p.getLocation().clone().add(0, 1, 0), (SoundData) FileType.SOUND.map.get("default_achievement"));
        } catch (Exception ignored) {
        }
        try {
            ItemData iD = (ItemData) FileType.AWARD.map.get(name);
            ItemStack iS = ItemData.getItemStack(iD, pD, 1, 0);
            BaseComponent format = new TextComponent(FormatHandler.format(s, false));
            BaseComponent itemName = new TextComponent(FormatHandler.format(iD.displayName, false));
            BaseComponent[] itemStackComponent = new BaseComponent[]{new TextComponent(NMSHandler.itemToJson(iS))};
            itemName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackComponent));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(format, itemName);
            }
            ItemData.sendItemStack(iD, pD, 1 ,0);
        } catch (Exception ignored) {
        }

    }
}
