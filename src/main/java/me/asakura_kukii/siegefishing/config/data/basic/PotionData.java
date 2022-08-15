package me.asakura_kukii.siegefishing.config.data.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.data.FileType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionData extends FileData {

    public PotionEffectType potionEffectType = PotionEffectType.ABSORPTION;
    public int duration = 10;
    public int amplifier = 1;
    public boolean ambient = false;
    public boolean particles = false;
    public boolean icon = false;

    public PotionData(String identifier, String fileName, FileType fT) {
        super(identifier, fileName, fT);
    }

    public PotionEffect getPotionEffect() {
        return new PotionEffect(potionEffectType, duration, amplifier, ambient, particles, icon);
    }

    public void apply(LivingEntity e) {
        e.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient, particles, icon));
    }
}
