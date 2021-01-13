package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

/**
 * @author Humid
 */
public class JadCombat extends CombatScript {

	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(14221), Integer.valueOf(2745), Integer.valueOf(15208) });
	}

	// Range Emote-9276
	// Melee Emote-9277
	// Mage Emote-9300
	// Range Gfx-451
	public int attack(NPC npc, Actor target) {
		int jadAttack = Misc.getRandom(2);
		int jadHit = Misc.getRandom(500);
		NPCCombatDefinitions defs = npc.getCombatDefinitions();

		if (target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(16204));
			delayHit(npc, 1, target, getMeleeHit(npc, jadHit));
		} else {
			switch (jadAttack) {
				case 1:
					npc.setNextAnimation(new Animation(16195));
					npc.setNextGraphics(new Graphics(2995));
					delayHit(npc, 1, target, getMagicHit(npc, jadHit));
					break;
				case 2:
					npc.setNextAnimation(new Animation(16202));
					npc.setNextGraphics(new Graphics(2994));
					RegionManager.sendProjectile(npc, target, 1627, 41, 16, 41, 35, 16, 0);
					delayHit(npc, 1, target, getRangeHit(npc, jadHit));
					break;
			}
		}
		return defs.getAttackDelay();
	}

}