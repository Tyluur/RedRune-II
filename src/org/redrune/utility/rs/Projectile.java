package org.redrune.utility.rs;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;

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
	private Entity source;
	
	/**
	 * The source's centered location.
	 */
	@Getter
	@Setter
	private Location sourceLocation;
	
	/**
	 * The victim.
	 */
	@Getter
	@Setter
	private Entity victim;
	
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
	private Location endLocation;
	
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
	public Projectile(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int delay, int speed, int angle, int startDistanceOffset) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.speed = speed;
		this.angle = angle;
		this.setCreatorSize(source.getSize());
		this.startDistanceOffset = startDistanceOffset;
//		System.out.println("projectileId = [" + projectileId + "], startHeight = [" + startHeight + "], endHeight = [" + endHeight + "], delay = [" + delay + "], speed = [" + speed + "], angle = [" + angle + "], startDistanceOffset = [" + startDistanceOffset + "]");
	}
	
	/**
	 * Gets the source location on construction.
	 *
	 * @param n
	 * 		The node.
	 * @return The centered location.
	 */
	public static Location getLocation(Entity n) {
		if (n == null) {
			return null;
		}
		if (n.isNPC()) {
			int size = n.getSize() >> 1;
			return n.toNPC().getLocation().transform(size, size, 0);
		}
		return n.getLocation();
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