package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class GeneralGraardorCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6260 };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Misc.getRandom(4) == 0) {
			switch (Misc.getRandom(10)) {
				case 0:
					npc.setNextForceTalk(new ForceTalk("Death to our enemies!"));
					npc.playSound(3219, 2);
					break;
				case 1:
					npc.setNextForceTalk(new ForceTalk("Brargh!"));
					npc.playSound(3209, 2);
					break;
				case 2:
					npc.setNextForceTalk(new ForceTalk("Break their bones!"));
					break;
				case 3:
					npc.setNextForceTalk(new ForceTalk("For the glory of Bandos!"));
					break;
				case 4:
					npc.setNextForceTalk(new ForceTalk("Split their skulls!"));
					npc.playSound(3229, 2);
					break;
				case 5:
					npc.setNextForceTalk(new ForceTalk("We feast on the bones of our enemies tonight!"));
					npc.playSound(3206, 2);
					break;
				case 6:
					npc.setNextForceTalk(new ForceTalk("CHAAARGE!"));
					npc.playSound(3220, 2);
					break;
				case 7:
					npc.setNextForceTalk(new ForceTalk("Crush them underfoot!"));
					npc.playSound(3224, 2);
					break;
				case 8:
					npc.setNextForceTalk(new ForceTalk("All glory to Bandos!"));
					npc.playSound(3205, 2);
					break;
				case 9:
					npc.setNextForceTalk(new ForceTalk("GRAAAAAAAAAR!"));
					npc.playSound(3207, 2);
					break;
				case 10:
					npc.setNextForceTalk(new ForceTalk("FOR THE GLORY OF THE BIG HIGH WAR GOD!"));
					break;
			}
		}
		if (Misc.getRandom(2) == 0) { // range magical attack
			npc.setNextAnimation(new Animation(7063));
			for (Actor t : npc.getPossibleTargets()) {
				delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 355, NPCConstants.RANGE, t)));
				RegionManager.sendProjectile(npc, t, 1200, 41, 16, 41, 35, 16, 0);
			}
		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackAnim()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCConstants.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}
