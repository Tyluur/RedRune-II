package org.redrune.game.node.entity.npc.extension;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.utility.rs.constant.Directions.Direction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/25/2017
 */
public class RockCrabNPC extends NPC {
	
	/**
	 * The original id of the rock crab
	 */
	private final int originalId;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public RockCrabNPC(int id, Location location, Direction direction) {
		super(id, location, direction);
		this.originalId = id;
		getCombatManager().setAggressiveForced(true);
		getCombatManager().setFindTargetRadius(1);
		System.out.println("Constructed a rock crab");
	}
	
	@Override
	public int getCombatLevel() {
		return 13;
	}
	
	@Override
	public void startFight(Entity target) {
		if (getId() == originalId - 1) {
			transform(originalId - 1);
			setHealthPoints(getMaxHealth());
			getCombatManager().setFindTargetRadius(16);
			System.out.println("Started a fight with " + target);
		}
		super.startFight(target);
	}
}
