package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

import me.asakura_kukii.siegefishing.config.data.FileType;
import me.asakura_kukii.siegefishing.config.data.addon.ExtraStringListData;
import me.asakura_kukii.siegefishing.handler.method.fishingbeta.FishingTaskData;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import me.asakura_kukii.siegefishing.utility.format.FormatHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.FluidCollisionMode;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

public class HookedHandler extends StageHandler{

    @Override
    public void update(FishingTaskData fTD) {
        //count timer
        fTD.timer--;

        //update free velocity
        updateRandomVel(fTD);

        //run away velocity
        Vector runAwayDirection = getRunAwayDirection(fTD);

        double distancePercentage = fTD.distance / fTD.targetDistance;
        double distancePercentageRelativeToStdDistance = distancePercentage - 1;

        //player motion
        Vector playerVel = fTD.pD.p.getVelocity().add(runAwayDirection.clone().multiply(distancePercentage).multiply(fTD.playerMaxVelocity));
        fTD.pD.p.setVelocity(playerVel.clone());

        //approach std motion
        fTD.hookVel = fTD.hookVel.add(runAwayDirection.multiply(-distancePercentageRelativeToStdDistance).multiply(fTD.runAwayVelocity));

        double pressureFromControlledDistance = 8 * (fTD.distance / fTD.stringDistance - fTD.distance / fTD.targetDistance) + fTD.distance / fTD.targetDistance;
        double pressureValue = fTD.fD.difficulty * pressureFromControlledDistance;
        fTD.pressure = pressureValue;


        //tiredness decreasing velocity, which is (pressure - 1) * difficulty
        double vitalityDamage = pressureValue - fTD.fD.difficulty;
        if (vitalityDamage < 0) vitalityDamage = 0;
        fTD.vitality = fTD.vitality - vitalityDamage;

        //this part belongs to render
        hotBarMsg(fTD);

        //collision check
        if (fTD.hookVel.length() == 0) {
            fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);
            return;
        }
        RayTraceResult rTR = Objects.requireNonNull(fTD.hookLoc.clone().getWorld()).rayTraceBlocks(fTD.hookLoc.clone().add(new Vector(0, -1, 0)), fTD.hookVel.clone(), fTD.hookVel.clone().length(), FluidCollisionMode.NEVER, true);
        if (rTR != null) {
            Vector bounceDirection = Objects.requireNonNull(rTR.getHitBlockFace()).getDirection().normalize();
            Vector bouncePosition = rTR.getHitPosition();
            Vector d = bounceDirection.clone().multiply(fTD.hookVel.clone().dot(bounceDirection.clone()));
            Vector e = fTD.hookVel.clone().subtract(d.clone()).multiply(1);
            Vector f = d.clone().multiply(-1).multiply(0);
            Vector bounceVelocity = e.clone().add(f.clone());
            fTD.hookLoc = bouncePosition.toLocation(Objects.requireNonNull(fTD.hookLoc.getWorld())).add(new Vector(0, 1, 0));
            fTD.hookVel = bounceVelocity;
        } else {
            fTD.hookLoc = fTD.hookLoc.add(fTD.hookVel);
        }
    }

    private void updateRandomVel(FishingTaskData fTD) {
        if (fTD.timer <= 0) {
            fTD.timer = (int) Math.ceil(fTD.r.nextDouble() * 20);
            Vector randomDirection = new Vector((fTD.r.nextDouble() - 0.5) * 2, 0, (fTD.r.nextDouble() - 0.5) * 2);
            if (randomDirection.getX() == 0 && randomDirection.getZ() == 0) {
                randomDirection = new Vector(1, 0, 0);
            } else {
                randomDirection = randomDirection.normalize();
            }
            double velocity = fTD.r.nextDouble() * fTD.vitality / (fTD.refVitality * fTD.fD.difficulty) * fTD.fD.difficulty * fTD.randomVelocity;
            fTD.randomVel = randomDirection.multiply(velocity);
        }
        fTD.hookVel = fTD.randomVel;
    }

    private Vector getRunAwayDirection(FishingTaskData fTD) {
        Vector runAwayDirection;
        double deltaX = fTD.hookLoc.getX() - fTD.pD.p.getLocation().getX();
        double deltaZ = fTD.hookLoc.getZ() - fTD.pD.p.getLocation().getZ();
        if (deltaX == 0 && deltaZ == 0) {
            runAwayDirection = new Vector(1, 0, 0).normalize();
        } else {
            runAwayDirection = new Vector(deltaX, 0, deltaZ).normalize();
        }
        return runAwayDirection;
    }

    private void hotBarMsg(FishingTaskData fTD) {

        double distanceGetToMaxPercentage = 1 - (fTD.distance - fTD.getDistance) / (fTD.stdDistance - fTD.getDistance);
        if (distanceGetToMaxPercentage > 1) distanceGetToMaxPercentage = 1;
        if (distanceGetToMaxPercentage < 0) distanceGetToMaxPercentage = 0;
        int distanceStringIndex = (int) Math.floor(distanceGetToMaxPercentage * 16);
        if (distanceStringIndex > 15) distanceStringIndex = 15;
        if (distanceStringIndex < 0) distanceStringIndex = 0;

        ExtraStringListData dSL = (ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get("distance_status");
        String dS = percentToChatColor(fTD, distanceGetToMaxPercentage, 16) + FormatHandler.format(dSL.extraStringList.get(distanceStringIndex), false);

        StringBuilder pressureBarString = new StringBuilder();
        for (double i = 0; i < fTD.rD.maxPressure; i = i + 0.1) {
            if (i < fTD.pressure) {
                pressureBarString.append(FormatHandler.format(((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get("pressure_status")).extraStringList.get(0), false));
            } else {
                pressureBarString.append(FormatHandler.format(((ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get("pressure_status")).extraStringList.get(1), false));
            }
        }

        int vitalityIndex = 0;
        if (fTD.vitality <= fTD.refVitality * fTD.fD.difficulty * 1) vitalityIndex = 0;
        if (fTD.vitality <= fTD.refVitality * fTD.fD.difficulty * 0.6) vitalityIndex = 1;
        if (fTD.vitality <= fTD.refVitality * fTD.fD.difficulty * 0.3) vitalityIndex = 2;
        if (fTD.vitality <= 0) vitalityIndex = 3;

        ExtraStringListData vSL = (ExtraStringListData) FileType.EXTRA_STRING_LIST.map.get("vitality_status");

        String vS = FormatHandler.format(vSL.extraStringList.get(vitalityIndex), false);
        fTD.pD.hotBarMsg = dS + " " + pressureBarString + " " + vS;
        fTD.pD.p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(fTD.pD.hotBarMsg));
    }

    private ChatColor percentToChatColor(FishingTaskData fTD, double d, int stateCount) {
        int index = (int) Math.floor(d * 16);
        if (index > 15) index = 15;
        if (index < 0) index = 0;

        int r1 = 255;
        int g1 = 214;
        int b1 = 201;
        int r2 = 231;
        int g2 = 248;
        int b2 = 255;
        int rc = (int) Math.ceil(d * (r2 - r1) + r1);
        int gc = (int) Math.ceil(d * (g2 - g1) + g1);
        int bc = (int) Math.ceil(d * (b2 - b1) + b1);

        if (index == 15) {
            rc = 142;
            gc = 255;
            bc = 142;
        }

        return ColorHandler.gen(rc, gc, bc);
    }
}
