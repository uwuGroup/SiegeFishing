package me.asakura_kukii.siegefishing.handler.effect.sound;

import me.asakura_kukii.siegefishing.config.data.basic.SoundData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class SoundHandler {

    public static void playSoundToPlayer(Player p, Vector bias, SoundData sD) {
        Random r = new Random();
        float volume = (float) (sD.volumeMin + r.nextDouble() * (sD.volumeMax - sD.volumeMin));
        float pitch = (float) (sD.pitchMin + r.nextDouble() * (sD.pitchMax - sD.pitchMin));
        p.playSound(p.getEyeLocation().clone().add(bias.clone()), sD.sound, volume, pitch);

    }

    public static void playSoundAtLoc(Location l, SoundData sD) {
        Random r = new Random();
        float volume = (float) (sD.volumeMin + r.nextDouble() * (sD.volumeMax - sD.volumeMin));
        float pitch = (float) (sD.pitchMin + r.nextDouble() * (sD.pitchMax - sD.pitchMin));
        Objects.requireNonNull(l.getWorld()).playSound(l.clone(), sD.sound, volume, pitch);
    }
}
