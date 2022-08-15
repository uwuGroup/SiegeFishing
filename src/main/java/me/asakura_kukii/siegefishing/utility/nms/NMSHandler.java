package me.asakura_kukii.siegefishing.utility.nms;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.Format;
import java.util.Random;

public class NMSHandler {
    public static void blockDestructionPacket(Player p, Location l, Integer stage) {
        Random r = new Random();
        ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(r.nextInt(), new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()), stage);
        sendPacket(p, packet);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            ServerPlayer ep = ((CraftPlayer)player).getHandle();
            ep.connection.send((Packet<?>) packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String itemToJson(ItemStack itemStack) throws RuntimeException {
        CompoundTag nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        net.minecraft.world.item.ItemStack nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        CompoundTag itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = new CompoundTag();
            nmsItemStackObj = CraftItemStack.asNMSCopy(itemStack);
            itemAsJsonObject = nmsItemStackObj.save(nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            throw new RuntimeException("failed to serialize itemstack to nms item", t);
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }
}
