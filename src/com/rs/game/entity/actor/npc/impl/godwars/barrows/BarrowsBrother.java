package com.rs.game.entity.actor.npc.impl.godwars.barrows;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.controler.impl.activity.Barrows;

@SuppressWarnings("serial")
public class BarrowsBrother extends NPC {

	private Barrows barrows;

	public BarrowsBrother(int id, WorldTile tile, Barrows barrows) {
		super(id, tile, -1, true, true);
		this.barrows = barrows;
	}

	@Override
	public void sendDeath(Actor source) {
		super.sendDeath(source);
		barrows.killedBrother();
	}

}
