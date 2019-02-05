package org.redrune.game.content.combat.npc;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.utility.functions.Misc;

import java.util.HashMap;
import java.util.List;

/**
 * @author Matrix Team
 * @author Tyluur <itstyluur@gmail.com>
 */
public final class CombatScriptsHandler {
	
	/**
	 * The map of cached combat scripts
	 */
	public static final HashMap<Object, CombatScript> CACHED_COMBAT_SCRIPTS = new HashMap<>();
	
	/**
	 * The default combat script
	 */
	private static final CombatScript DEFAULT_SCRIPT = new DefaultCombatScript();
	
	/**
	 * Dynamically registers all npc combat scripts
	 */
	public static void registerAll() {
		try {
			List<Object> scripts = Misc.getClasses(CombatScriptsHandler.class.getPackage().getName() + ".scripts");
			for (Object o : scripts) {
				if (!(o instanceof CombatScript)) {
					System.out.println(o + " was not a combat script.");
					continue;
				}
				CombatScript script = (CombatScript) o;
				for (Object key : script.getKeys()) {
					CACHED_COMBAT_SCRIPTS.put(key, script);
				}
			}
			System.out.println("Loaded " + CACHED_COMBAT_SCRIPTS.size() + " cached combat scripts.");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fires the combat script for an npc
	 *
	 * @return The delay until the next swing
	 */
	public static int fireCombatScript(final NPC npc, final Actor target) {
		CombatScript script = CACHED_COMBAT_SCRIPTS.get(npc.getId());
		if (script == null) {
			script = CACHED_COMBAT_SCRIPTS.get(npc.getDefinitions().getName());
			if (script == null) {
				script = DEFAULT_SCRIPT;
			}
		}
		return script.attack(npc, target);
	}
}
