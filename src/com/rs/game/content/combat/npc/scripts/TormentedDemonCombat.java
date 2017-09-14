package com.rs.game.content.combat.npc.scripts;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.combat.npc.CombatScript;
import com.rs.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.others.TormentedDemon;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.Misc;
import com.rs.utility.constants.NPCConstants;

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
					hit = getRandomMaxHit(npc, 189, NPCConstants.MELEE, target);
					npc.setNextAnimation(new Animation(10922));
					npc.setNextGraphics(new Graphics(1886));
					delayHit(npc, 1, target, getMeleeHit(npc, hit));
				}
				return defs.getAttackDelay();
			case 1:
				hit = getRandomMaxHit(npc, 270, NPCConstants.MAGE, target);
				npc.setNextAnimation(new Animation(10918));
				npc.setNextGraphics(new Graphics(1883, 0, 96 << 16));
				RegionManager.sendProjectile(npc, target, 1884, 34, 16, 30, 35, 16, 0);
				delayHit(npc, 1, target, getMagicHit(npc, hit));
				break;
			case 2:
				hit = getRandomMaxHit(npc, 270, NPCConstants.RANGE, target);
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
