package me.asakura_kukii.siegefishing.handler.method.entity;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.EntityCollectorData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityContainerData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityMarketData;
import me.asakura_kukii.siegefishing.config.data.addon.EntityShopData;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class TagEntityDataListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onEntityInteractAt(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        List<Entity> checkList = e.getRightClicked().getNearbyEntities(2, 2, 2);
        checkList.add(e.getRightClicked());
        for (Entity entity : checkList) {
            for (FileData fD : FileType.ENTITY_CONTAINER_DATA_FILE.map.values()) {
                EntityContainerData eCD = (EntityContainerData) fD;
                if (entity.getScoreboardTags().contains(eCD.tag)) {
                    TagEntityDataHandler.openContainer(eCD, p);
                }
            }
            for (FileData fD : FileType.ENTITY_COLLECTOR_DATA_FILE.map.values()) {
                EntityCollectorData eCD = (EntityCollectorData) fD;
                if (entity.getScoreboardTags().contains(eCD.tag)) {
                    TagEntityDataHandler.interactCollector(eCD, p);
                }
            }
            for (FileData fD : FileType.ENTITY_SHOP_DATA_FILE.map.values()) {
                EntityShopData eSD = (EntityShopData) fD;
                if (entity.getScoreboardTags().contains(eSD.tag)) {
                    TagEntityDataHandler.openShop(eSD, p);
                }
            }
            for (FileData fD : FileType.ENTITY_MARKET_DATA_FILE.map.values()) {
                EntityMarketData eMD = (EntityMarketData) fD;
                if (entity.getScoreboardTags().contains(eMD.tag)) {
                    TagEntityDataHandler.openMarket(eMD, p);
                }
            }
        }
    }
}

