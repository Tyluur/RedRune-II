package game.entity.actor.npc.impl.kalph;

import engine.SystemManager;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.player.Player;
import game.global.World;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.functions.Misc;

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
	public ArrayList<Actor> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
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
