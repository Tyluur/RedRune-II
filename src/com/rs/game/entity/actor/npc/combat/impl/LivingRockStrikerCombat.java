/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.world.World;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;

/**
 * 
 * @author Owner
 */
public class LivingRockStrikerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 8833 };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, 10)) { // range magical attack
			npc.setNextAnimation(new Animation(1296));
			for (Actor t : npc.getPossibleTargets()) {
				delayHit(
						npc,
						1,
						t,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, 140,
										NPCCombatDefinitions.RANGE, t)));
				World.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
			}
		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}
