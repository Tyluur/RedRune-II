package com.rs.game.entity.actor.npc.impl.others;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitSplat;
import com.rs.game.entity.actor.npc.NPC;

@SuppressWarnings("serial")
public class Lucien extends NPC {
	
	public Lucien(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setCapDamage(300);
		setCombatLevel(59999);
		this.setName("Zeniths #1 Boss");
		setRun(true);
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
	public void handleIngoingHit(Hit hit) {
		if (hit.getLook() != HitSplat.MELEE_DAMAGE && hit.getLook() != HitSplat.RANGE_DAMAGE && hit.getLook() != HitSplat.MAGIC_DAMAGE) {
			return;
		}
		super.handleIngoingHit(hit);
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.2);
			if (recoil > 0) {
				hit.getSource().applyHit(new Hit(this, recoil, HitSplat.REFLECTED_DAMAGE));
			}
		}
	}
}
