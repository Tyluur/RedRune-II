package org.redrune.utility.game.repository.npc;

import org.redrune.game.entity.actor.npc.NPC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public class NPCWalkingFlag {
	
	/**
	 * The map of walk flags
	 */
	private static final Map<Integer, Boolean> WALK_FLAGS = new HashMap<>();
	
	/**
	 * Registers all the flags
	 */
	public static void registerFlags() {
		WALK_FLAGS.put(1597, false);
		WALK_FLAGS.put(3385, false);
		WALK_FLAGS.put(4361, false);
		WALK_FLAGS.put(11248, true);
		WALK_FLAGS.put(2024, false);
		WALK_FLAGS.put(1918, false);
		WALK_FLAGS.put(1334, false);
		WALK_FLAGS.put(1411, false);
		WALK_FLAGS.put(33, false);
		WALK_FLAGS.put(1288, false);
		WALK_FLAGS.put(945, false);
		WALK_FLAGS.put(2998, false);
		WALK_FLAGS.put(1699, false);
		WALK_FLAGS.put(1658, false);
		WALK_FLAGS.put(550, false);
		WALK_FLAGS.put(599, false);
		WALK_FLAGS.put(549, false);
		WALK_FLAGS.put(1866, false);
		WALK_FLAGS.put(554, false);
		WALK_FLAGS.put(5112, false);
		WALK_FLAGS.put(278, false);
		WALK_FLAGS.put(519, false);
		WALK_FLAGS.put(6370, false);
		WALK_FLAGS.put(211, false);
		WALK_FLAGS.put(3001, false);
		WALK_FLAGS.put(4516, false);
		WALK_FLAGS.put(2258, false);
		WALK_FLAGS.put(2892, false);
		WALK_FLAGS.put(4293, false);
		WALK_FLAGS.put(2894, false);
		WALK_FLAGS.put(2896, false);
		WALK_FLAGS.put(2328, false);
		WALK_FLAGS.put(4288, false);
		WALK_FLAGS.put(682, false);
		WALK_FLAGS.put(1303, false);
		WALK_FLAGS.put(608, false);
		WALK_FLAGS.put(588, false);
		WALK_FLAGS.put(1778, false);
		WALK_FLAGS.put(6970, false);
		WALK_FLAGS.put(599, false);
		WALK_FLAGS.put(6539, false);
		WALK_FLAGS.put(6537, false);
		WALK_FLAGS.put(4653, false);
		WALK_FLAGS.put(14332, true);
		WALK_FLAGS.put(961, false);
		WALK_FLAGS.put(4247, false);
		WALK_FLAGS.put(4375, false);
		WALK_FLAGS.put(2824, false);
		WALK_FLAGS.put(946, false);
		WALK_FLAGS.put(947, false);
		WALK_FLAGS.put(949, false);
		System.out.println("Registered " + WALK_FLAGS.size() + " custom walk flags.");
	}
	
	/**
	 * Checks if the map flags contains a key
	 */
	public static boolean containsKey(int npcId) {
		return WALK_FLAGS.containsKey(npcId);
	}
	
	/**
	 * Gets the flag for the npc walking data
	 *
	 * @param npcId
	 * 		The npc
	 */
	public static int getWalkingFlag(int npcId) {
		if (!WALK_FLAGS.containsKey(npcId)) {
			return NPC.NO_WALK;
		} else {
			Boolean walking = WALK_FLAGS.get(npcId);
			if (walking) {
				return NPC.NORMAL_WALK;
			} else {
				return NPC.NO_WALK;
			}
		}
	}
}
