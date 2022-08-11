package me.asakura_kukii.siegefishing.handler.method.fishing.render;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.method.fishing.FishingSession;
import me.asakura_kukii.siegefishing.utility.VectorUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.logging.Level;

public class ThreadRenderer extends BasicRenderer {

    public ThreadRenderer(FishingSession fishingSession){
        super(fishingSession);
    }
    @Override
    protected void renderTick(final long tick) {
        FishingSession fishingSession = getFishingSession();
        HookTracker hookTracker = fishingSession.getHookTracker();
        if (hookTracker == null){
            return;
        }
        RodRenderParam renderParam = createRenderParam(fishingSession);
        if (tick - getLastSpawn(fishingSession.getPlayer().getUniqueId()) < renderParam.getInteval()){
            return;
        }
        new ThreadRenderTask(hookTracker, renderParam, fishingSession.getPlayer()).start();
    }

    private RodRenderParam createRenderParam(FishingSession fishingSession) {
        double pressure = fishingSession.getPressure();

        double speed = speedFunction(pressure);
        double interval = intervalFunction(pressure);
        int color = colorFunction(pressure);
        double size = sizeFunction(pressure);
        return new RodRenderParam(speed, interval, color, size, pressure);
    }

    private double sizeFunction(double pressure) {
        return 1d;
    }

    private int colorFunction(double pressure) {
        return 0x66CCFF;
    }

    private double intervalFunction(double pressure) {
        return 10d;
    }

    private double speedFunction(double pressure) {
        return 20d;
    }

    class ThreadRenderTask extends BukkitRunnable {
        private HookTracker hookTracker;
        private Player player;

        private double traveledLength = 0;
        private double particleDensity = 4;
        private double maxPressure = 1;
        private RodRenderParam rodRenderParam;

        private long lived = 0;
        private long ttl = 1000;

        public ThreadRenderTask(HookTracker hookTracker, RodRenderParam renderParam, Player player) {
            this.hookTracker = hookTracker;
            this.player = player;
            this.rodRenderParam = renderParam;
        }

        private void render() {
            Location hookLocation = hookTracker.getRodLocation().clone();
            if (!Objects.equals(hookLocation.getWorld(), player.getLocation().getWorld())) {
                this.stop();
                return;
            }
            Location playerStickLocation = getPlayerStickLocation();

            double speed = rodRenderParam.getSpeed();
            double travelInThisTick = speed / 20;

            Vector towards = playerStickLocation.clone().subtract(hookLocation).toVector();
            double totalLength = towards.length();
            if (totalLength <= 0) {
                this.stop();
                return;
            }
            double stopLength = traveledLength + travelInThisTick;
            if (stopLength > totalLength) {
                stop();
            }
            double h = towards.getY();

            // spawn step vector
            // currentLocation.add(towards) -> nextLocation
            towards.normalize().multiply(1 / particleDensity);

            Location currentLocation = hookLocation.clone().add(towards.clone().normalize().multiply(traveledLength));

            stopLength = Math.min(stopLength, totalLength);
            while (traveledLength <= stopLength && lived++ < ttl) {
                double locationPercentile = traveledLength / totalLength;
                Location spawnLoc = makeCurve(currentLocation.clone(), locationPercentile, h);
                spawnParticle(spawnLoc, rodRenderParam);

                currentLocation.add(towards);
                traveledLength += towards.length();
            }
        }

        private void spawnParticle(Location spawnLoc, RodRenderParam rodRenderParam) {
            World world = spawnLoc.getWorld();
            if (world == null) {
                return;
            }

            world.spawnParticle(Particle.REDSTONE, spawnLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(rodRenderParam.getColor()), (float) rodRenderParam.getSize()));
        }

        private Location makeCurve(Location clone, double locationPercentile, double h) {
            double pressurePercentile = getTightenedFactor(clone.distance(player.getEyeLocation()), getFishingSession().getFishThreadLength());
            // use y = x^2 to make a curve.
            double curveFactor = curve(locationPercentile);
            clone.add(0, (1 - (pressurePercentile)) * (curveFactor - locationPercentile) * h, 0);
            return clone;
        }

        private double curve(double locationPercentile) {
            return Math.pow(locationPercentile, 5);
        }

        /**
         * return a percentile of pressure, ranging [0, 1].
         * if pressure is 0, it's loose
         * if pressure is 1, it's tightened
         *
         * @param distance distance
         * @param fishThreadLength
         * @return the getTightenedFactor
         */
        private double getTightenedFactor(double distance, double fishThreadLength) {
            return Math.max(0, Math.min(1, distance / fishThreadLength));
        }


        public void start() {
            this.runTaskTimer(SiegeFishing.pluginInstance, 0, 0);
        }

        public void stop() {
            this.cancel();
        }

        private Location getPlayerStickLocation() {
            Location playerFootLocation = player.getLocation();
            Location playerEyeLocation = player.getEyeLocation();

            // get center location of player
            Location centerLocation = playerFootLocation.clone().add(playerEyeLocation).multiply(0.5);

            // use normalize() to prevent Zero
            Vector xzDirection = VectorUtil.normalize(playerFootLocation.toVector().subtract(playerEyeLocation.getDirection()));


            Vector forwardLocation = getForwardDirection(playerFootLocation, xzDirection);
            centerLocation.add(forwardLocation);

            Vector leftDirection = VectorUtil.normalize(xzDirection.crossProduct(centerLocation.toVector())).multiply(0.5);
            Vector upDirection = VectorUtil.normalize(xzDirection.crossProduct(leftDirection)).multiply(0.1);

            if (rodOnRightHand(player)) {
                centerLocation.subtract(leftDirection);
            } else if (rodOnLeftHand(player)) {
                centerLocation.add(leftDirection);
            }
            centerLocation.add(upDirection);
            return centerLocation;
        }

        private boolean rodOnLeftHand(Player player) {
            MainHand mainHand = player.getMainHand();
            if (mainHand.equals(MainHand.LEFT)) {
                return isRodItem(player.getInventory().getItemInMainHand());
            } else {
                return isRodItem(player.getInventory().getItemInOffHand());
            }
        }

        private boolean rodOnRightHand(Player player) {
            MainHand mainHand = player.getMainHand();
            if (mainHand.equals(MainHand.RIGHT)) {
                return isRodItem(player.getInventory().getItemInMainHand());
            } else {
                return isRodItem(player.getInventory().getItemInOffHand());
            }
        }

        // return xz direction vector of a player
        private Vector getForwardDirection(Location playerEyeLocation, Vector xzDirection) {
            float yaw = playerEyeLocation.getYaw();
            World world = playerEyeLocation.getWorld();
            if (world == null) {
                throw new RuntimeException(String.format("player %s world is null", player.getName()));
            }
            Location forwardLocation = VectorUtil.normalize(xzDirection.clone().setY(0)).toLocation(world);
            forwardLocation.setYaw(yaw);
            return forwardLocation.getDirection();
        }


        public HookTracker getHookTracker() {
            return hookTracker;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public void run() {
            try {
                render();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "error rendering fishing string.", e);
                cancel();
            }
        }
    }
}

