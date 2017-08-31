package com.rs.game.entity.actor.npc.impl.others;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.minigame.CastleWars;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.utility.Misc;

@SuppressWarnings("serial")
public class CastleWarBarricade extends NPC {

	private int team;

	public CastleWarBarricade(int team, WorldTile tile) {
		super(1532, tile, -1, true, true);
		setCantFollowUnderCombat(true);
		this.team = team;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
		if (getId() == 1533 && Misc.getRandom(20) == 0)
			sendDeath(this);
	}

	public void litFire() {
		transformIntoNPC(1533);
		sendDeath(this);
	}

	public void explode() {
		// TODO gfx
		sendDeath(this);
	}

	@Override
	public void sendDeath(Actor killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		if (this.getId() != 1533) {
			setNextAnimation(null);
			reset();
			setLocation(getRespawnTile());
			finish();
		} else {
			super.sendDeath(killer);
		}
		CastleWars.removeBarricade(team, this);
	}

}
