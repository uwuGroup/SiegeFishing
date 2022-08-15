package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.handler.method.shovel.ShovelTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShovelData extends ItemData {

    public List<Material> digMaterialList = new ArrayList<>();

    public ShovelData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    @Override
    public ItemStack finalizeGenerateItemStack(ItemStack iS, PlayerData pD, int extra) {
        return iS;
    }

    @Override
    public boolean trigger(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        boolean cancel = false;
        switch (iKT) {
            case ITEM_RIGHT_CLICK:
                if (iST.equals(InputSubType.TRIGGER) || iST.equals(InputSubType.EXTRA)) cancel = handleRightClick(triggerSlot, iKT, iST, pD, iS);
                break;
            default:
                break;
        }
        return cancel;
    }

    public boolean handleRightClick(int triggerSlot, InputKeyType iKT, InputSubType iST, PlayerData pD, ItemStack iS) {
        if (ShovelTaskData.shovelTaskDataMap.containsKey(pD.p.getUniqueId())) {
            ShovelTaskData sTD = ShovelTaskData.shovelTaskDataMap.get(pD.p.getUniqueId());
            sTD.interact();
            return true;
        } else {
            ShovelTaskData sTD = new ShovelTaskData(pD, this);
            return true;
        }
    }
}
