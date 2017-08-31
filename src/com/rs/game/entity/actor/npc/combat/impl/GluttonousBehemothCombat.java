package com.rs.game.entity.actor.npc.combat.impl;

import java.util.ArrayList;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.world.World;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

public class GluttonousBehemothCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Gluttonous behemoth" };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		ArrayList<Actor> possibleTargets = npc.getPossibleTargets();
		boolean stomp = false;
		for (Actor t : possibleTargets) {
			int distanceX = t.getX() - npc.getX();
			int distanceY = t.getY() - npc.getY();
			if (distanceX < size && distanceX > -1 && distanceY < size
					&& distanceY > -1) {
				stomp = true;
				delayHit(
						npc,
						0,
						t,
						getRegularHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, t)));
			}
		}
		if (stomp) {
			npc.setNextAnimation(new Animation(13718));
			return defs.getAttackDelay();
		}
		int attackStyle = Misc.getRandom(2);
		if (attackStyle == 2) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (!(distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1))
				attackStyle = Misc.getRandom(1);
			else {
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(
						npc,
						0,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, target)));

				return defs.getAttackDelay();
			}
		}
		if (attackStyle == 0) {
			npc.setNextAnimation(new Animation(13719));
			World.sendProjectile(npc, target, 2612, 41, 16, 41, 35, 16, 0);
			int damage = getRandomMaxHit(npc, defs.getMaxHit(),
					NPCCombatDefinitions.MAGE, target);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (damage != 0) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextGraphics(new Graphics(2613));
					}
				}, 1);
			}
		} else if (attackStyle == 1) {
			npc.setNextAnimation(new Animation(13722));
			World.sendProjectile(npc, target, 2611, 41, 16, 41, 35, 16, 0);
			delayHit(
					npc,
					2,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.RANGE, target)));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					target.setNextGraphics(new Graphics(2611));
				}
			}, 1);
		}
		return defs.getAttackDelay();
	}
}
