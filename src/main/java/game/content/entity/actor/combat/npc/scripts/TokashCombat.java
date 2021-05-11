package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import utility.functions.Misc;

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