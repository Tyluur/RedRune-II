package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.corp.CorporealBeast;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.SkillConstants;

import java.util.ArrayList;

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
		final ArrayList<Actor> possibleTargets = npc.getPossibleTargets(true, true);
		boolean stomp = false;
		for (Actor t : possibleTargets) {
			int distanceX = t.getX() - npc.getX();
			int distanceY = t.getY() - npc.getY();
			if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1) {
				stomp = true;
				delayHit(npc, 0, t, getRegularHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.SLASH_ATTACK, t)));
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
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
				attackStyle = 2 + Misc.getRandom(2); // set mage
			} else {
				npc.setNextAnimation(new Animation(attackStyle == 0 ? defs.getAttackAnim() : 10058));
				delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.SLASH_ATTACK, target)));
				return defs.getAttackDelay();
			}
		}
		if (attackStyle == 2) { // powerfull mage spiky ball
			npc.setNextAnimation(new Animation(10410));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 650, BonusConstants.MAGIC_ATTACK, target)));
			RegionManager.sendProjectile(npc, target, 1825, 41, 16, 41, 0, 16, 0);
		} else if (attackStyle == 3) { // translucent ball of energy
			npc.setNextAnimation(new Animation(10410));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 550, BonusConstants.MAGIC_ATTACK, target)));
			if (target instanceof Player) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						int skill = Misc.getRandom(2);
						skill = skill == 0 ? SkillConstants.MAGIC : (skill == 1 ? SkillConstants.SUMMONING : SkillConstants.PRAYER);
						Player player = (Player) target;
						if (skill == SkillConstants.PRAYER) {
							player.getPrayer().drainPrayer(10 + Misc.getRandom(40));
						} else {
							int lvl = player.getSkills().getLevel(skill);
							lvl -= 1 + Misc.getRandom(4);
							player.getSkills().set(skill, lvl < 0 ? 0 : lvl);
						}
						player.getPackets().sendMessage("Your " + SkillConstants.SKILL_NAME[skill] + " has been slighly drained!");
					}

				}, 1);
				RegionManager.sendProjectile(npc, target, 1823, 41, 16, 41, 0, 16, 0);
			}
		} else if (attackStyle == 4) {
			npc.setNextAnimation(new Animation(10410));
			final WorldTile tile = new WorldTile(target);
			RegionManager.sendProjectile(npc, tile, 1824, 41, 16, 30, 0, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 6; i++) {
						final WorldTile newTile = new WorldTile(tile, 3);
						if (!RegionManager.canMoveNPC(newTile.getPlane(), newTile.getX(), newTile.getY(), 1)) {
							continue;
						}
						RegionManager.sendProjectile(npc, tile, newTile, 1824, 0, 0, 25, 0, 30, 0);
						for (Actor t : possibleTargets) {
							if (Misc.getDistance(newTile.getX(), newTile.getY(), t.getX(), t.getY()) > 1 || !t.clipedProjectile(newTile, false)) {
								continue;
							}
							delayHit(npc, 0, t, getMagicHit(npc, getRandomMaxHit(npc, 350, BonusConstants.MAGIC_ATTACK, t)));
						}
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								RegionManager.sendGraphics(npc, new Graphics(1806), newTile);
							}

						});
					}
				}
			}, 1);
		}
		return defs.getAttackDelay();
	}
}
