package com.rs.game.content.node.npc;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.WorldTile;

import java.util.HashMap;

public class FishingSpotsHandler {

	public static final HashMap<WorldTile, WorldTile> moveSpots = new HashMap<WorldTile, WorldTile>();

	public static void init() {
		moveSpots.put(new WorldTile(2836, 3431, 0),
				new WorldTile(2845, 3429, 0));
		moveSpots.put(new WorldTile(2853, 3423, 0),
				new WorldTile(2860, 3426, 0));
		moveSpots.put(new WorldTile(3110, 3432, 0),
				new WorldTile(3104, 3423, 0));
		moveSpots.put(new WorldTile(3104, 3424, 0),
				new WorldTile(3110, 3433, 0));
	}

	public static boolean moveSpot(NPC npc) {
		WorldTile key = new WorldTile(npc);
		WorldTile spot = moveSpots.get(key);
		if (spot == null && moveSpots.containsValue(key)) {
			for (WorldTile k : moveSpots.keySet()) {
				WorldTile v = moveSpots.get(k);
				if (v.getX() == key.getY() && v.getY() == key.getX()
						&& v.getPlane() == key.getPlane()) {
					spot = k;
					break;
				}
			}
		}
		if (spot == null)
			return false;
		npc.setNextWorldTile(spot);
		return true;
	}

}
