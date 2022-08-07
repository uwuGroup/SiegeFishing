package me.asakura_kukii.siegefishing.handler.inventory;

import me.asakura_kukii.siegefishing.config.data.basic.BasicItemData;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SiegeInventory {
    public static HashMap<UUID, SiegeInventory> openingInventoryMap = new HashMap();
    public static List<UUID> changingInventory = new ArrayList<>();

    public Inventory inv;

    public HashMap<Integer, ItemStack> content;
    public boolean paging;
    public boolean movable;
    public int pageSize = 0;
    public int currentPage = 0;
    public int maxPage = 1;

    public SiegeInventory(String name, List<ItemStack> contentList, boolean movable) {
        HashMap<Integer, ItemStack> content = new HashMap<>();
        int index = 0;
        for (ItemStack iS : contentList) {
            content.put(index, iS);
            index++;
        }
        initiateSiegeInventory(name, 54, content, movable);
    }

    public SiegeInventory(String name, int size, HashMap<Integer, ItemStack> content, boolean movable) {
        initiateSiegeInventory(name, size, content, movable);
    }

    private void initiateSiegeInventory(String name, int size, HashMap<Integer, ItemStack> content, boolean interactable) {
        int invSize = (int) Math.ceil(size / 9.0) * 9;
        int maxIndex = 0;
        for (Integer i : content.keySet()) {
            if (i >= maxIndex) {
                maxIndex = i;
            }
        }
        if (maxIndex + 1 > invSize) {
            this.paging = true;
            this.pageSize = invSize - 9;
            this.maxPage = (int) Math.ceil(((double) (maxIndex + 1)) / pageSize);
        } else {
            this.paging = false;
            this.pageSize = invSize;
        }
        this.content = content;
        this.inv = Bukkit.createInventory(null, invSize, name);
        this.inv = updateSiegeInventory();
        this.movable = interactable;
    }

    public Inventory updateSiegeInventory() {
        inv.clear();
        if (paging) {
            List<ItemStack> buttonList = getButton();
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
        return inv;
    }

    public void showInventoryToPlayer(Player p) {
        openingInventoryMap.remove(p.getUniqueId());
        changingInventory.add(p.getUniqueId());
        openingInventoryMap.put(p.getUniqueId(), this);
        p.openInventory(this.updateSiegeInventory());
    }

    public void nextPage() {
        if (currentPage + 1 < maxPage) {
            currentPage = currentPage + 1;
        }
    }

    public void lastPage() {
        if (currentPage > 0) {
            currentPage = currentPage - 1;
        }
    }

    public static List<ItemStack> getButton () {
        BasicItemData bID0 = (BasicItemData) FileType.forceGetDataFromMap("default_button_0", FileType.BASIC_ITEM);
        BasicItemData bID1 = (BasicItemData) FileType.forceGetDataFromMap("default_button_1", FileType.BASIC_ITEM);
        List<ItemStack> itemStackList = new ArrayList<>();
        ItemStack iS0 = ItemData.getItemStack(bID0, null, 1, 0);
        ItemStack iS1 = ItemData.getItemStack(bID1, null, 1, 0);
        iS0 = NBTHandler.set(iS0, "type", "button", false);
        iS0 = NBTHandler.set(iS0, "id", "last", false);
        iS1 = NBTHandler.set(iS1, "type", "button", false);
        iS1 = NBTHandler.set(iS1, "id", "next", false);
        itemStackList.add(iS0);
        itemStackList.add(iS1);
        return itemStackList;
    }
}
