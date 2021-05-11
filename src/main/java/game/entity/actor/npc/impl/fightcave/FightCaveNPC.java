package game.entity.actor.npc.impl.fightcave;

import game.entity.actor.npc.NPC;
import game.global.WorldTile;
import utility.functions.Misc;

public class FightCaveNPC extends NPC {

	public FightCaveNPC(int id, WorldTile tile) {
		super(id, tile, Misc.getNameHash("FightCaves"), false, true);
	}
}