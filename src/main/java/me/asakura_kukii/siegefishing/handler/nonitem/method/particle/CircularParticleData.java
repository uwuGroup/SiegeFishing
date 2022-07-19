package me.asakura_kukii.siegefishing.handler.nonitem.method.particle;

import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;

import java.util.List;

public class CircularParticleData {
    public double radius;
    public int sample;
    public List<ParticleData> particleDataList;
    public int circleAmount;

    public boolean orthogonal;
    public boolean circleAxis;
    public boolean circleX;
    public boolean circleZ;
    public boolean alignAxisToWorld;
    public boolean alignAxisToDirection;

}
