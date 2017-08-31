package com.rs.game.entity.actor.npc.combat;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.utility.Misc;

import java.util.HashMap;
import java.util.List;

public class CombatScriptsHandler {
	
	private static final HashMap<Object, CombatScript> cachedCombatScripts = new HashMap<Object, CombatScript>();
	
	private static final CombatScript DEFAULT_SCRIPT = new Default();
	
	@SuppressWarnings("rawtypes")
	public static void init() {
		try {
			List<Object> scripts = Misc.getClasses("com.rs.game.entity.actor.npc.combat.impl");
			for (Object o : scripts) {
				if (!(o instanceof CombatScript)) {
					System.out.println(o + " was not a combat script.");
					continue;
				}
				CombatScript script = (CombatScript) o;
				for (Object key : script.getKeys()) {
					cachedCombatScripts.put(key, script);
				}
			}
			System.out.println("Loaded " + cachedCombatScripts.size() + " cached combat scripts.");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static int specialAttack(final NPC npc, final Actor target) {
		CombatScript script = cachedCombatScripts.get(npc.getId());
		if (script == null) {
			script = cachedCombatScripts.get(npc.getDefinitions().name);
			if (script == null) {
				script = DEFAULT_SCRIPT;
			}
		}
		return script.attack(npc, target);
	}
}
