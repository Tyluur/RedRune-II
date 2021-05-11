package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.ForceTalk;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.player.Player;
import utility.constants.BonusConstants;
import utility.functions.Misc;

public class EvilChickenCombat extends CombatScript {
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Evil Chicken" };
	}
	
	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackAnim()));
		switch (Misc.getRandom(5)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("Bwuk"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Bwuk bwuk bwuk"));
				break;
			case 2:
				String name = "";
				if (target instanceof Player) {
					name = ((Player) target).getDisplayName();
				}
				npc.setNextForceTalk(new ForceTalk("Flee from me, " + name));
				break;
			case 3:
				name = "";
				if (target instanceof Player) {
					name = ((Player) target).getDisplayName();
				}
				npc.setNextForceTalk(new ForceTalk("Begone, " + name));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("Bwaaaauuuuk bwuk bwuk"));
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("MUAHAHAHAHAAA!"));
				break;
		}
		target.setNextGraphics(new Graphics(337));
		delayHit(npc, 0, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.MAGIC_ATTACK, target)));
		return defs.getAttackDelay();
	}
}
