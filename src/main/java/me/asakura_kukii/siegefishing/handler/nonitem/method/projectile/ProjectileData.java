package me.asakura_kukii.siegefishing.handler.nonitem.method.projectile;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;


public class ProjectileData {
    public double vel = 3;
    public double maxVel = 5;
    public Vector3D acc = new Vector3D(0.0, 0.0, 0.0);
    public int maxBounce = 3;
    public double bounceFactor = 0.6;
    public double bounceFriction = 0.2;
    public boolean sticky = false;
    public boolean attachable = false;
    public int maxLife = 500;
    public int interval = 20;
    public int intervalStart = 20;
    public int intervalStop = 20;
    public List<ParticleData> trailParticleDataList;
    public List<SoundData> trailSoundDataList;
    public List<ParticleData> bounceParticleDataList;
    public List<SoundData> bounceSoundDataList;
    public List<ParticleData> intervalParticleDataList;
    public List<SoundData> intervalSoundDataList;
    public List<ParticleData> vanishParticleDataList;
    public List<SoundData> vanishSoundDataList;


    public Entity shooter;
    public Location currentPos;
    public Vector currentVel = new Vector(0.0, 0.0, 0.0);
    public int bounce = 0;
    public int life = 0;
    public boolean attach = false;
    public Entity attachTarget;
    public Vector attachVector;
    public List<MethodNodeData> next;
    public HashMap<String, Object> extraData;


    public ProjectileData cloneData() {
        ProjectileData pD = new ProjectileData();

        pD.vel = this.vel;
        pD.maxVel = this.maxVel;
        pD.acc = this.acc;
        pD.maxBounce = this.maxBounce;
        pD.bounceFactor = this.bounceFactor;
        pD.bounceFriction = this.bounceFriction;
        pD.sticky = this.sticky;
        pD.attachable = this.attachable;
        pD.maxLife = this.maxLife;
        pD.interval = this.interval;
        pD.intervalStart = this.intervalStart;
        pD.intervalStop = this.intervalStop;
        pD.trailParticleDataList = this.trailParticleDataList;
        pD.trailSoundDataList = this.trailSoundDataList;
        pD.bounceParticleDataList = this.bounceParticleDataList;
        pD.bounceSoundDataList = this.bounceSoundDataList;
        pD.intervalParticleDataList = this.intervalParticleDataList;
        pD.intervalSoundDataList = this.intervalSoundDataList;
        pD.vanishParticleDataList = this.vanishParticleDataList;
        pD.vanishSoundDataList = this.vanishSoundDataList;

        return pD;
    }
}
