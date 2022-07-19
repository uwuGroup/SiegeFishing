package me.asakura_kukii.siegefishing.handler.nonitem.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Objects;

public class ParticleHandler {

    public static ParticleData blockDustParticle(Material m) {
        ParticleData pD = new ParticleData("hitEffect", "");
        pD.particle = Particle.BLOCK_DUST;
        pD.c = 5;
        pD.dx = (float) 0.05;
        pD.dy = (float) 0.05;
        pD.dz = (float) 0.05;
        pD.e = 1;
        pD.r = 166;
        pD.g = 0;
        pD.b = 0;
        pD.bx = 0;
        pD.by = 0;
        pD.bz = 0;
        pD.material = m;
        return pD;


    }

    public static void spawnParticle(Location loc, ParticleData pD) {
        Location l = loc.add(pD.bx, pD.by, pD.bz);
        if (pD.particle != null) {
            if(pD.particle == Particle.REDSTONE) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(pD.r, pD.g, pD.b), pD.e);
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.c, pD.dx, pD.dy, pD.dz, pD.e, dust, true);
            } else if ((pD.particle == Particle.BLOCK_CRACK || pD.particle == Particle.BLOCK_DUST || pD.particle == Particle.FALLING_DUST) && pD.material != null) {
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.c, pD.dx, pD.dy, pD.dz, pD.e, pD.material.createBlockData(), true);
            } else {
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.c, pD.dx, pD.dy, pD.dz, pD.e, null,  true);
            }
        }
    }
}
