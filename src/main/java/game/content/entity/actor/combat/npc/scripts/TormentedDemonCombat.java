package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.npc.impl.others.TormentedDemon;
import game.global.map.region.RegionManager;
import utility.constants.BonusConstants;
import utility.functions.Misc;

public class TormentedDemonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Tormented demon" };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		TormentedDemon torm = (TormentedDemon) npc;
		int hit = 0;
		int attackStyle = torm.getFixedAmount() == 0 ? Misc.getRandom(2) : torm.getFixedCombatType();
		if (torm.getFixedAmount() == 0) {
			torm.setFixedCombatType(attackStyle);
		}
		switch (attackStyle) {
			case 0:
				if (npc.withinDistance(target, 3)) {
					hit = getRandomMaxHit(npc, 189, BonusConstants.SLASH_ATTACK, target);
					npc.setNextAnimation(new Animation(10922));
					npc.setNextGraphics(new Graphics(1886));
					delayHit(npc, 1, target, getMeleeHit(npc, hit));
				}
				return defs.getAttackDelay();
			case 1:
				hit = getRandomMaxHit(npc, 270, BonusConstants.MAGIC_ATTACK, target);
				npc.setNextAnimation(new Animation(10918));
				npc.setNextGraphics(new Graphics(1883, 0, 96 << 16));
				RegionManager.sendProjectile(npc, target, 1884, 34, 16, 30, 35, 16, 0);
				delayHit(npc, 1, target, getMagicHit(npc, hit));
				break;
			case 2:
				hit = getRandomMaxHit(npc, 270, BonusConstants.RANGE_ATTACK, target);
				npc.setNextAnimation(new Animation(10919));
				npc.setNextGraphics(new Graphics(1888));
				RegionManager.sendProjectile(npc, target, 1887, 34, 16, 30, 35, 16, 0);
				delayHit(npc, 1, target, getRangeHit(npc, hit));
				break;
		}
		torm.setFixedAmount(torm.getFixedAmount() + 1);
		return defs.getAttackDelay();
	}
}
