package me.asakura_kukii.siegefishing.handler.method.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class SiegeShowcase extends SiegeInventory{
    public int currentPage = 0;
    public boolean paging = false;
    public int pageSize = 0;
    public int maxPage = 0;

    public SiegeShowcase(Player p, String name, List<ItemStack> contentList, int size) {
        super(p, name, contentList, size, SiegeInventoryType.SHOWCASE);
        initiate();
    }

    public SiegeShowcase(Player p, String name, HashMap<Integer, ItemStack> content, int size) {
        super(p, name, content, size, SiegeInventoryType.SHOWCASE);
        initiate();
    }

    public void initiate() {
        int maxIndex = 0;
        for (Integer i : content.keySet()) {
            if (i >= maxIndex) {
                maxIndex = i;
            }
        }
        if (maxIndex + 1 > size) {
            paging = true;
            pageSize = size - 9;
            maxPage = (int) Math.ceil(((double) (maxIndex + 1)) / pageSize);
        } else {
            paging = false;
            pageSize = size;
        }
        update();
    }

    public void update() {
        if (paging) {
            inv.clear();
            if (currentPage != 0) {
                inv.setItem(pageSize, buttonList.get(0));
            }
            if (currentPage != maxPage -1) {
                inv.setItem(pageSize + 8, buttonList.get(1));
            }
        }
        for (int i = currentPage * pageSize; i < (currentPage + 1) * pageSize; i++) {
            int invIndex = i - currentPage * pageSize;
            if (content.containsKey(i)) {
                inv.setItem(invIndex, content.get(i));
            }
        }
    }

    @Override
    public void nextPage() {
        if (currentPage + 1 < maxPage) {
            currentPage = currentPage + 1;
        }
        update();
        show(p);
    }

    @Override
    public void lastPage() {
        if (currentPage > 0) {
            currentPage = currentPage - 1;
        }
        update();
        show(p);
    }

    @Override
    public void interact(ClickType cT, Integer slot) {
        if (!paging || slot == null) return;
        if (slot == pageSize) lastPage();
        if (slot == pageSize + 8) nextPage();
    }

    @Override
    public void close() {
    }
}
