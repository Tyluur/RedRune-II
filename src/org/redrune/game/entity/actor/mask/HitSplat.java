package org.redrune.game.entity.actor.mask;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
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
	private final int mark;
	
	HitSplat(int mark) {
		this.mark = mark;
	}
}
