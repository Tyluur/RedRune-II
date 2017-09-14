package com.rs.game.content.combat.npc;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.constants.NPCConstants;

public class DefaultCombatScript extends CombatScript {
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Default" };
	}
	
	@Override
	public int attack(NPC npc, Actor target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (attackStyle == NPCConstants.MELEE) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target)));
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target);
			delayHit(npc, 2, target, attackStyle == NPCConstants.RANGE ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
			if (defs.getAttackProjectile() != -1) {
				RegionManager.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
			}
		}
		if (defs.getAttackGfx() != -1) {
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		}
		npc.setNextAnimation(new Animation(defs.getAttackAnim()));
		return defs.getAttackDelay();
	}
}
