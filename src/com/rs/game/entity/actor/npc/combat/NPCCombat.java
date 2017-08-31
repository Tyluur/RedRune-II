package com.rs.game.entity.actor.npc.combat;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.ForceMovement;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.godwars.zaros.Nex;
import com.rs.game.content.Combat;
import com.rs.utility.game.map.MapAreas;
import com.rs.utility.Misc;

public final class NPCCombat {

	private NPC npc;
	private int combatDelay;
	private Actor target;

	public NPCCombat(NPC npc) {
		this.npc = npc;
	}

	public int getCombatDelay() {
		return combatDelay;
	}

	/*
	 * returns if under combat
	 */
	public boolean process() {
		if (combatDelay > 0)
			combatDelay--;
		if (target != null) {
			if (!checkAll()) {
				removeTarget();
				return false;
			}
			if (combatDelay <= 0)
				combatDelay = combatAttack();
			return true;
		}
		return false;
	}

	/*
	 * return combatDelay
	 */
	private int combatAttack() {
		Actor target = this.target; // prevents multithread issues
		if (target == null)
			return 0;
		// if hes frooze not gonna attack
		if (npc.getFreezeDelay() >= Misc.currentTimeMillis())
			return 0;
		// check if close to target, if not let it just walk and dont attack
		// this gameticket
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		int maxDistance = attackStyle == NPCCombatDefinitions.MELEE
				|| attackStyle == NPCCombatDefinitions.SPECIAL2 ? 0 : 7;
		if ((!(npc instanceof Nex))
				&& !npc.clipedProjectile(target, maxDistance == 0))
			return 0;
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		/*
		 * if(npc.hasWalkSteps()) maxDistance += 1;
		 */
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			return 0;
		}
		addAttackedByDelay(target);
		return CombatScriptsHandler.specialAttack(npc, target);
	}

	protected void doDefenceEmote(Actor target) {
		/*
		 * if (target.getNextAnimation() != null) // if has att emote already
		 * return;
		 */
		target.setNextAnimationNoPriority(new Animation(Combat
				.getDefenceEmote(target)));
	}

	public Actor getTarget() {
		return target;
	}

	public void addAttackedByDelay(Actor target) { // prevents multithread
													// issues
		target.setAttackedBy(npc);
		target.setAttackedByDelay(Misc.currentTimeMillis()
				+ npc.getCombatDefinitions().getAttackDelay() * 600 + 600); // 8seconds
	}

	public void setTarget(Actor target) {
		this.target = target;
		npc.setNextFaceActor(target);
		if (!checkAll()) {
			removeTarget();
			return;
		}
	}

	public boolean checkAll() {
		Actor target = this.target; // prevents multithread issues
		if (target == null)
			return false;
		if (npc.isDead() || npc.hasFinished() || npc.isForceWalking()
				|| target.isDead() || target.hasFinished())
			return false;
		if (npc.getFreezeDelay() >= Misc.currentTimeMillis())
			return true; // if freeze cant move ofc
		int distanceX = npc.getX() - npc.getRespawnTile().getX();
		int distanceY = npc.getY() - npc.getRespawnTile().getY();
		int size = npc.getSize();
		int maxDistance = 32;
		if (!npc.isCantFollowUnderCombat() && !(npc instanceof Familiar)) {

			if (npc.getMapAreaNameHash() != -1) {
				// if out his area
				if (!MapAreas.isAtArea(npc.getMapAreaNameHash(), npc)
						|| (!npc.canBeAttackFromOutOfArea() && !MapAreas
								.isAtArea(npc.getMapAreaNameHash(), target))) {
					npc.forceWalkRespawnTile();
					return false;
				}
			} else if (distanceX > size + maxDistance
					|| distanceX < -1 - maxDistance
					|| distanceY > size + maxDistance
					|| distanceY < -1 - maxDistance) {
				// if more than 64 distance from respawn place
				npc.forceWalkRespawnTile();
				return false;
			}
		}
		maxDistance = 16;
		distanceX = target.getX() - npc.getX();
		distanceY = target.getY() - npc.getY();
		if (npc.getPlane() != target.getPlane()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false; // if target distance higher 16
		// checks for no multi area :)
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if (!familiar.canAttack(target))
				return false;
		} else {
			if (!npc.isForceMultiAttacked()) {
				if (!target.isAtMultiArea() || !npc.isAtMultiArea()) {
					if (npc.getAttackedBy() != target
							&& npc.getAttackedByDelay() > System
									.currentTimeMillis())
						return false;
					if (target.getAttackedBy() != npc
							&& target.getAttackedByDelay() > System
									.currentTimeMillis())
						return false;
				}
			}
		}
		if (!npc.isCantFollowUnderCombat()) {
			/*
			 * if (npc.getX() == target.getX() && npc.getY() == target.getY() &&
			 * !target.hasWalkSteps()) { npc.resetWalkSteps(); if
			 * (!npc.addWalkSteps(npc.getX() + size, npc.getY(), size)) { if
			 * (!npc.addWalkSteps(npc.getX() - size, npc.getY(), size)) { if
			 * (!npc.addWalkSteps(npc.getX(), npc.getY() + size, size)) { if
			 * (!npc.addWalkSteps(npc.getX(), npc.getY() - size, size)) return
			 * false; } } } return true; } addXp
			 */
			// if is under
			if (distanceX < size && distanceX > -1 && distanceY < size
					&& distanceY > -1) {
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getX() + 1, npc.getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getX() - size - 1, npc.getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(target.getX(), npc.getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(target.getX(), npc.getY()
									- size - 1)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			if (npc instanceof Nex) {
				Nex nex = (Nex) npc;
				maxDistance = nex.isFollowTarget() ? 0 : 7;
				if (nex.getFlyTime() == 0
						&& (distanceX > size + maxDistance
								|| distanceX < -1 - maxDistance
								|| distanceY > size + maxDistance || distanceY < -1
								- maxDistance)) {
					npc.resetWalkSteps();
					npc.addWalkStepsInteract(target.getX(), target.getY(), 2,
							size, true);
					if (!npc.hasWalkSteps()) {
						int[][] dirs = Misc.getCoordOffsetsNear(size);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final WorldTile tile = new WorldTile(new WorldTile(
									target.getX() + dirs[0][dir], target.getY()
											+ dirs[1][dir], target.getPlane()));
							if (World.canMoveNPC(tile.getPlane(), tile.getX(),
									tile.getY(), size)) { // if found done
								nex.setFlyTime(4);
								npc.setNextForceMovement(new ForceMovement(
										new WorldTile(npc), 0, tile, 1, Misc
												.getMoveDirection(
														tile.getX()
																- npc.getX(),
														tile.getY()
																- npc.getY())));
								npc.setNextAnimation(new Animation(6985));
								npc.setNextWorldTile(tile);
								return true;
							}
						}
					}
					return true;
				} else
					// if doesnt need to move more stop moving
					npc.resetWalkSteps();
			} else {
				maxDistance = npc.isForceFollowClose() ? 0
						: (attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2) ? 0
								: 7;
				// is far from target, moves to it till can attack
				if ((!npc.clipedProjectile(target, maxDistance == 0))
						|| distanceX > size + maxDistance
						|| distanceX < -1 - maxDistance
						|| distanceY > size + maxDistance
						|| distanceY < -1 - maxDistance) {
					npc.resetWalkSteps();
					npc.addWalkStepsInteract(target.getX(), target.getY(), 2,
							size, true);
					return true;
				} else
					// if doesnt need to move more stop moving
					npc.resetWalkSteps();
				// if under target, moves

			}
		}
		return true;

	}

	public void addCombatDelay(int delay) {
		combatDelay += delay;
	}

	public void setCombatDelay(int delay) {
		combatDelay = delay;
	}

	public boolean underCombat() {
		return target != null;
	}

	public void removeTarget() {
		this.target = null;
		npc.setNextFaceActor(null);
	}

	public void reset() {
		combatDelay = 0;
		target = null;
	}

}
