package com.rs.game.entity.actor.mask;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import lombok.Getter;
import lombok.Setter;

public final class Hit {
	
	/**
	 * The source of the damage
	 */
	@Getter
	@Setter
	private Actor source;
	
	/**
	 * The hit splat of the damage
	 */
	@Getter
	@Setter
	private HitSplat look;
	
	/**
	 * The amount of damage
	 */
	@Getter
	@Setter
	private int damage;
	
	/**
	 * The soaking damage
	 */
	@Getter
	@Setter
	private Hit soaking;
	
	/**
	 * The delay (used only in the mask aspect) of the hit
	 */
	@Getter
	@Setter
	private int delay;
	
	/**
	 * The max hit possible to land
	 */
	private int maxHit = -1;
	
	public Hit(Actor source, int damage, HitSplat look) {
		this(source, damage, look, 0);
	}
	
	public Hit(Actor source, int damage, HitSplat look, int delay) {
		this.source = source;
		this.damage = damage;
		this.look = look;
		this.delay = delay;
	}
	
	/**
	 * Checks if the hit is critical, based on the max hit and the hit landed.
	 */
	public boolean isCritical() {
		if (maxHit == -1 || damage == 0) {
			return false;
		}
		double criticalMinimum = maxHit * 0.90;
		return damage >= criticalMinimum;
	}
	
	public boolean missed() {
		return damage == 0;
	}
	
	public int getMark(Player player, Actor victm) {
		if (HitSplat.HEALED_DAMAGE == look) {
			return look.getMark();
		}
		if (damage == 0) {
			return HitSplat.MISSED.getMark();
		}
		int mark = look.getMark();
		if (isCritical()) {
			mark += 10;
		}
		if (!interactingWith(player, victm)) {
			mark += 14;
		}
		return mark;
	}
	
	public boolean interactingWith(Player player, Actor victm) {
		return player == victm || player == source;
	}
	
	public Hit setMaxHit(int maxHit) {
		this.maxHit = maxHit;
		return this;
	}
	
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
		
		private int mark;
		
		HitSplat(int mark) {
			this.mark = mark;
		}
		
		public int getMark() {
			return mark;
		}
	}
	
}
