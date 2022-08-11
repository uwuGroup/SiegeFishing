package me.asakura_kukii.siegefishing.handler.method.fishing.ai;

import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import me.asakura_kukii.siegefishing.utility.cooldown.CooldownController;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class FishAI {
    private Vector currentDirection;
    private long nextAiTick = 0;
    private long currentPressure = 0;
    private long targetPressure = 0;
    private double speed;
    private double currentHp;
    private double strength;
    private double hp;

    public FishAI(double speed, double hp, double strength){
        this.speed = speed;
        this.currentHp = hp;
        this.strength = strength;
    }

    CooldownController cooldownController = new CooldownController();

    public void doAI(FishingSession fishingSession, long tick){
        UUID uniqueId = fishingSession.getPlayer().getUniqueId();
        if (!cooldownController.isCding(uniqueId, tick)){
            changeDirection();
            cooldownController.cd(uniqueId, (int) Math.floor(random.nextGaussian() * strength * 5 * 20));
        }
        Location nextLocation = movement(fishingSession);
        Vector movement = nextLocation.toVector().subtract(fishingSession.getHookTracker().getRodLocation().toVector());
        // 8 / tick, it's too fast to hold.
        if (movement.length() > 8){
            fishingSession.fail();
        }
        fishingSession.getHookTracker().updateTracerLocation(nextLocation);

        Player player = fishingSession.getPlayer();
        if (notInSameWorld(nextLocation, player.getEyeLocation()))
        if (nextLocation.distance(player.getEyeLocation()) > fishingSession.getFishThreadLength()){
            movePlayer(player, movement);
        }
    }

    private void changeDirection() {
        if (currentDirection == null){
            currentDirection = new Vector(1, 0, 0);
        }
        this.currentDirection.rotateAroundY((random.nextGaussian() - 0.5) * 360);
    }

    private void movePlayer(Player player, Vector movement) {
        Location location = player.getLocation();
        movement.setY(0);
        player.teleport(location.add(movement));
    }

    private boolean notInSameWorld(Location nextLocation, Location location) {
        return Objects.equals(nextLocation, location);
    }

    private Location movement(FishingSession fishingSession) {
        Location currentLocation = fishingSession.getHookTracker().getRodLocation();
        Location add = currentLocation.clone().add(currentDirection.clone().multiply(calcActualSpeed()).multiply(1 / 20).setY(0));
        // if under liquid, then float up.
        if (add.getBlock().getRelative(BlockFace.UP).isLiquid()){
            add.add(0, 0.1, 0);
        }
        return add;
    }

    private static final Random random = new Random();

    private double randomSpeed(){
        return random.nextGaussian() * speed;
    }

    private double calcActualSpeed(){
        return currentPressure * speed;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }
}
