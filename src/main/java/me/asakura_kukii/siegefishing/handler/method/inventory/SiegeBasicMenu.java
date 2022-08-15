package me.asakura_kukii.siegefishing.handler.method.inventory;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import me.asakura_kukii.siegefishing.handler.player.PlayerDataHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SiegeBasicMenu extends SiegeInventory{

    public SiegeBasicMenu(Player p) {
        super(p, "", new HashMap<>(), 9, SiegeInventoryType.SHOWCASE);
        initiate();
    }

    public void initiate() {
        update();
    }

    public void update() {
        PlayerData pD = PlayerDataHandler.getPlayerData(p);
        ItemStack iS = new ItemStack(Material.COOKIE);
        try {
            ItemData iD = (ItemData) FileType.BASIC_ITEM.map.get("default_menu");
            iS = ItemData.getItemStack(iD, pD, 1, 0);
            ItemMeta iM = iS.getItemMeta();
            if (iM != null) {
                String s = iM.getDisplayName();
                if (s.contains("%player%")) s = s.replaceAll("%player%", pD.p.getName());
                iM.setDisplayName(FormatHandler.format(s, false));
                List<String> mSL = new ArrayList<>();
                if (iM.getLore() != null) {
                    for (String s1 : iM.getLore()) {
                        if (s1.contains("%cup%")) {
                            if (pD.unlockAchievementList.contains("total_0")) {
                                s1 = s1.replaceAll("%cup%", "total_0");
                            }

                            if (pD.unlockAchievementList.contains("total_1")) {
                                s1 = s1.replaceAll("%cup%", "total_1");
                            }
                            if (pD.unlockAchievementList.contains("total_2")) {
                                s1 = s1.replaceAll("%cup%", "total_2");
                            }
                        }
                        if (s1.contains("%player%")) s1 = s1.replaceAll("%player%", pD.p.getName());
                        if (s1.contains("balance")) s1 = s1.replaceAll("%balance%", String.format("%.2f", pD.balance));
                        if (s1.contains("%progress_total%")) s1 = s1.replaceAll("%progress_total%", String.format("%.2f", pD.totalPercent * 100));
                        if (s1.contains("%progress_fish%")) s1 = s1.replaceAll("%progress_fish%", String.format("%.2f", pD.fishPercent * 100));
                        if (s1.contains("%progress_boat%")) s1 = s1.replaceAll("%progress_boat%", String.format("%.2f", pD.boatPercent * 100));
                        if (s1.contains("%progress_food%")) s1 = s1.replaceAll("%progress_food%", String.format("%.2f", pD.foodPercent * 100));
                        if (s1.contains("%progress_region%")) s1 = s1.replaceAll("%progress_region%", String.format("%.2f", pD.regionPercent * 100));
                        mSL.add(s1);
                    }
                }
                iM.setLore(FormatHandler.format(mSL));
            }
            iS.setItemMeta(iM);
        } catch (Exception ignored) {
        }
        for (int i = 0; i < size; i++) {
            content.put(i, iS);
            inv.setItem(i, iS);
        }

    }

    @Override
    public void nextPage() {
    }

    @Override
    public void lastPage() {
    }

    @Override
    public void interact(ClickType cT, Integer slot) {
    }

    @Override
    public void close() {
    }
}
