package com.rs.game.entity.actor.npc.impl.kalph;

import com.rs.cores.CoresManager;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

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
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || !player.withinDistance(this, 64) || ((!isAtMultiArea() || !player.isAtMultiArea()) && player.getAttackedBy() != this && player.getAttackedByDelay() > Misc.currentTimeMillis()) || !clipedProjectile(player, false)) {
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
						CoresManager.slowExecutor.schedule(() -> {
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
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}
	
}
