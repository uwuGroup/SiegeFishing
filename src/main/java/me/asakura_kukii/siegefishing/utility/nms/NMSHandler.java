package me.asakura_kukii.siegefishing.utility.nms;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
}
