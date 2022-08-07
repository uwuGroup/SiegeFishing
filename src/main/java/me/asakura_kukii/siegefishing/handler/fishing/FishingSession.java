package me.asakura_kukii.siegefishing.handler.fishing;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.fishing.render.FishingRenderer;
import me.asakura_kukii.siegefishing.handler.fishing.render.HookTracker;
import me.asakura_kukii.siegefishing.utility.random.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

public class FishingSession {
    private FishingRenderer renderer;
    private ArmorStand armorStand;
    private HookTracker hookTracker;
    private FishLoopTask fishLoopTask;
    private Player player;
    private ItemStack rodItem;
    private double pressure;
    private int expectedHookTick = Integer.MAX_VALUE;
    private FishingStatus status = FishingStatus.THROWN;
    private boolean valid = false;
    private double threadLength = 20d;

    private static final Map<UUID, FishingSession> sessionMap = new HashMap<>();

    public static FishingSession newSession(Player player, ItemStack rodItem){
        FishingSession session = sessionMap.get(player.getUniqueId());
        if (session != null){
            session.stop();
        }

        session = new FishingSession(player, rodItem);
        sessionMap.put(player.getUniqueId(), session);

        return session;
    }

    public static FishingSession get(Player player){
        return sessionMap.get(player.getUniqueId());
    }

    private FishingSession(Player player, ItemStack rodItem){
        this.player = player;
        this.rodItem = rodItem;
    }

    public void start(){
        if (valid){
            stop();
        }
        Location eyeLocation = player.getEyeLocation();
        this.armorStand = this.throwArmorStand();
        this.hookTracker = new SiegeHookTracer(armorStand);

        valid = true;
        renderer = new FishingRenderer(this);
        renderer.startRender();
        fishLoopTask = new FishLoopTask(armorStand);
        fishLoopTask.start();
    }

    public void onHitWater(SiegeFish fish, long expectedTick){
        if (!FishingStatus.THROWN.equals(status)){
            Bukkit.getLogger().log(Level.WARNING, ChatColor.YELLOW+ "incorrect status on hitWater(): " + status);
            return;
        }
        renderer.onHitWater();
        status = FishingStatus.WAIT;

        Location rodLocation = getHookTracker().getRodLocation();
        Location eyeLocation = player.getEyeLocation();
        if(notInSameWorld(rodLocation, eyeLocation)){
            stop();
            return;
        }
        threadLength = rodLocation.distance(eyeLocation);
        // set time to have a fish on hook
        this.expectedHookTick = expectedHookTick;
    }

    public void stop(){
        renderer.stopRender();
        if (armorStand != null) {
            armorStand.remove();
            armorStand = null;
        }
        if (hookTracker != null){
            hookTracker = null;
        }
        if (fishLoopTask != null){
            fishLoopTask.stop();
        }
        valid = false;
    }

    private boolean notInSameWorld(Location rodLocation, Location eyeLocation) {
        return !Objects.equals(rodLocation.getWorld(), eyeLocation.getWorld());
    }

    public void fishOnHook() {
        status = FishingStatus.HOOKED;
        renderer.onHooked();
    }

    private ArmorStand throwArmorStand(){
        ArmorStand aS = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
        aS.setSmall(true);
        aS.setInvulnerable(true);
        aS.setInvisible(true);
        aS.setGravity(true);
        Vector direction = player.getEyeLocation().getDirection().normalize();
        aS.setVelocity(direction);
        Objects.requireNonNull(aS.getEquipment()).setHelmet(new ItemStack(Material.COOKIE));
        return aS;
    }

    public ItemStack getFishItem(){
        if (!FishingStatus.HOOKED.equals(status)){
            return null;
        }
        if (!fishLoopTask.isFishDead()){
            return null;
        }
        return fishLoopTask.getFishItem();
    }

    public boolean isValid() {
        return valid;
    }

    public FishingRenderer getRenderer() {
        return renderer;
    }

    public ItemStack getRodItem() {
        return rodItem;
    }

    public HookTracker getHookTracker() {
        return hookTracker;
    }

    public Player getPlayer() {
        return player;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getExpectedHookTick() {
        return expectedHookTick;
    }

    public boolean isWaiting(){
        return FishingStatus.WAIT.equals(status);
    }

    public boolean isOnHook() {
        return FishingStatus.HOOKED.equals(status);
    }

    public boolean isFlying() {
        return FishingStatus.THROWN.equals(status);
    }

    public double getFishThreadLength() {
        return threadLength;
    }

    public void fail() {
        renderer.onFail();
        stop();
    }

    public void finish(){
        renderer.onFinish();
        stop();
    }

    class FishLoopTask extends BukkitRunnable {
        FishingSession fishingSession = FishingSession.this;

        private SiegeFish siegeFish;
        private long tick = 0;
        private Entity entity;

        public FishLoopTask(Entity entity){
            this.entity = entity;
        }
        @Override
        public void run() {
            if (!fishingSession.isValid()){
                stop();
                return;
            }
            doTick(tick);
            tick++;
        }

        private void doTick(long tick) {
            Block block = entity.getLocation().getBlock();

            boolean liquid = block.isLiquid();
            entity.setGravity(!liquid);

            if (liquid){
                entity.setVelocity(new Vector(0, 0, 0));
                Location entityLocation = entity.getLocation();
                double target = Math.ceil(entityLocation.getY());
                if (entityLocation.getY() < target - 0.05){
                    entity.setVelocity(entity.getVelocity().setY(0.1));
                }
                if (isFlying()){
                    this.onHitWater(tick);
                }
            }
            if (isFlying()){
                doFly();
                return;
            }
            if (isWaitingFish()){
                if (tick >= fishingSession.getExpectedHookTick()){
                    onFishOnHook();
                }
                return;
            }
            if (isHooked()){
                if (siegeFish != null){
                    siegeFish.doTick(fishingSession, tick);
                }
            }

        }

        private boolean isHooked() {
            return fishingSession.isOnHook();
        }

        private void onFishOnHook() {
            fishingSession.fishOnHook();
        }

        private boolean isWaitingFish() {
            return fishingSession.isWaiting();
        }

        private boolean isFlying() {
            return fishingSession.isFlying();
        }

        private void onHitWater(long tick) {
            this.siegeFish = determineFish();
            fishingSession.onHitWater(this.siegeFish, tick + siegeFish.getHookTime());
        }

        private SiegeFish determineFish() {
            // todo: determine a fish and start waiting.
            List<SiegeFish> fishList = getFishList();
            return RandomUtil.weightedRandom(fishList);
        }


        private List<SiegeFish> getFishList() {
            List<SiegeFish> fishList = new ArrayList<>();
            // TODO: 2022/8/7 finish this
            return fishList;
        }


        private void doFly() {

        }

        public void start() {
            this.runTaskTimer(SiegeFishing.pluginInstance, 0, 0);
        }

        public void stop(){
            this.cancel();
            this.siegeFish = null;
        }

        public boolean isFishDead() {
            return siegeFish.getHp() > 0;
        }

        public ItemStack getFishItem() {
            if (siegeFish == null){
                siegeFish = determineFish();
            }
            if (siegeFish == null){
                return null;
            }

            return siegeFish.getFishItem();
        }
    }
}
