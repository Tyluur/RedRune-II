package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.World;
import com.rs.utility.Misc;
import com.rs.utility.constants.NPCConstants;

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
				World.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
				WorldTile teleTile = t;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new WorldTile(t, 2);
					if (World.canMoveNPC(t.getPlane(), teleTile.getX(), teleTile.getY(), t.getSize())) {
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
			World.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
		}
	}
}
