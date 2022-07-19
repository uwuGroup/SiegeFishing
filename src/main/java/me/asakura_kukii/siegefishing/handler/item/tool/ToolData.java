package me.asakura_kukii.siegefishing.handler.item.tool;

import me.asakura_kukii.siegefishing.handler.item.ItemData;
import me.asakura_kukii.siegefishing.handler.nonitem.player.PlayerData;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolData extends ItemData {
    public String usage;

    public ToolData(String identifier, String fileName){
        this.identifier = identifier;
        this.fileName = fileName;
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS, PlayerData pD, int level) {
        iS = NBTHandler.set(iS, "type", "tool", false);
        iS = NBTHandler.set(iS, "u", this.usage, false);
        ItemMeta iM = iS.getItemMeta();
        assert iM != null;
        iM.setCustomModelData(this.customModelIndex);
        iS.setItemMeta(iM);
        return iS;
    }

    public static ToolData getData(ItemStack iS) {
        if (NBTHandler.hasSiegeWeaponCompoundTag(iS) && NBTHandler.contains(iS, "type") && NBTHandler.contains(iS, "id")) {
            if (((String) NBTHandler.get(iS, "type", String.class)).matches("tool")) {
                if (FileType.TOOL.map.containsKey((String) NBTHandler.get(iS, "id", String.class))) {
                    return (ToolData) FileType.TOOL.map.get((String) NBTHandler.get(iS, "id", String.class));
                }
            }
        }
        return null;
    }
}
