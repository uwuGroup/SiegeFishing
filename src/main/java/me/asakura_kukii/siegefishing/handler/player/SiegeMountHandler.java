package me.asakura_kukii.siegefishing.handler.player;

import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SiegeMountHandler {
    public static void communicate(PlayerData pD) {
        ItemStack iS0;
        if (pD.backItemStackList.containsKey(0)) {
            iS0 = pD.backItemStackList.get(0);
        } else {
            iS0 = new ItemStack(Material.AIR);
        }

        ItemStack iS1;
        if (pD.backItemStackList.containsKey(1)) {
            iS1 = pD.backItemStackList.get(1);
        } else {
            iS1 = new ItemStack(Material.AIR);
        }
        me.asakura_kukii.siegemounthandler.api.SiegeMountHandler.putMount(pD.p, iS0, 0);
        me.asakura_kukii.siegemounthandler.api.SiegeMountHandler.putMount(pD.p, iS1, 1);
    }
}
