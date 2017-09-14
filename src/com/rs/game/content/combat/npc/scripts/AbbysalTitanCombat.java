package com.rs.game.content.combat.npc.scripts;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.combat.npc.CombatScript;
import com.rs.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.NPCConstants;

public class AbbysalTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7350, 7349 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = 0;
		damage = getRandomMaxHit(npc, 140, NPCConstants.MELEE, target);
		npc.setNextAnimation(new Animation(7980));
		npc.setNextGraphics(new Graphics(1490));

		if (target instanceof Player) { // cjay failed dragonkk saved the day
			Player player = (Player) target;
			if (damage > 0 && player.getPrayer().getPrayerpoints() > 0) {
				player.getPrayer().drainPrayer(damage / 2);
			}
		}
		delayHit(npc, 1, target, getMeleeHit(npc, damage));
		return defs.getAttackDelay();
	}
}
