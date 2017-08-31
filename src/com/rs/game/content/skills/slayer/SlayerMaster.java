package com.rs.game.content.skills.slayer;

import java.util.HashMap;
import java.util.Map;

/**
 * A list of slayer masters
 * 
 * @author Emperial
 * 
 */
public enum SlayerMaster {
	TURAEL(TaskSet.TURAEL, 8273, 3);

	/**
	 * The slayer master map Integer is the npc id
	 */
	public static final Map<Integer, SlayerMaster> SLAYER_MASTERS = new HashMap<Integer, SlayerMaster>();

	/**
	 * Grab a slayer master by id
	 * 
	 * @param id
	 * @return
	 */
	public static SlayerMaster getMaster(int id) {
		return SLAYER_MASTERS.get(id);
	}

	/**
	 * Populate the mapping.
	 */
	static {
		for (SlayerMaster master : SlayerMaster.values()) {
			SLAYER_MASTERS.put(master.npcId, master);
		}
	}

	SlayerMaster(TaskSet type, int npcId, int requiredCombatLevel) {
		this.type = type;
		this.npcId = npcId;
		this.requiredCombatLevel = requiredCombatLevel;
	}

	/**
	 * The NPC id of the slayer master
	 */
	public int npcId;
	/**
	 * The task set that the slayer master assigns
	 */
	public TaskSet type;
	/**
	 * The combat level required to get tasks from this slayer master
	 */
	public int requiredCombatLevel;
}