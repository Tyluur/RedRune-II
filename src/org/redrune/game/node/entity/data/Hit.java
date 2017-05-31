package org.redrune.game.node.entity.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;

/**
 * Represents a damage to hit.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class Hit {
	
	/**
	 * The entity dealing the damage.
	 */
	@Getter
	private final Entity source;
	
	/**
	 * The damage hitsplat.
	 */
	@Getter
	@Setter
	private HitSplat splat;
	
	/**
	 * The amount of damage to be dealt
	 */
	@Getter
	@Setter
	private int damage;
	
	/**
	 * If the hit is a critical hit
	 */
	@Getter
	@Setter
	private boolean critical;
	
	/**
	 * The amount of soaked damage.
	 */
	@Getter
	@Setter
	private int soaked;
	
	/**
	 * The delay on the hit
	 */
	@Getter
	@Setter
	private int delay;
	
	public Hit(Entity source, int damage) {
		this(source, damage, HitSplat.REGULAR_DAMAGE);
	}
	
	public Hit(Entity source, int damage, HitSplat splat) {
		this.source = source;
		this.damage = damage;
		this.splat = splat;
		this.critical = false;
		this.soaked = 0;
	}
	
	/**
	 * Sets the critical look
	 */
	public void setCriticalMark() {
		critical = true;
	}
	
	/**
	 * Sets the hit to be a heal
	 */
	public void setHealHit() {
		setSplat(HitSplat.HEALED_DAMAGE);
		critical = false;
	}
	
	/**
	 * If the hit was missed
	 */
	public boolean missed() {
		return damage == 0;
	}
	
	/**
	 * Gets the mark of the hitsplat
	 *
	 * @param player
	 * 		The player
	 * @param victim
	 * 		The victim
	 */
	public int getMark(Player player, Entity victim) {
		if (HitSplat.HEALED_DAMAGE == splat) {
			return splat.getMark();
		}
		if (damage == 0) {
			return HitSplat.MISSED.getMark();
		}
		int mark = splat.getMark();
		if (critical) {
			mark += 10;
		}
		if (!interactingWith(player, victim)) {
			mark += 14;
		}
		return mark;
	}
	
	/**
	 * Checks if the player is interacting
	 *
	 * @param player
	 * 		The player
	 * @param victim
	 * 		The victim
	 */
	// TODO: implementation
	public boolean interactingWith(Player player, Entity victim) {
		return false;
		//return Objects.equals(player, victim) || Objects.equals(player, source);
	}
	
	/**
	 * Represents the damage types.
	 *
	 * @author Emperor
	 */
	public enum HitSplat {
		
		MISSED(8),
		REGULAR_DAMAGE(3),
		MELEE_DAMAGE(0),
		RANGE_DAMAGE(1),
		MAGIC_DAMAGE(2),
		REFLECTED_DAMAGE(4),
		ABSORB_DAMAGE(5),
		POISON_DAMAGE(6),
		DESEASE_DAMAGE(7),
		HEALED_DAMAGE(9),
		CANNON_DAMAGE(13);
		
		@Getter
		@Setter
		private int mark;
		
		HitSplat(int mark) {
			this.setMark(mark);
		}
		
	}
}
