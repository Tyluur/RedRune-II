package game.content.entity.actor.combat.npc.scripts;

import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.mask.Hit;
import game.entity.actor.mask.HitSplat;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import utility.constants.BonusConstants;
import utility.functions.Misc;

import java.util.ArrayList;

public class AsteaFrostwebCombat extends CombatScript {
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Astea Frostweb" };
	}
	
	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		// if (Utils.getRandom(10) == 0) {
		// AsteaFrostweb boss = (AsteaFrostweb) npc;
		// boss.spawnSpider();
		// }
		if (Misc.getRandom(10) == 0) { // spikes
			ArrayList<Actor> possibleTargets = npc.getPossibleTargets(true, true);
			npc.setNextAnimation(new Animation(defs.getAttackAnim()));
			for (Actor t : possibleTargets) {
				delayHit(npc, 1, t, new Hit(npc, Misc.getRandom(defs.getMaxHit()), HitSplat.REGULAR_DAMAGE));
			}
			return defs.getAttackDelay();
		} else {
			int attackStyle = Misc.getRandom(1);
			if (attackStyle == 1) { // check melee
				if (Misc.getDistance(npc.getX(), npc.getY(), target.getX(), target.getY()) > 1) {
					attackStyle = 0; // set mage
				} else { // melee
					npc.setNextAnimation(new Animation(defs.getAttackAnim()));
					delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.SLASH_ATTACK, target)));
					return defs.getAttackDelay();
				}
			}
			if (attackStyle == 0) { // mage
				npc.setNextAnimation(new Animation(defs.getAttackAnim()));
				ArrayList<Actor> possibleTargets = npc.getPossibleTargets(true, true);
				
				int d = getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.MAGIC_ATTACK, target);
				delayHit(npc, 1, target, getMagicHit(npc, d));
				if (d != 0) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if (target.getFreezeDelay() >= System.currentTimeMillis()) {
								target.setNextGraphics(new Graphics(1677, 0, 100));
							} else {
								target.setNextGraphics(new Graphics(369));
								target.addFreezeDelay(10000);
							}
						}
					}, 1);
					for (final Actor t : possibleTargets) {
						if (t != target && t.withinDistance(target, 2)) {
							int damage = getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.MAGIC_ATTACK, t);
							delayHit(npc, 1, t, getMagicHit(npc, damage));
							if (damage != 0) {
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										if (t.getFreezeDelay() >= System.currentTimeMillis()) {
											t.setNextGraphics(new Graphics(1677, 0, 100));
										} else {
											t.setNextGraphics(new Graphics(369));
											t.addFreezeDelay(10000);
										}
									}
								}, 1);
							}
							
						}
					}
				}
				if (Misc.getDistance(npc.getX(), npc.getY(), target.getX(), target.getY()) <= 1) { // lure
					// after
					// freeze
					npc.resetWalkSteps();
					npc.addWalkSteps(target.getX() + Misc.getRandom(2), target.getY() + Misc.getRandom(2));
				}
			}
		}
		return defs.getAttackDelay();
	}
}
