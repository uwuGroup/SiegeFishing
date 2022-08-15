package me.asakura_kukii.siegefishing.handler.method.inventory;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.*;
import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import me.asakura_kukii.siegefishing.handler.effect.sound.SoundHandler;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SiegeShop extends SiegeInventory{

    public HashMap<ItemData, Double> itemDataPriceMap;

    public SiegeShop(Player p, String name, int size, HashMap<ItemData, Double> itemDataPriceMap) {
        super(p, name, new HashMap<>(), size, SiegeInventoryType.FISH_SHOP);
        this.itemDataPriceMap = itemDataPriceMap;
        initiate();
    }

    public void initiate() {
        int index = 0;
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        List<ItemData> itemDataList = new ArrayList<>(itemDataPriceMap.keySet());
        itemDataList.sort(new Comparator<ItemData>() {
            @Override
            public int compare(ItemData o1, ItemData o2) {
                return o1.identifier.compareTo(o2.identifier);
            }
        });

        for (ItemData iD : itemDataList) {
            ItemStack iS = ItemData.getItemStack(iD, pD, 1, 1);
            ItemMeta iM = iS.getItemMeta();
            assert iM != null;
            List<String> loreList = iM.getLore();
            List<String> modifiedLoreList = new ArrayList<>();
            modifiedLoreList.add("");
            modifiedLoreList.add("&f@placeholder_2#&f&l@rarity# &f&l物品价格 " + String.format("%.2f", itemDataPriceMap.get(iD)) + " 鱼");
            assert loreList != null;
            modifiedLoreList.addAll(loreList);
            iM.setLore(FormatHandler.format(modifiedLoreList));
            iS.setItemMeta(iM);
            content.put(index, iS);
            index++;
        }
        update();
    }

    private void update() {
        inv.clear();
        for (int i = 0; i < size; i++) {
            if (content.containsKey(i)) {
                inv.setItem(i, content.get(i));
            }
        }
    }

    private void buy(int slot) {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        ItemStack iS = inv.getItem(slot);
        ItemData iD = ItemData.getItemData(iS);
        if (iD == null) return;
        double price = itemDataPriceMap.get(iD);
        if (pD.balance > price) {
            pD.balance = pD.balance - price;
            ItemData.sendItemStack(iD, pD, 1, 0);
            if (iD instanceof BoatData) {
                if (!pD.unlockBoatIdentifierList.contains(iD.identifier)) {
                    pD.unlockBoatIdentifierList.add(iD.identifier);
                }

            }
            if (iD instanceof RodData) {
                if (!pD.unlockRodIdentifierList.contains(iD.identifier)) {
                    pD.unlockRodIdentifierList.add(iD.identifier);
                }

            }
            if (iD instanceof FoodData) {
                if (!pD.unlockFoodIdentifierList.contains(iD.identifier)) {
                    pD.unlockFoodIdentifierList.add(iD.identifier);
                }
            }
            pD.p.sendMessage(FormatHandler.format("&8@description#&7「&8&l" + pD.p.getName() + "&7」&7您消费了 &e" + String.format("%.2f", price) + "&7鱼！", false));
            pD.p.sendMessage(FormatHandler.format("&8@description#&7「&8&l" + pD.p.getName() + "&7」&7现在共有 &e" + String.format("%.2f", pD.balance) + "&7鱼！", false));
            try {
                SoundHandler.playSoundToPlayer(pD.p, new Vector(0, 1, 0), (SoundData) FileType.SOUND.map.get("default_success"));
            } catch (Exception ignored) {

            }
        } else {
            try {
                SoundHandler.playSoundToPlayer(pD.p, new Vector(0, 1, 0), (SoundData) FileType.SOUND.map.get("default_failure"));
            } catch (Exception ignored) {

            }
        }
        update();
        show(p);
    }

    public void save() {
    }

    @Override
    public void nextPage() {
    }

    @Override
    public void lastPage() {
    }

    @Override
    public void interact(ClickType cT, Integer slot) {
        if (slot == null) return;
        if (cT.equals(ClickType.LEFT)) {
            buy(slot);
        }
    }

    @Override
    public void close() {
        save();
    }
}
