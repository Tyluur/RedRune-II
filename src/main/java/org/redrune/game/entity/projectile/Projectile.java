package org.redrune.game.entity.projectile;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.global.WorldTile;

/**
 * Represents a projectile to send.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/16/2017
 */
public class Projectile {
	
	/**
	 * The source node.
	 */
	private Actor source;
	
	/**
	 * The source's centered location.
	 */
	private WorldTile sourceTile;
	
	/**
	 * The victim.
	 */
	private Actor victim;
	
	/**
	 * The projectile's gfx id.
	 */
	private int projectileId;
	
	/**
	 * The start height.
	 */
	private int startHeight;
	
	/**
	 * The ending height.
	 */
	private int endHeight;
	
	/**
	 * The delay.
	 */
	private int delay;
	
	/**
	 * The speed.
	 */
	private int speed;
	
	/**
	 * The angle.
	 */
	private int angle;
	
	/**
	 * The size of the creator
	 */
	private int creatorSize = 1;
	
	/**
	 * The distance to start.
	 */
	private int startDistanceOffset;
	
	/**
	 * The end location (used for location based projectiles).
	 */
	private WorldTile endLocation;
	
	/**
	 * Constructs a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The entity victim.
	 * @param projectileId
	 * 		The projectile gfx id.
	 * @param startHeight
	 * 		The start height.
	 * @param endHeight
	 * 		The end height.
	 * @param delay
	 * 		The type of the projectile.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The projectile angle.
	 * @param startDistanceOffset
	 * 		The distance offset.
	 */
	public Projectile(Actor source, Actor victim, int projectileId, int startHeight, int endHeight, int delay, int speed, int angle, int startDistanceOffset) {
		this.source = source;
		this.sourceTile = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.speed = speed;
		this.angle = angle;
		this.setCreatorSize(source.getSize());
		this.startDistanceOffset = startDistanceOffset;
		//		System.out.println("projectileId = [" + projectileId + "], startHeight = [" + startHeight + "], endHeight = [" + endHeight + "], delay = [" + delay + "], speed = [" + speed + "], angle = [" + angle + "], startDistanceOffset = [" + startDistanceOffset + "], sourceTile=[" + sourceTile + "]");
	}
	
	/**
	 * Gets the source location on construction.
	 *
	 * @param entity
	 * 		The node.
	 * @return The centered location.
	 */
	public static WorldTile getLocation(Actor entity) {
		if (entity == null) {
			return null;
		}
		if (entity.isNPC()) {
			int size = entity.getSize() >> 1;
			return entity.toNPC().getWorldTile().transform(size, size, 0);
		}
		return entity.getWorldTile();
	}
	
	@Override
	public String toString() {
		return "Projectile{" + "source=" + source + ", sourceTile=" + sourceTile + ", victim=" + victim + ", projectileId=" + projectileId + ", startHeight=" + startHeight + ", endHeight=" + endHeight + ", delay=" + delay + ", speed=" + speed + ", angle=" + angle + ", creatorSize=" + creatorSize + ", startDistanceOffset=" + startDistanceOffset + ", endLocation=" + endLocation + '}';
	}
	
	/**
	 * Checks if the projectile is location based.
	 *
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isLocationBased() {
		return endLocation != null;
	}

    public Actor getSource() {
        return this.source;
    }

    public WorldTile getSourceTile() {
        return this.sourceTile;
    }

    public Actor getVictim() {
        return this.victim;
    }

    public int getProjectileId() {
        return this.projectileId;
    }

    public int getStartHeight() {
        return this.startHeight;
    }

    public int getEndHeight() {
        return this.endHeight;
    }

    public int getDelay() {
        return this.delay;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getAngle() {
        return this.angle;
    }

    public int getCreatorSize() {
        return this.creatorSize;
    }

    public int getStartDistanceOffset() {
        return this.startDistanceOffset;
    }

    public WorldTile getEndLocation() {
        return this.endLocation;
    }

    public void setSource(Actor source) {
        this.source = source;
    }

    public void setSourceTile(WorldTile sourceTile) {
        this.sourceTile = sourceTile;
    }

    public void setVictim(Actor victim) {
        this.victim = victim;
    }

    public void setProjectileId(int projectileId) {
        this.projectileId = projectileId;
    }

    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }

    public void setEndHeight(int endHeight) {
        this.endHeight = endHeight;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setCreatorSize(int creatorSize) {
        this.creatorSize = creatorSize;
    }

    public void setStartDistanceOffset(int startDistanceOffset) {
        this.startDistanceOffset = startDistanceOffset;
    }

    public void setEndLocation(WorldTile endLocation) {
        this.endLocation = endLocation;
    }
}