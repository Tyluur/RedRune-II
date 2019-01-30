package org.redrune.game.entity.actor.npc.impl.kalph;

import org.redrune.engine.SystemManager;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class KalphiteQueen extends NPC {
	
	public KalphiteQueen(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}
	
	@Override
	public ArrayList<Actor> getPossibleTargets() {
		ArrayList<Actor> possibleTarget = new ArrayList<Actor>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.isFinished() || !player.isRunning() || !player.withinDistance(this, 64) || ((!isInMultiArea() || !player.isInMultiArea()) && player.getAttackedBy() != this && player.getAttackedByDelay() > Misc.currentTimeMillis()) || !clipedProjectile(player, false)) {
						continue;
					}
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}
	
	@Override
	public void sendDeath(final Actor source) {
		// TODO Finish up first & second death
		final NPC n = this;
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int step;
			
			NPC kq;
			
			@Override
			public void run() {
				if (getId() == 1158) {
					if (step == 10) {
						stop();
					}
					if (step == 0) {
						setNextAnimation(new Animation(defs.getDeathAnim()));
					}
					if (step == 2) {
						finish();
						kq = new NPC(1160, n, 0, true, true);
						kq.setNextGraphics(new Graphics(1055));
					}
					if (step == 8) {
					}
				} else if (getId() == 1160) {
					if (step == 4) {
						finish();
						stop();
					}
					if (step == 0) {
						setNextAnimation(new Animation(defs.getDeathAnim()));
						SystemManager.SLOW_EXECUTOR.schedule(() -> {
							try {
								spawn();
							} catch (Exception | Error e) {
								e.printStackTrace();
							}
						}, getCombatDefinitions().getRespawnDelay() * 600, TimeUnit.MILLISECONDS);
					}
				}
				step++;
			}
		}, 0, 1);
	}
	
	public static boolean atKQ(WorldTile tile) {
		return (tile.getX() >= 3462 && tile.getX() <= 3510) && (tile.getY() >= 9462 && tile.getY() <= 9528);
	}
	
	public void respawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		RegionManager.updateActorRegion(this);
		loadMapRegions();
		checkMultiArea();
	}
	
}
