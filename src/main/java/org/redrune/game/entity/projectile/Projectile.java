package org.redrune.game.entity.projectile;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a projectile to send.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class Projectile {
	
	/**
	 * The source node.
	 */
	@Getter
	@Setter
	private Actor source;
	
	/**
	 * The source's centered location.
	 */
	@Getter
	@Setter
	private WorldTile sourceTile;
	
	/**
	 * The victim.
	 */
	@Getter
	@Setter
	private Actor victim;
	
	/**
	 * The projectile's gfx id.
	 */
	@Getter
	@Setter
	private int projectileId;
	
	/**
	 * The start height.
	 */
	@Getter
	@Setter
	private int startHeight;
	
	/**
	 * The ending height.
	 */
	@Getter
	@Setter
	private int endHeight;
	
	/**
	 * The delay.
	 */
	@Getter
	@Setter
	private int delay;
	
	/**
	 * The speed.
	 */
	@Getter
	@Setter
	private int speed;
	
	/**
	 * The angle.
	 */
	@Getter
	@Setter
	private int angle;
	
	/**
	 * The size of the creator
	 */
	@Getter
	@Setter
	private int creatorSize = 1;
	
	/**
	 * The distance to start.
	 */
	@Getter
	@Setter
	private int startDistanceOffset;
	
	/**
	 * The end location (used for location based projectiles).
	 */
	@Getter
	@Setter
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
}