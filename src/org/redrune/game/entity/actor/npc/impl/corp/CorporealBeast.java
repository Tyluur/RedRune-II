package org.redrune.game.entity.actor.npc.impl.corp;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;

@SuppressWarnings("serial")
public class CorporealBeast extends NPC {
	
	private DarkEnergyCore core;
	
	public CorporealBeast(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(1000);
		setLureDelay(3000);
		setForceTargetDistance(64);
		setForceFollowClose(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead()) {
			return;
		}
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && getPossibleTargets().isEmpty()) {
			setHitpoints(maxhp);
		}
	}
	
	@Override
	public void sendDeath(Actor source) {
		super.sendDeath(source);
		if (core != null) {
			core.sendDeath(source);
		}
	}
	
	public void spawnDarkEnergyCore() {
		if (core != null) {
			return;
		}
		core = new DarkEnergyCore(this);
	}
	
	public void removeDarkEnergyCore() {
		if (core == null) {
			return;
		}
		core.finish();
		core = null;
	}
	
}
