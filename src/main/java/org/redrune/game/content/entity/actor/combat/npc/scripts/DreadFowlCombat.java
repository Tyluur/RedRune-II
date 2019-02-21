package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class DreadFowlCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6825, 6824 };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(7810));
			npc.setNextGraphics(new Graphics(1318));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 40, NPCConstants.MAGE, target)));
			RegionManager.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
		} else {
			if (Misc.getRandom(10) == 0) {// 1/10 chance of random special
				// (weaker)
				npc.setNextAnimation(new Animation(7810));
				npc.setNextGraphics(new Graphics(1318));
				delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 30, NPCConstants.MAGE, target)));
				RegionManager.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
			} else {
				npc.setNextAnimation(new Animation(7810));
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 30, NPCConstants.MELEE, target)));
			}
		}
		return defs.getAttackDelay();
	}
}
