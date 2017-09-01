package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.utility.Misc;

/**
 * @author Tyler
 */
public class NomadCombat extends CombatScript {

	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(8528) });
	}

	// Melee Emote-12696
	// Mage Emote-9300
	// Range Gfx-451
	public int attack(NPC npc, Actor target) {
		int nomadAttack = Misc.getRandom(2);
		int nomadHit = Misc.getRandom(500);
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(12696));
			delayHit(npc, 1, target, getMeleeHit(npc, nomadHit));
		} else {
			switch (nomadAttack) {
				case 1:
					npc.setNextAnimation(new Animation(12697));
					delayHit(npc, 1, target, getMagicHit(npc, nomadHit));
					break;
			}
		}
		return defs.getAttackDelay();
	}

}