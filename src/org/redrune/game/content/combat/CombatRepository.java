package org.redrune.game.content.combat;

import org.redrune.game.content.combat.npc.CombatScript;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public final class CombatRepository {
	
	/**
	 * The map of cached combat scripts
	 */
	public static final HashMap<Object, CombatScript> CACHED_COMBAT_SCRIPTS = new HashMap<>();
}
