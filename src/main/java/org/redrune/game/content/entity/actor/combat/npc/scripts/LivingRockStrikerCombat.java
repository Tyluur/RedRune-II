/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.BonusConstants;

/**
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
			for (Actor t : npc.getPossibleTargets(true, true)) {
				delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 140, BonusConstants.RANGE_ATTACK, t)));
				RegionManager.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
			}
		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackAnim()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.SLASH_ATTACK, target)));
		}
		return defs.getAttackDelay();
	}
}
