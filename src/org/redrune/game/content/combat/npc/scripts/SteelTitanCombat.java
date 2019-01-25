package org.redrune.game.content.combat.npc.scripts;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class SteelTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7344, 7343 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		boolean distant = false;
		int size = npc.getSize();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			distant = true;
		}
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(8190));
			target.setNextGraphics(new Graphics(1449));
			if (distant) {// range hit
				delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.RANGE, target)), getRangeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.RANGE, target)), getRangeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.RANGE, target)), getRangeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.RANGE, target)));
			} else {// melee hit
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.MELEE, target)), getMeleeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.MELEE, target)), getMeleeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.MELEE, target)), getMeleeHit(npc, getRandomMaxHit(npc, 244, NPCConstants.MELEE, target)));
			}
		} else {
			if (distant) {
				int attackStage = Misc.getRandom(1);// 2
				switch (attackStage) {
					case 0:// magic
						damage = getRandomMaxHit(npc, 255, NPCConstants.MAGE, target);
						npc.setNextAnimation(new Animation(7694));
						RegionManager.sendProjectile(npc, target, 1451, 34, 16, 30, 35, 16, 0);
						delayHit(npc, 2, target, getMagicHit(npc, damage));
						break;
					case 1:// range
						damage = getRandomMaxHit(npc, 244, NPCConstants.RANGE, target);
						npc.setNextAnimation(new Animation(8190));
						RegionManager.sendProjectile(npc, target, 1445, 34, 16, 30, 35, 16, 0);
						delayHit(npc, 2, target, getRangeHit(npc, damage));
						break;
				}
			} else {// melee
				damage = getRandomMaxHit(npc, 244, NPCConstants.MELEE, target);
				npc.setNextAnimation(new Animation(8183));
				delayHit(npc, 1, target, getMeleeHit(npc, damage));
			}
		}
		return defs.getAttackDelay();
	}
}
