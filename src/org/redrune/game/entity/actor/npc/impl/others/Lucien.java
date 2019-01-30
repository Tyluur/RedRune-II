package org.redrune.game.entity.actor.npc.impl.others;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;

@SuppressWarnings("serial")
public class Lucien extends NPC {
	
	public Lucien(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setCapDamage(300);
		setCombatLevel(59999);
		this.setName("Zeniths #1 Boss");
		setRunModeOn(true);
		setForceMultiAttacked(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public void handleIncomingHit(Hit hit) {
		if (hit.getSplat() != HitSplat.MELEE_DAMAGE && hit.getSplat() != HitSplat.RANGE_DAMAGE && hit.getSplat() != HitSplat.MAGIC_DAMAGE) {
			return;
		}
		super.handleIncomingHit(hit);
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.2);
			if (recoil > 0) {
				hit.getSource().applyHit(new Hit(this, recoil, HitSplat.REFLECTED_DAMAGE));
			}
		}
	}
}
