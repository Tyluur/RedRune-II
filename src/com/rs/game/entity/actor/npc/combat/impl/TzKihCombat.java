package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.NPCConstants;

public class TzKihCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "tz-kih" };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (npc instanceof Familiar) {// TODO get anim and gfx
			if (usingSpecial) {
				for (Actor actor : npc.getPossibleTargets()) {
					damage = getRandomMaxHit(npc, 70, NPCConstants.MAGE, target);
					Player player = (Player) target;
					if (player.getTemporaryAttributtes().get("drainingPrayer") != null) {
						player.getPrayer().drainPrayer(damage);
					} else {
						delayHit(npc, 1, actor, getMagicHit(npc, damage));
					}
				}
			}
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(8257));
		damage = getRandomMaxHit(npc, 50, NPCConstants.MAGE, target);
		Player player = (Player) target;
		if (player.getTemporaryAttributtes().get("drainingPrayer") != null) {
			player.getPrayer().drainPrayer(damage);
		} else {
			delayHit(npc, 1, target, getMagicHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}
