package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.world.World;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;

public class ThornySnailCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6807, 6806 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(8148));
			npc.setNextGraphics(new Graphics(1385));
			World.sendProjectile(npc, target, 1386, 34, 16, 30, 35, 16, 0);
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 80,
									NPCCombatDefinitions.RANGE, target)));
			npc.setNextGraphics(new Graphics(1387));
		} else {
			npc.setNextAnimation(new Animation(8143));
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 40,
									NPCCombatDefinitions.RANGE, target)));
		}
		return defs.getAttackDelay();
	}

}
