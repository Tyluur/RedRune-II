package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.npc.impl.familiar.Familiar;
import game.global.map.region.RegionManager;
import utility.constants.BonusConstants;

public class MinotaurCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze Minotaur", "Iron Minotaur", "Steel Minotaur", "Mithril Minotaur", "Adamant Minotaur", "Rune Minotaur" };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8026));
			npc.setNextGraphics(new Graphics(1334));
			RegionManager.sendProjectile(npc, target, 1333, 34, 16, 30, 35, 16, 0);
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 40, BonusConstants.MAGIC_ATTACK, target)));
		}
		return defs.getAttackDelay();
	}
}
