package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.world.World;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.utility.Misc;

public class MossTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7330, 7329 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(8223));
			npc.setNextGraphics(new Graphics(1460));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 160,
									NPCCombatDefinitions.MAGE, target)));
			for (Actor targets : npc.getPossibleTargets()) {
				World.sendProjectile(npc, targets, 1462, 34, 16, 30, 35, 16, 0);
				if (Misc.getRandom(3) == 0)// 1/3 chance of being poisioned
					targets.getPoisonManager().makePoisoned(58);
			}
		} else {
			damage = getRandomMaxHit(npc, 160, NPCCombatDefinitions.MELEE,
					target);
			npc.setNextAnimation(new Animation(8222));
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		}
		return defs.getAttackDelay();
	}

}
