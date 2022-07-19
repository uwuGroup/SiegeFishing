package me.asakura_kukii.siegefishing.handler.nonitem.method.common;

import me.asakura_kukii.siegefishing.handler.nonitem.method.damageline.DamageLineHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.method.particle.CircularParticleHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.method.projectile.ProjectileHandler;
import me.asakura_kukii.siegefishing.loader.method.CircularParticleIO;
import me.asakura_kukii.siegefishing.loader.method.DamageLineIO;
import me.asakura_kukii.siegefishing.loader.method.ProjectileIO;
import me.asakura_kukii.siegefishing.loader.method.common.MethodIO;

import java.util.Arrays;
import java.util.List;

public enum MethodType {

    DAMAGELINE(new DamageLineIO(), new DamageLineHandler(), new String[]{"hitEntity","hitBlock","vanish"}),
    PROJECTILE(new ProjectileIO(), new ProjectileHandler(), new String[]{"bounce","interval","vanish"}),
    CIRCULAR_PARTICLE(new CircularParticleIO(), new CircularParticleHandler(), new String[]{""});

    public List<String> triggerList;
    public MethodIO mIO;
    public MethodHandler mH;
    //public String[] common = {"time"};
    MethodType(MethodIO mIO, MethodHandler mH, String[] triggerArray) {
        this.triggerList = Arrays.asList(triggerArray);
        //this.triggerList.addAll(Arrays.asList(common));
        this.mIO = mIO;
        this.mH = mH;
    };
}
