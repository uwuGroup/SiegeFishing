package me.asakura_kukii.siegefishing.utility.boundingbox;

import me.asakura_kukii.siegefishing.utility.coodinate.PositionHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class HumanoidBoundingBox implements BoundingBox{
    public Entity e;
    public Location feetCenter;
    public Location neckCenter;
    public Location headCenter;
    public double bodyHeight = 1.45;
    //sneak 1.00 !sneak 1.45
    public double bodyRadius = 0.40;
    public double headHeight = 0.45;
    public double headRadius = 0.30;
    public double headShotRadius = 0.20;

    public HumanoidBoundingBox(Entity e) {
        this.e = e;
        this.feetCenter = e.getLocation();
        if (e instanceof Player ) {
            if (!((Player) e).isSneaking()) {
                this.bodyHeight = 1.45;
            } else {
                this.bodyHeight = 1.00;
            }
        } else {
            this.bodyHeight = 1.45;
        }

        this.neckCenter = e.getLocation().add(new Vector(0, bodyHeight, 0));
        this.headCenter = this.neckCenter.clone().add(PositionHandler.upVectorToLivingEntitySight((LivingEntity) e).clone().normalize().multiply(headHeight));
    }

    public HumanoidBoundingBox(Entity e, double bodyHeight, double bodyRadius, double headHeight, double headRadius, double headShotRadius) {
        this.e = e;
        this.feetCenter = e.getLocation();
        this.bodyHeight = bodyHeight;
        this.bodyRadius = bodyRadius;
        this.headHeight = headHeight;
        this.headRadius = headRadius;
        this.headShotRadius = headShotRadius;
        this.neckCenter = e.getLocation().add(new Vector(0, bodyHeight, 0));
        this.headCenter = this.neckCenter.clone().add(PositionHandler.upVectorToLivingEntitySight((LivingEntity) e).clone().normalize().multiply(headHeight));
    }

    public Location intersects(Location start, Vector direction) {
        Location hitLocation = intersectsHead(start, direction);
        if (hitLocation != null) {
            return hitLocation;
        }
        hitLocation = intersectsBody(start, direction);
        if (hitLocation != null) {
            return hitLocation;
        }
        return null;
    }

    public Location intersectsHead(Location start, Vector direction) {
        return BoundingBoxHandler.twoPointCylinderCheck(start, direction, this.headCenter, this.neckCenter, this.headHeight, this.headRadius);
    }

    public Location intersectsBody(Location start, Vector direction) {
        return BoundingBoxHandler.twoPointCylinderCheck(start, direction, this.neckCenter, this.feetCenter, this.bodyHeight, this.bodyRadius);
    }

    public boolean headShotCheck(Location start, Vector direction) {
        return BoundingBoxHandler.twoPointCylinderCheck(start, direction, this.headCenter, this.neckCenter, this.headShotRadius);
    }
}
