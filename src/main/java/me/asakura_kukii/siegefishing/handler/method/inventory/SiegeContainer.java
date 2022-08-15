package me.asakura_kukii.siegefishing.handler.method.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class SiegeContainer extends SiegeInventory{
    public int currentPage = 0;
    public boolean paging = false;
    public int pageSize = 0;
    public int maxPage = 0;
    public Field f;
    public Object o;

    public SiegeContainer(Player p, String name, HashMap<Integer, ItemStack> content, int size, int maxPage, Field f, Object o) {
        super(p, name, content, size, SiegeInventoryType.CONTAINER);
        this.maxPage = maxPage;
        this.f = f;
        this.o = o;
        initiate();
    }

    public void initiate() {
        if (maxPage > 1) {
            paging = true;
            pageSize = size - 9;
        } else {
            paging = false;
            pageSize = size;
        }
        for (int i = 0; i < pageSize; i++) movableSlot.add(i);
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
    public void nextPage() {
        if (currentPage + 1 < maxPage) {
            currentPage = currentPage + 1;
        }
        update();
        show(p);
    }

    public void lastPage() {
        if (currentPage > 0) {
            currentPage = currentPage - 1;
        }
        update();
        show(p);
    }

    @Override
    public void interact(ClickType cT, Integer slot) {
        save();
        if (!paging || slot == null) return;
        if (slot == pageSize) lastPage();
        if (slot == pageSize + 8) nextPage();
    }

    @Override
    public void close() {
        save();
    }

    public void save() {
        HashMap<Integer, ItemStack> modifiedContent = new HashMap<>(content);
        for (int i = 0; i < pageSize; i++) {
            int index = currentPage * pageSize + i;
            ItemStack iS = inv.getItem(i);
            modifiedContent.remove(index);
            if (iS == null || iS.getType().equals(Material.AIR)) {
                continue;
            }
            modifiedContent.put(index, iS);
        }
        content = modifiedContent;
        try {
            f.set(o, modifiedContent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
