package me.asakura_kukii.siegefishing.config.data.addon;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.ItemData;
import me.asakura_kukii.siegefishing.handler.method.boat.BoatHandler;
import me.asakura_kukii.siegefishing.handler.method.shovel.ShovelTaskData;
import me.asakura_kukii.siegefishing.handler.player.input.InputKeyType;
import me.asakura_kukii.siegefishing.handler.player.input.InputSubType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BoatData extends ItemData {

    public int boatCustomModelData = 0;

    public BoatData(String identifier, String fileName, FileType fT) {
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
        BoatHandler.generateBoat(pD, this);
        return true;
    }
}
