package org.redrune.game.entity.actor.mask;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;

public final class Hit {
	
	/**
	 * The source of the damage
	 */
	private Actor source;
	
	/**
	 * The hit splat of the damage
	 */
	private HitSplat splat;
	
	/**
	 * The amount of damage
	 */
	private int damage;
	
	/**
	 * The soaking damage
	 */
	private Hit soaking;
	
	/**
	 * The delay (used only in the mask aspect) of the hit
	 */
	private int delay;
	
	/**
	 * The max hit possible to land
	 */
	private int maxHit = -1;
	
	/**
	 * The task to execute once the hit lands
	 */
	private Runnable landTask;
	
	public Hit(Actor source, int damage, HitSplat splat) {
		this(source, damage, splat, 0);
	}
	
	public Hit(Actor source, int damage, HitSplat splat, int delay) {
		this.source = source;
		this.damage = damage;
		this.splat = splat;
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
	
	/**
	 * If the hit missed
	 */
	public boolean missed() {
		return damage == 0;
	}
	
	/**
	 * Gets the hitmark of the hit
	 */
	public int getMark(Player player, Actor victim) {
		if (HitSplat.HEALED_DAMAGE == splat) {
			return splat.getMark();
		}
		if (damage == 0) {
			return HitSplat.MISSED.getMark();
		}
		int mark = splat.getMark();
		if (isCritical()) {
			mark += 10;
		}
		if (!interactingWith(player, victim)) {
			mark += 14;
		}
		return mark;
	}
	
	/**
	 * If the player is interacting with the victim, used to shade hitsplats more
	 */
	public boolean interactingWith(Player player, Actor victim) {
		return player == victim || player == source;
	}
	
	/**
	 * Sets the max hit and
	 *
	 * @return This instance for chaining
	 */
	public Hit setMaxHit(int maxHit) {
		this.maxHit = maxHit;
		return this;
	}
	
	@Override
	public String toString() {
		return "Hit{" + "source=" + source + ", splat=" + splat + ", damage=" + damage + ", soaking=" + soaking + ", delay=" + delay + ", maxHit=" + maxHit + '}';
	}
	
	/**
	 * Fires the hit land task
	 */
	public void fireLandTask() {
		if (landTask == null) {
			return;
		}
		landTask.run();
	}

	public boolean isMagicHit() {
		return getSplat() == HitSplat.MAGIC_DAMAGE;
	}

	public boolean isMeleeHit() {
		return getSplat() == HitSplat.MELEE_DAMAGE;
	}

	public boolean isRangeHit() {
		return getSplat() == HitSplat.RANGE_DAMAGE;
	}

    public Actor getSource() {
        return this.source;
    }

    public HitSplat getSplat() {
        return this.splat;
    }

    public int getDamage() {
        return this.damage;
    }

    public Hit getSoaking() {
        return this.soaking;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setSource(Actor source) {
        this.source = source;
    }

    public void setSplat(HitSplat splat) {
        this.splat = splat;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSoaking(Hit soaking) {
        this.soaking = soaking;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setLandTask(Runnable landTask) {
        this.landTask = landTask;
    }
}
