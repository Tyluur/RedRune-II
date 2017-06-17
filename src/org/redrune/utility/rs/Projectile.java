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
	 * The type.
	 */
	@Getter
	@Setter
	private int type;
	
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
	 * The distance to start.
	 */
	@Getter
	@Setter
	private int distance;
	
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
	 * @param type
	 * 		The type of the projectile.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The projectile angle.
	 * @param distance
	 * 		The distance.
	 */
	private Projectile(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle, int distance) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.type = type;
		this.speed = speed;
		this.angle = angle;
		this.distance = distance;
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
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source entity.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation())));
		return new Projectile(source, victim, projectileId, 40, 36, 41, speed, 5, source.getSize() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId, int startHeight, int endHeight) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation())));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, 41, speed, 5, source.getSize() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation())));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, 5, source.getSize() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @param speed
	 * 		The projectile speed.
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int speed) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, 5, source.getSize() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The angle.
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, source.getSize() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The angle.
	 * @param distance
	 * 		The distance to start from.
	 * @return The created projectile.
	 */
	public static Projectile create(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle, int distance) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, distance);
	}
	
	/**
	 * Creates a new magic-speed based projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @param angle
	 * 		The angle.
	 * @return The created projectile.
	 */
	public static Projectile magic(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int angle) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation())));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, 11);
	}
	
	/**
	 * Creates a new range-speed based projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The victim.
	 * @param projectileId
	 * 		The projectile's gfx id;
	 * @param startHeight
	 * 		The starting height.
	 * @param endHeight
	 * 		The ending height.
	 * @param type
	 * 		The projectile type.
	 * @param angle
	 * 		The angle.
	 * @return The created projectile.
	 */
	public static Projectile ranged(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int type, int angle) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation())));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, 11);
	}
	
	/**
	 * Changes the projectile so it sends from the source Entity to the victim Entity given.
	 *
	 * @param source
	 * 		The source Entity.
	 * @param victim
	 * 		The victim Entity.
	 * @return The projectile instance.
	 */
	public Projectile transform(Entity source, Entity victim) {
		return transform(source, victim, source.isNPC(), 46, 5);
	}
	
	/**
	 * Changes the projectile so it sends from the source Entity to the victim Entity given.
	 *
	 * @param source
	 * 		The source Entity.
	 * @param victim
	 * 		The victim Entity.
	 * @param npc
	 * 		If the source should be handled as an NPC.
	 * @param baseSpeed
	 * 		The base speed.
	 * @param modifiedSpeed
	 * 		The modified speed.
	 * @return The projectile instance.
	 */
	public Projectile transform(Entity source, Entity victim, boolean npc, int baseSpeed, int modifiedSpeed) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.speed = (int) (baseSpeed + sourceLocation.distance(victim.getLocation()) * modifiedSpeed);
		if (npc) {
			this.distance = source.getSize() << 6;
		}
		return this;
	}
	
	/**
	 * Transforms the projectile so it is location based and it sends to the location.
	 *
	 * @param source
	 * 		The source Entity.
	 * @param l
	 * 		The location.
	 * @return The projectile instance.
	 */
	public Projectile transform(Entity source, Location l) {
		return transform(source, l, source.isNPC(), 46, 5);
	}
	
	/**
	 * Transforms a projectile to be location based, with updated parameters.
	 *
	 * @param source
	 * 		The Entity sending this projectile.
	 * @param l
	 * 		The end location.
	 * @param npc
	 * 		If the Entity should be handled as an npc.
	 * @param baseSpeed
	 * 		The base speed.
	 * @param modifiedSpeed
	 * 		The modified speed.
	 * @return The projectile instance.
	 */
	public Projectile transform(Entity source, Location l, boolean npc, int baseSpeed, int modifiedSpeed) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.endLocation = l;
		this.speed = (int) (baseSpeed + sourceLocation.distance(l) * modifiedSpeed);
		if (npc) {
			this.distance = source.getSize() << 6;
		}
		return this;
	}
	
	/**
	 * Gets a new {@code Projectile} {@code Object} based of this projectile object.
	 *
	 * @param source
	 * 		The source entity.
	 * @param victim
	 * 		The victim entity.
	 * @param speedMultiplier
	 * 		The speed multiplier.
	 * @return The created {@code Projectile} {@code Object}.
	 */
	public Projectile copy(Entity source, Entity victim, double speedMultiplier) {
		int distance = source.isNPC() ? source.getSize() << 6 : 11;
		int speed = (int) (this.speed + (source.getLocation().distance(victim.getLocation()) * speedMultiplier));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, distance);
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