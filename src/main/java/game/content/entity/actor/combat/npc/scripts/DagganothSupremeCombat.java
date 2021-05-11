package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.global.map.region.RegionManager;
import utility.constants.BonusConstants;

/**
 * @author Tyluur<itstyluur@icloud.com>
 * @since 7/5/2015
 */
public class DagganothSupremeCombat extends CombatScript {
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 2881, 2882, 2883 };
	}
	
	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage;
		switch (npc.getId()) {
			case 2881: // supreme (range)
				damage = getRandomMaxHit(npc, 300, BonusConstants.RANGE_ATTACK, target);
				npc.setNextAnimation(new Animation(defs.getAttackAnim()));
				npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
				delayHit(npc, 2, target, getRangeHit(npc, damage));
				RegionManager.sendProjectile(npc, target, 475, 41, 16, 41, 35, 16, 0);
				break;
			case 2882: // prime (mage)
				damage = getRandomMaxHit(npc, 300, BonusConstants.MAGIC_ATTACK, target);
				npc.setNextAnimation(new Animation(defs.getAttackAnim()));
				npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
				delayHit(npc, 2, target, getMagicHit(npc, damage));
				RegionManager.sendProjectile(npc, target, 2707, 41, 16, 41, 35, 16, 0);
				break;
			case 2883: // rex (melee)
				damage = getRandomMaxHit(npc, 300, BonusConstants.SLASH_ATTACK, target);
				npc.setNextAnimation(new Animation(defs.getAttackAnim()));
				delayHit(npc, 0, target, getMeleeHit(npc, damage));
				break;
		}
		return defs.getAttackDelay();
	}
}
