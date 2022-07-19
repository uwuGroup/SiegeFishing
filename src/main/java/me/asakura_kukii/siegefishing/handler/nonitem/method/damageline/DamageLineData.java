package me.asakura_kukii.siegefishing.handler.nonitem.method.damageline;

import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;

import java.util.List;

public class DamageLineData {
    public double damage;
    public double length;
    public List<ParticleData> trailParticleDataList;
    public List<SoundData> trailSoundDataList;
    public List<ParticleData> hitParticleDataList;
    public List<SoundData> hitSoundDataList;
    public List<ParticleData> vanishParticleDataList;
    public List<SoundData> vanishSoundDataList;
}
