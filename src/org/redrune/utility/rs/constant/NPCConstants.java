package org.redrune.utility.rs.constant;

import static org.redrune.game.content.combat.player.CombatType.*;

/**
 * Constants for npcs
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public interface NPCConstants {
	
	/**
	 * The melee combat style
	 */
	int MELEE_COMBAT_STYLE = MELEE.ordinal();
	
	/**
	 * The range combat style
	 */
	int RANGE_COMBAT_STYLE = RANGE.ordinal();
	
	/**
	 * The magic combat style
	 */
	int MAGIC_COMBAT_STYLE = MAGIC.ordinal();
	
	/**
	 * The passive aggressive type
	 */
	int PASSIVE_AGGRESSIVE = 0;
	
	/**
	 * The force aggressive type
	 */
	int FORCE_AGGRESSIVE = 1;
}
