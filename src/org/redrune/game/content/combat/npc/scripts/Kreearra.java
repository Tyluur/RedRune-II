package org.redrune.game.content.combat.npc.scripts;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class Kreearra extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6222 };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (!npc.isUnderCombat()) {
			npc.setNextAnimation(new Animation(6997));
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 260, NPCConstants.MELEE, target)));
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(6976));
		for (Actor t : npc.getPossibleTargets()) {
			if (Misc.getRandom(2) == 0) {
				sendMagicAttack(npc, t);
			} else {
				delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 720, NPCConstants.RANGE, t)));
				RegionManager.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
				WorldTile teleTile = t;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new WorldTile(t, 2);
					if (RegionManager.canMoveNPC(t.getPlane(), teleTile.getX(), teleTile.getY(), t.getSize())) {
						break;
					}
				}
				t.setNextWorldTile(teleTile);
			}
		}
		return defs.getAttackDelay();
	}

	private void sendMagicAttack(NPC npc, Actor target) {
		npc.setNextAnimation(new Animation(6976));
		for (Actor t : npc.getPossibleTargets()) {
			delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 210, NPCConstants.MAGE, t)));
			RegionManager.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
		}
	}
}
