package com.rs.game.entity.actor.npc.impl.godwars.zaros;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.minigame.ZarosGodwars;
import com.rs.game.entity.actor.npc.NPC;

@SuppressWarnings("serial")
public class NexMinion extends NPC {

	private boolean hasNoBarrier;

	public NexMinion(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCantFollowUnderCombat(true);
		setCapDamage(0);
	}

	public void breakBarrier() {
		setCapDamage(-1);
		hasNoBarrier = true;
	}

	@Override
	public void processNPC() {
		if (isDead() || !hasNoBarrier)
			return;
		if (!getCombat().process())
			checkAgressivity();
	}

	@Override
	public void sendDeath(Actor source) {
		super.sendDeath(source);
		ZarosGodwars.moveNextStage();
	}

}
