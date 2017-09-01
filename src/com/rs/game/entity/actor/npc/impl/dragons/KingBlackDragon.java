package com.rs.game.entity.actor.npc.impl.dragons;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;

@SuppressWarnings("serial")
public class KingBlackDragon extends NPC {
	
	public KingBlackDragon(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}
	
	public static boolean atKBD(WorldTile tile) {
		return (tile.getX() >= 2250 && tile.getX() <= 2292) && (tile.getY() >= 4675 && tile.getY() <= 4710);
	}
	
}
