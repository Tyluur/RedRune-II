package com.rs.game.entity.actor.npc.impl.fightcave;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.utility.Misc;

public class FightCaveNPC extends NPC {

	public FightCaveNPC(int id, WorldTile tile) {
		super(id, tile, Misc.getNameHash("FightCaves"), false, true);
	}
}