package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;

public class LavaTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7342, 7341 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(7883));
			npc.setNextGraphics(new Graphics(1491));
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 140, BonusConstants.SLASH_ATTACK, target)));
			if (damage <= 4 && target instanceof Player) {
				Player player = (Player) target;
				player.getCombatDefinitions().decreaseSpecialEnergy((player.getCombatDefinitions().getSpecialAttackPercentage() / 10));
			}
		} else {
			damage = getRandomMaxHit(npc, 140, BonusConstants.SLASH_ATTACK, target);
			npc.setNextAnimation(new Animation(7980));
			npc.setNextGraphics(new Graphics(1490));
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		}
		if (Misc.getRandom(10) == 0)// 1/10 chance of happening
		{
			delayHit(npc, 1, target, getMeleeHit(npc, Misc.getRandom(50)));
		}
		return defs.getAttackDelay();
	}
}
