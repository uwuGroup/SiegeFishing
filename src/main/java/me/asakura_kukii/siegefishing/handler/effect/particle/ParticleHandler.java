package me.asakura_kukii.siegefishing.handler.effect.particle;

import me.asakura_kukii.siegefishing.config.data.basic.ParticleData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ParticleHandler {

    public static ParticleData blockDustParticle(Material m) {
        ParticleData pD = new ParticleData("hitEffect", "", FileType.PARTICLE);
        pD.particle = Particle.BLOCK_DUST;
        pD.count = 5;
        pD.sizeX = (float) 0.05;
        pD.sizeY = (float) 0.05;
        pD.sizeZ = (float) 0.05;
        pD.extra = 1;
        pD.colorR = 166;
        pD.colorG = 0;
        pD.colorB = 0;
        pD.biasX = 0;
        pD.biasY = 0;
        pD.biasZ = 0;
        pD.material = m;
        return pD;
    }

    public static void spawnParticleAtLoc(Location loc, ParticleData pD, boolean force) {
        Location l = loc.add(pD.biasX, pD.biasY, pD.biasZ);
        if (pD.particle != null) {
            if(pD.particle == Particle.REDSTONE) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(pD.colorR, pD.colorG, pD.colorB), pD.extra);
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, dust, force);
            } else if ((pD.particle == Particle.BLOCK_CRACK || pD.particle == Particle.BLOCK_DUST || pD.particle == Particle.FALLING_DUST) && pD.material != null) {
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, pD.material.createBlockData(), force);
            } else {
                Objects.requireNonNull(l.getWorld()).spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, null,  force);
            }
        }
    }

    public static void spawnParticleAtLocForPlayer(Player p, Location loc, ParticleData pD) {
        Location l = loc.add(pD.biasX, pD.biasY, pD.biasZ);
        if (pD.particle != null) {
            if(pD.particle == Particle.REDSTONE) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(pD.colorR, pD.colorG, pD.colorB), pD.extra);
                p.spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, dust);
            } else if ((pD.particle == Particle.BLOCK_CRACK || pD.particle == Particle.BLOCK_DUST || pD.particle == Particle.FALLING_DUST) && pD.material != null) {
                p.spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, pD.material.createBlockData());
            } else {
                p.spawnParticle(pD.particle, l, pD.count, pD.sizeX, pD.sizeY, pD.sizeZ, pD.extra, null);
            }
        }
    }
}
