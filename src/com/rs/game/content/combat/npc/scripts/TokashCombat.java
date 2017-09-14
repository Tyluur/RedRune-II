package com.rs.game.content.combat.npc.scripts;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.combat.npc.CombatScript;
import com.rs.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import com.rs.utility.Misc;

public class TokashCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "To'Kash the Bloodchiller" };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		int attack = Misc.getRandom(3);
		int hit = Misc.getRandom(400 + npc.getCombatLevel());
		switch (attack) {
			case 2:
			case 3:
			case 0:
				npc.setNextAnimation(new Animation(14392));
				delayHit(npc, 2, target, getMeleeHit(npc, hit));
				break;
			case 1:
				npc.setNextAnimation(new Animation(14525));
				npc.setNextGraphics(new Graphics(3003));
				target.setNextGraphics(new Graphics(3005));
				delayHit(npc, 2, target, getMagicHit(npc, hit + 100));
				break;
		}
		return defs.getAttackDelay();
	}
}