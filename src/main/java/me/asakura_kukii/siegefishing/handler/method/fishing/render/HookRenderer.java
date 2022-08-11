package me.asakura_kukii.siegefishing.handler.method.fishing.render;

import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import me.asakura_kukii.siegefishing.utility.cooldown.CooldownController;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class HookRenderer extends BasicRenderer{
    private boolean firstHit = false;

    public HookRenderer(FishingSession fishingSession) {
        super(fishingSession);
    }

    private boolean isOnWater(){
        HookTracker hookTracker = getFishingSession().getHookTracker();
        Block block = hookTracker.getRodLocation().getBlock();
        return block.isLiquid();
    }

    @Override
    public void onHitWater(){
        firstHit = false;
    }

    private Location lastLocation;
    private double spawnedParticleLength = 0;
    private double traveled = 0;
    private double particleDensity = 4;

    @Override
    protected void renderTick(long tick) {
        if (lastLocation == null){
            lastLocation = getFishingSession().getHookTracker().getRodLocation();
            return;
        }
        if (!firstHit){
            renderBulletRing(tick);
            renderFlying(tick);
            return;
        }

        if (getFishingSession().isWaiting()){
            renderAmbient(tick);
        }

        if (getFishingSession().isOnHook()){

        }
    }

    private final CooldownController ambientCd = new CooldownController();
    private static final Random random = new Random();
    private void renderAmbient(long tick) {
        UUID uniqueId = getFishingSession().getPlayer().getUniqueId();
        if (ambientCd.isCding(uniqueId, tick)){
            return;
        }
        Location rodLocation = getFishingSession().getHookTracker().getRodLocation();
        World world = rodLocation.getWorld();
        if (world == null){
            return;
        }
        for (int i = 0; i < 10; i++) {
            Location add = rodLocation.clone().add(new Vector(random.nextDouble() * 10, 0, random.nextDouble() * 10));
            world.spawnParticle(Particle.WATER_BUBBLE, add, 5, 1, 1, 1);
        }

        ambientCd.cd(uniqueId, random.nextInt() * 20L);
    }

    private void renderFlying(long tick) {
        Location currentLocation = getFishingSession().getHookTracker().getRodLocation().clone();
        Vector dLocation = currentLocation.clone().toVector().subtract(lastLocation.toVector());
        if (dLocation.length() <= 0){
            return;
        }
        // update traveled length
        traveled += dLocation.length();

        // spawn particles between lastLocation & currentLocation
        Vector direction = dLocation.clone().normalize();
        Location spawnLocation = lastLocation.clone();
        double lengthPerSpawn = 1 / particleDensity;
        while (spawnedParticleLength < traveled){
            spawnLocation.add(direction.clone().multiply(lengthPerSpawn));
            spawnedParticleLength += lengthPerSpawn;
        }
    }

    private final CooldownController bulletRingCd = new CooldownController();
    private void renderBulletRing(long tick) {
        if (bulletRingCd.isCding(getFishingSession().getPlayer().getUniqueId(), tick)){
            return;
        }
        Location rodLocation = getFishingSession().getHookTracker().getRodLocation();
        World world = rodLocation.getWorld();
        if (world == null){
            return;
        }
        world.spawnParticle(Particle.FIREWORKS_SPARK, rodLocation, 10, 0, 0, 0, 1d);
    }
}
