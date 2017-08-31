package com.rs.game.entity.actor.npc.combat.impl;

import java.util.ArrayList;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.corp.CorporealBeast;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

public class CorporealBeastCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 8133 };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Misc.getRandom(40) == 0) {
			CorporealBeast beast = (CorporealBeast) npc;
			beast.spawnDarkEnergyCore();
		}
		int size = npc.getSize();
		final ArrayList<Actor> possibleTargets = npc.getPossibleTargets();
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
			npc.setNextAnimation(new Animation(10496));
			npc.setNextGraphics(new Graphics(1834));
			return defs.getAttackDelay();
		}
		int attackStyle = Misc.getRandom(4);
		if (attackStyle == 0 || attackStyle == 1) { // melee
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size
					|| distanceY < -1)
				attackStyle = 2 + Misc.getRandom(2); // set mage
			else {
				npc.setNextAnimation(new Animation(attackStyle == 0 ? defs
						.getAttackEmote() : 10058));
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
		if (attackStyle == 2) { // powerfull mage spiky ball
			npc.setNextAnimation(new Animation(10410));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 650,
									NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 1825, 41, 16, 41, 0, 16, 0);
		} else if (attackStyle == 3) { // translucent ball of energy
			npc.setNextAnimation(new Animation(10410));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 550,
									NPCCombatDefinitions.MAGE, target)));
			if (target instanceof Player) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						int skill = Misc.getRandom(2);
						skill = skill == 0 ? Skills.MAGIC
								: (skill == 1 ? Skills.SUMMONING
										: Skills.PRAYER);
						Player player = (Player) target;
						if (skill == Skills.PRAYER)
							player.getPrayer().drainPrayer(
									10 + Misc.getRandom(40));
						else {
							int lvl = player.getSkills().getLevel(skill);
							lvl -= 1 + Misc.getRandom(4);
							player.getSkills().set(skill, lvl < 0 ? 0 : lvl);
						}
						player.getPackets().sendGameMessage(
								"Your " + Skills.SKILL_NAME[skill]
										+ " has been slighly drained!");
					}

				}, 1);
				World.sendProjectile(npc, target, 1823, 41, 16, 41, 0, 16, 0);
			}
		} else if (attackStyle == 4) {
			npc.setNextAnimation(new Animation(10410));
			final WorldTile tile = new WorldTile(target);
			World.sendProjectile(npc, tile, 1824, 41, 16, 30, 0, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 6; i++) {
						final WorldTile newTile = new WorldTile(tile, 3);
						if (!World.canMoveNPC(newTile.getPlane(),
								newTile.getX(), newTile.getY(), 1))
							continue;
						World.sendProjectile(npc, tile, newTile, 1824, 0, 0,
								25, 0, 30, 0);
						for (Actor t : possibleTargets) {
							if (Misc.getDistance(newTile.getX(),
									newTile.getY(), t.getX(), t.getY()) > 1
									|| !t.clipedProjectile(newTile, false))
								continue;
							delayHit(
									npc,
									0,
									t,
									getMagicHit(
											npc,
											getRandomMaxHit(npc, 350,
													NPCCombatDefinitions.MAGE,
													t)));
						}
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								World.sendGraphics(npc, new Graphics(1806),
										newTile);
							}

						});
					}
				}
			}, 1);
		}
		return defs.getAttackDelay();
	}
}
