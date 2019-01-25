package org.redrune.game.entity.actor.npc.impl.fightcave;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.utility.functions.Misc;

public class FightCaveNPC extends NPC {

	public FightCaveNPC(int id, WorldTile tile) {
		super(id, tile, Misc.getNameHash("FightCaves"), false, true);
	}
}