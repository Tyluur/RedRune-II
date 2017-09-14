package com.rs.game.entity.actor.npc.impl.others;

import com.rs.cores.CoresManager;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import com.rs.game.world.World;
import com.rs.game.world.region.RegionManager;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public final class Nomad extends NPC {
	
	// private boolean[] demonPrayer;
	private int fixedCombatType;
	
	private int[] cachedDamage;
	
	// private int shieldTimer;
	private int fixedAmount;
	
	// private int prayerTimer;
	
	public Nomad(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		// demonPrayer = new boolean[3];
		cachedDamage = new int[3];
		// shieldTimer = 0;
		// switchPrayers(0);
	}
	
	// public void switchPrayers(int type) {
	// transformIntoNPC(8133);
	// demonPrayer[type] = false;
	// resetPrayerTimer();
	// }
	
	// private void resetPrayerTimer() {
	// prayerTimer = 16;
	// }
	
	// @Override
	// public void processNPC() {
	// super.processNPC();
	// if (isDead())
	// return;
	// if (Utils.getRandom(40) <= 2)
	// sendRandomProjectile();
	// if (getCombat().process()) {// no point in processing
	// if (shieldTimer > 0)
	// shieldTimer--;
	// if (prayerTimer > 0)
	// prayerTimer--;
	// if (fixedAmount >= 5)
	// fixedAmount = 0;
	// if (prayerTimer == 0) {
	// for (int i = 0; i < cachedDamage.length; i++) {
	// if (cachedDamage[i] >= 310) {
	// demonPrayer = new boolean[3];
	// switchPrayers(i);
	// cachedDamage = new int[3];
	// }
	// }
	// }
	// for (int i = 0; i < cachedDamage.length; i++) {
	// if (cachedDamage[i] >= 310) {
	// demonPrayer = new boolean[3];
	// switchPrayers(i);
	// cachedDamage = new int[3];
	// }
	// }
	// }
	// }
	
	// @Override
	// public void handleIngoingHit(final Hit hit) {
	// int type = 0;
	// super.handleIngoingHit(hit);
	// if (hit.getSource() instanceof Player) {// darklight
	// Player player = (Player) hit.getSource();
	// if ((player.getEquipment().getWeaponId() == 6746 || player
	// .getEquipment().getWeaponId() == 2402)
	// && hit.getSplat() == HitLook.MELEE_DAMAGE
	// && hit.getDamage() > 0) {
	// shieldTimer = 60;
	// player.getPackets().sendGameMessage(
	// "The Corporeal Beast is temporarily weakend by your weapon.");
	// }
	// }
	// if (shieldTimer <= 0) {// 75% of damage is absorbed
	// hit.setDamage((int) (hit.getDamage() * 0.25));
	// setNextGraphics(new Graphics(1885));
	// }
	// if (hit.getSplat() == HitLook.MELEE_DAMAGE) {
	// if (demonPrayer[0]) {
	// hit.setDamage(0);
	// } else {
	// cachedDamage[0] += hit.getDamage();
	// }
	// } else if (hit.getSplat() == HitLook.MELEE_DAMAGE) {
	// type = 1;
	// if (demonPrayer[1]) {
	// hit.setDamage(0);
	// } else {
	// cachedDamage[1] += hit.getDamage();
	// }
	// } else if (hit.getSplat() == HitLook.RANGE_DAMAGE) {
	// type = 2;
	// if (demonPrayer[2]) {
	// hit.setDamage(0);
	// } else {
	// cachedDamage[2] += hit.getDamage();
	// }
	// } else if (hit.getSplat() == HitLook.MISSED) {
	// cachedDamage[type] += 20;
	// } else {
	// cachedDamage[Utils.getRandom(2)] += 20;// random
	// }
	// }
	
	@Override
	public void sendDeath(Actor source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		// shieldTimer = 0;
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
	
	// private void sendRandomProjectile() {
	// WorldTile tile = new WorldTile(getX() + Utils.random(7), getY()
	// + Utils.random(7), getPlane());
	// setNextAnimation(new Animation(10918));
	// World.sendProjectile(this, tile, 1887, 34, 16, 40, 35, 16, 0);
	// for (int regionId : getMapRegionsIds()) {
	// List<Integer> playerIndexes = World.getRegion(regionId)
	// .getPlayerIndexes();
	// if (playerIndexes != null) {
	// for (int npcIndex : playerIndexes) {
	// Player player = World.getPlayers().get(npcIndex);
	// if (player == null || player.isDead()
	// || player.hasFinished() || !player.isRunning()
	// || !player.withinDistance(tile, 3))
	// continue;
	// player.getPackets().sendGameMessage(
	// "The Corporeal Beast's magical attack splashes on you.");
	// player.applyHit(new Hit(this, 281, HitLook.MAGIC_DAMAGE, 1));
	// }
	// }
	// }
	// }
	
	@Override
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(getRespawnTile());
			finish();
		}
		final NPC npc = this;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				setFinished(false);
				World.addNPC(npc);
				npc.setLastRegionId(0);
				RegionManager.updateActorRegion(npc);
				loadMapRegions();
				checkMultiArea();
				// shieldTimer = 0;
				fixedCombatType = 0;
				fixedAmount = 0;
			}
		}, getCombatDefinitions().getRespawnDelay() * 400, TimeUnit.MILLISECONDS);
	}
	
	public static boolean atTD(WorldTile tile) {
		return (tile.getX() >= 2560 && tile.getX() <= 2630) && (tile.getY() >= 5710 && tile.getY() <= 5753);
	}
	
	public int getFixedCombatType() {
		return fixedCombatType;
	}
	
	public void setFixedCombatType(int fixedCombatType) {
		this.fixedCombatType = fixedCombatType;
	}
	
	public int getFixedAmount() {
		return fixedAmount;
	}
	
	public void setFixedAmount(int fixedAmount) {
		this.fixedAmount = fixedAmount;
	}
	
}