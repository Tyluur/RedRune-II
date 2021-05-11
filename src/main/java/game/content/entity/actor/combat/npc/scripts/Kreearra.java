package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.constants.BonusConstants;
import utility.functions.Misc;

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
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 260, BonusConstants.SLASH_ATTACK, target)));
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(6976));
		for (Actor t : npc.getPossibleTargets(true, true)) {
			if (Misc.getRandom(2) == 0) {
				sendMagicAttack(npc, t);
			} else {
				delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 720, BonusConstants.RANGE_ATTACK, t)));
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
		for (Actor t : npc.getPossibleTargets(true, true)) {
			delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 210, BonusConstants.MAGIC_ATTACK, t)));
			RegionManager.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
		}
	}
}
