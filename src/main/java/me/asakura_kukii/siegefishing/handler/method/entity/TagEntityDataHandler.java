package me.asakura_kukii.siegefishing.handler.method.entity;

import me.asakura_kukii.siegefishing.config.data.addon.*;
import me.asakura_kukii.siegefishing.config.data.basic.ConfigData;
import me.asakura_kukii.siegefishing.handler.method.inventory.*;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TagEntityDataHandler {
    public static void openContainer(EntityContainerData eCD, Player p) {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        try {
            Field f = PlayerData.class.getField("fishBox");
            Object o = pD;
            String name = ConfigData.fishBoxName;
            if (name.contains("%player%")) name = name.replaceAll("%player%", p.getName());
            SiegeInventory sI = new SiegeContainer(pD.p, FormatHandler.format(name, false), pD.fishBox, 54, 8, f, o);
            sI.show(pD.p);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void openShop(EntityShopData eSD, Player p) {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        SiegeInventory sI = new SiegeFishShop(pD.p, FormatHandler.format(eSD.fishShopName, false), 54, eSD.levelPriceMap);
        sI.show(pD.p);
    }

    public static void interactCollector(EntityCollectorData eCD, Player p) {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        String name = ConfigData.fishBookName;
        if (name.contains("%player%")) name = name.replaceAll("%player%", p.getName());
        SiegeInventory sI1 = new SiegeShowcase(pD.p, FormatHandler.format(name, false), FishData.generateHandBook(pD), 54);
        sI1.show(pD.p);
    }

    public static void openMarket(EntityMarketData eMD, Player p) {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        String name = eMD.fishShopName;
        if (name.contains("%player%")) name = name.replaceAll("%player%", p.getName());
        SiegeInventory sI1 = new SiegeShop(pD.p, FormatHandler.format(name, false), 54, eMD.itemDataPriceMap);
        sI1.show(pD.p);
    }
}
