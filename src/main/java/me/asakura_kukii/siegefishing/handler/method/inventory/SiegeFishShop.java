package me.asakura_kukii.siegefishing.handler.method.inventory;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
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
import org.bukkit.util.Vector;

import java.util.HashMap;

public class SiegeFishShop extends SiegeInventory{

    public HashMap<Integer, Double> levelPriceMap;

    public SiegeFishShop(Player p, String name, int size, HashMap<Integer, Double> levelPriceMap) {
        super(p, name, new HashMap<>(), size, SiegeInventoryType.FISH_SHOP);
        this.levelPriceMap = levelPriceMap;
        initiate();
    }

    public void initiate() {
        for (int i = 0; i < size - 1; i++) movableSlot.add(i);
        update();
    }

    private void update() {
        inv.clear();
        inv.setItem(size - 1, buttonList.get(2));
        for (int i = 0; i < size - 1; i++) {
            if (content.containsKey(i)) {
                inv.setItem(i, content.get(i));
            }
        }
    }

    private void sell() {
        double income = 0;
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        for (int i = 0; i < size - 1; i++) {
            ItemStack iS = content.get(i);
            if (ItemData.getItemData(iS) instanceof FishData) {
                FishData fD = (FishData) ItemData.getItemData(iS);
                if (!(boolean) NBTHandler.get(iS, "special", Boolean.class)) {
                    double weight = (double) NBTHandler.get(iS, "weight", Double.class);
                    assert fD != null;
                    if (levelPriceMap.containsKey(fD.rarityLevel)) {
                        income = income + levelPriceMap.get(fD.rarityLevel) * weight * iS.getAmount();
                        content.remove(i);
                    }
                } else {
                    pD.p.sendMessage(FormatHandler.format("&8@description#&7「&8&l" + pD.p.getName() + "&7」&7心系大海的鱼拒绝了被出售！", false));
                }
            }
        }

        pD.balance = pD.balance + income;
        if (income != 0) {
            pD.p.sendMessage(FormatHandler.format("&8@description#&7「&8&l" + pD.p.getName() + "&7」&7出售获得 &e" + String.format("%.2f", income) + "&7鱼！", false));
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
        HashMap<Integer, ItemStack> modifiedContent = new HashMap<>(content);
        for (int i = 0; i < size - 1; i++) {
            ItemStack iS = inv.getItem(i);
            modifiedContent.remove(i);
            if (iS == null || iS.getType().equals(Material.AIR)) {
                continue;
            }
            modifiedContent.put(i, iS);
        }
        content = modifiedContent;
    }

    @Override
    public void nextPage() {
    }

    @Override
    public void lastPage() {
    }

    @Override
    public void interact(ClickType cT, Integer slot) {
        save();
        if (slot == null) return;
        if (slot == size - 1) sell();
    }

    @Override
    public void close() {
        save();
        for (int i = 0; i < size - 1; i++) {
            ItemStack iS = inv.getItem(i);
            if (iS != null) {
                if (p.getInventory().firstEmpty() != -1) {
                    p.getInventory().setItem(p.getInventory().firstEmpty(), iS);
                } else {
                    p.getWorld().dropItem(p.getEyeLocation(), iS);
                }
            }
        }
    }
}
