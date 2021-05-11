/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.actor.npc.impl.lrc;

import engine.SystemManager;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.player.Player;
import game.global.World;
import game.global.WorldTile;
import game.global.map.region.RegionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Owner
 */
public class LivingRockStriker extends NPC {

	public LivingRockStriker(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Actor> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		ArrayList<Actor> possibleTarget = new ArrayList<Actor>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.isFinished() || !player.isRunning() || !player.withinDistance(this, 64) || ((!isInMultiArea() || !player.isInMultiArea()) && player.getAttackedBy() != this && player.getAttackedByDelay() > System.currentTimeMillis()) || !clipedProjectile(player, false)) {
						continue;
					}
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	/*
	 * gotta override else setRespawnTask override doesnt work
	 */
	@Override
	public void sendDeath(Actor source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {

			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathAnim()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			setLocation(getRespawnTile());
			finish();
		}
		final NPC npc = this;
		SystemManager.SLOW_EXECUTOR.schedule(() -> {
			setFinished(false);
			World.addNPC(npc);
			npc.setLastRegionId(0);
			RegionManager.updateActorRegion(npc);
			loadMapRegions();
			checkMultiArea();
		}, getCombatDefinitions().getRespawnDelay() * 600, TimeUnit.MILLISECONDS);
	}
}
