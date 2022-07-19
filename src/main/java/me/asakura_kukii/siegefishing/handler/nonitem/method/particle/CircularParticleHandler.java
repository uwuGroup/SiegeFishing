package me.asakura_kukii.siegefishing.handler.nonitem.method.particle;

import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodHandler;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.*;

public class CircularParticleHandler extends MethodHandler {

    public CircularParticleHandler() {};

    public void init(MethodNodeData mND, Location start, Vector direction, Entity e, HashMap<String, Object> extraData) {
        circularParticle(mND.next, start, direction, e, (CircularParticleData) mND.data.get(0), extraData);
    }


    public static void circularParticle(List<MethodNodeData> mNDL, Location start, Vector direction, Entity shooter, CircularParticleData cPD, HashMap<String, Object> extraData) {
        Vector axis, planeX, planeZ;

        HashMap<Vector, Vector> vM = new HashMap<>();

        if (cPD.orthogonal) {
            if (cPD.alignAxisToWorld) {
                axis = new Vector(0, 1, 0);
                planeX = new Vector(1, 0, 0);
                planeZ = new Vector(0, 0, 1);
            } else {
                Random random = new Random();
                if (cPD.alignAxisToDirection) {
                    axis = direction.clone().normalize();
                } else {
                    axis = new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
                }
                Vector rotateAxis =  new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
                rotateAxis = rotateAxis.clone().subtract(axis.clone().normalize().multiply(rotateAxis.clone().dot(axis.clone().normalize()))).normalize();
                planeX = axis.clone().rotateAroundNonUnitAxis(rotateAxis, Math.PI / 2).normalize();
                planeZ = axis.clone().getCrossProduct(planeX.clone()).normalize();
            }
            if (cPD.circleAxis) vM.put(axis, planeX);
            if (cPD.circleX) vM.put(planeX, axis);
            if (cPD.circleZ) vM.put(planeZ, axis);
        } else {
            for (int i = 0; i < cPD.circleAmount; i++) {
                Random random = new Random();
                axis = new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
                Vector rotateAxis =  new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
                rotateAxis = rotateAxis.clone().subtract(axis.clone().normalize().multiply(rotateAxis.clone().dot(axis.clone().normalize()))).normalize();
                planeX = axis.clone().rotateAroundNonUnitAxis(rotateAxis, Math.PI / 2).normalize();
                vM.put(axis, planeX);
            }
        }


        for (int i = 0; i < cPD.sample; i++) {
            double rotA = Math.sin(Math.PI * 2 * i / cPD.sample);
            double rotB = Math.cos(Math.PI * 2 * i / cPD.sample);

            for (Vector v : vM.keySet()) {
                circularParticleFromVector(start.clone(), v, vM.get(v), cPD, rotA, rotB, shooter);
            }
        }
    }

    public static void circularParticleFromVector(Location l, Vector axis, Vector referenceAxis, CircularParticleData cPD, double xFactor, double zFactor, Entity e) {
        Vector planeZ;
        planeZ = axis.clone().getCrossProduct(referenceAxis.clone()).normalize();
        e.sendMessage(axis.toString() + "  ");
        Location temp = l.clone().add(referenceAxis.clone().multiply(xFactor).multiply(cPD.radius)).add(planeZ.clone().multiply(zFactor).multiply(cPD.radius));
        for (ParticleData particleData : cPD.particleDataList) {
            ParticleHandler.spawnParticle(temp.clone(), particleData);
        }
    }
}