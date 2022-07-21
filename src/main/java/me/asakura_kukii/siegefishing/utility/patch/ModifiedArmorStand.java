package me.asakura_kukii.siegefishing.utility.patch;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Objects;


public class ModifiedArmorStand extends ArmorStand {

    public static Entity SpawnModifiedArmorStand(Location l) {
        ModifiedArmorStand mAS = new ModifiedArmorStand(l);
        ServerLevel world = ((CraftWorld) Objects.requireNonNull(l.getWorld())).getHandle();
        world.tryAddFreshEntityWithPassengers(mAS);
        return (org.bukkit.entity.ArmorStand) mAS.getBukkitEntity();
    }

    public ModifiedArmorStand(Location l) {
        super(((CraftWorld) Objects.requireNonNull(l.getWorld())).getHandle(), l.getX(), l.getY(), l.getZ());
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }
}
