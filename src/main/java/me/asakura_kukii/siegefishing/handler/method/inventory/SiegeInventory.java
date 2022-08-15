package me.asakura_kukii.siegefishing.handler.method.inventory;

import me.asakura_kukii.siegefishing.config.data.basic.BasicItemData;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class SiegeInventory {
    public static HashMap<UUID, SiegeInventory> openingInventoryMap = new HashMap();
    public static List<UUID> changingInventory = new ArrayList<>();

    public Player p;
    public Inventory inv;
    public String name;
    public HashMap<Integer, ItemStack> content;
    public SiegeInventoryType sIT;
    public int size = 54;
    public List<Integer> movableSlot = new ArrayList<>();

    public Field f = null;
    public Object o = null;
    public List<ItemStack> buttonList = new ArrayList<>();


    public SiegeInventory(Player p, String name, List<ItemStack> contentList, int size, SiegeInventoryType sIT) {
        HashMap<Integer, ItemStack> content = new HashMap<>();
        int index = 0;
        for (ItemStack iS : contentList) {
            content.put(index, iS);
            index++;
        }
        this.name = name;
        this.content = content;
        this.sIT = sIT;
        this.size = (int) Math.ceil(size / 9.0) * 9;
        this.p = p;
        buttonList = getButton();
        inv = Bukkit.createInventory(null, size, name);
    }

    public SiegeInventory(Player p, String name, HashMap<Integer, ItemStack> content, int size, SiegeInventoryType sIT) {
        this.name = name;
        this.content = content;
        this.sIT = sIT;
        this.size = (int) Math.ceil(size / 9.0) * 9;
        this.p = p;
        this.inv = Bukkit.createInventory(null, this.size, name);
        buttonList = getButton();
    }

    public abstract void nextPage();

    public abstract void lastPage();

    public abstract void interact(ClickType cT, Integer slot);

    public abstract void close();

    public void show(Player p) {
        if (openingInventoryMap.containsKey(p.getUniqueId())) {
            changingInventory.add(p.getUniqueId());
        }
        openingInventoryMap.remove(p.getUniqueId());
        openingInventoryMap.put(p.getUniqueId(), this);
        p.openInventory(inv);
    }

    public static List<ItemStack> getButton() {
        BasicItemData bID0 = (BasicItemData) FileType.forceGetDataFromMap("default_button_0", FileType.BASIC_ITEM);
        BasicItemData bID1 = (BasicItemData) FileType.forceGetDataFromMap("default_button_1", FileType.BASIC_ITEM);
        BasicItemData bID2 = (BasicItemData) FileType.forceGetDataFromMap("default_button_2", FileType.BASIC_ITEM);
        List<ItemStack> itemStackList = new ArrayList<>();
        ItemStack iS0 = ItemData.getItemStack(bID0, null, 1, 0);
        ItemStack iS1 = ItemData.getItemStack(bID1, null, 1, 0);
        ItemStack iS2 = ItemData.getItemStack(bID2, null, 1, 0);
        iS0 = NBTHandler.set(iS0, "type", "button", false);
        iS0 = NBTHandler.set(iS0, "id", "last", false);
        iS1 = NBTHandler.set(iS1, "type", "button", false);
        iS1 = NBTHandler.set(iS1, "id", "next", false);
        iS2 = NBTHandler.set(iS2, "type", "button", false);
        iS2 = NBTHandler.set(iS2, "id", "sell", false);
        itemStackList.add(iS0);
        itemStackList.add(iS1);
        itemStackList.add(iS2);
        return itemStackList;
    }
}
