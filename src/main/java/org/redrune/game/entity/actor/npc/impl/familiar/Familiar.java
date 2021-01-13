package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;

/**
 * Modified by:
 *
 * @author Gircat <gircat101@gmail.com> Created on Aug 6, 2014 at 9:12:00 PM.
 */
public abstract class Familiar extends NPC implements Serializable {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3255206534594320406L;
	
	public abstract boolean submitSpecial(Object object);
	
	private int ticks;
	
	private int trackTimer;
	
	private int specialEnergy;
	
	private boolean trackDrain;
	
	private BeastOfBurden bob;
	
	private Pouches pouch;
	
	private transient Player owner;
	
	private transient int[][] checkNearDirs;
	
	private transient boolean sentRequestMoveMessage;
	
	private transient boolean dead;
	
	public Familiar(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(pouch.getNpcId(), tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
		setRunModeOn(true);
		this.owner = owner;
		this.pouch = pouch;
		resetTickets();
		specialEnergy = 60;
		if (getBOBSize() > 0) {
			bob = new BeastOfBurden(getBOBSize());
		}
		call(true);
	}
	
	public void resetTickets() {
		ticks = (int) (pouch.getTime() / 1000 / 30);
		trackTimer = 0;
	}
	
	public abstract int getBOBSize();
	
	public void call(boolean login) {
		int size = getSize();
		if (login) {
			if (bob != null) {
				bob.setEntitys(owner, this);
			}
			checkNearDirs = Misc.getCoordOffsetsNear(size);
			sendMainConfigs();
		} else {
			removeTarget();
		}
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getPlane()));
			if (RegionManager.canMoveNPC(tile.getPlane(), tile.getX(), tile.getY(), size)) { // if found done
				teleTile = tile;
				break;
			}
		}
		if (login || teleTile != null) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextGraphics(new Graphics(getDefinitions().getSize() > 1 ? 1315 : 1314));
				}
			});
		}
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				owner.getPackets().sendMessage("Theres not enough space for your familiar appear.");
				sentRequestMoveMessage = true;
			}
			return;
		}
		sentRequestMoveMessage = false;
		setNextWorldTile(teleTile);
	}
	
	public void sendMainConfigs() {
		switchOrb(true);
		owner.getPackets().sendConfig(448, pouch.getPouchId());
		owner.getPackets().sendConfig(1160, 243269632);
		refreshSpecialEnergy();
		sendTimeRemaining();
		owner.getPackets().sendConfig(1175, getSpecialAmount() << 23);
		owner.getPackets().sendGlobalString(204, getSpecialName());
		owner.getPackets().sendGlobalString(205, getSpecialDescription());
		owner.getPackets().sendGlobalConfig(1436, getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
		unlockOrb(); // temporary
	}
	
	public void switchOrb(boolean on) {
		owner.getPackets().sendConfig(1174, on ? -1 : 0);
		if (on) {
			unlock();
		} else {
			lockOrb();
		}
	}
	
	public void refreshSpecialEnergy() {
		owner.getPackets().sendConfig(1177, specialEnergy);
	}
	
	public void sendTimeRemaining() {
		owner.getPackets().sendConfig(1176, ticks * 65);
	}
	
	public abstract int getSpecialAmount();
	
	public abstract String getSpecialName();
	
	public abstract String getSpecialDescription();
	
	public abstract SpecialAttack getSpecialAttack();
	
	public void unlockOrb() {
		owner.getPackets().sendHideIComponent(747, 8, false);
		sendLeftClickOption(owner);
	}
	
	public void unlock() {
		switch (getSpecialAttack()) {
			case CLICK:
				owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 2);
				owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 2);
				break;
			case ENTITY:
				owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 20480);
				owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 20480);
				break;
			case OBJECT:
			case ITEM:
				owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 65536);
				owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 65536);
				break;
		}
		owner.getPackets().sendHideIComponent(747, 8, false);
	}
	
	public void lockOrb() {
		owner.getPackets().sendHideIComponent(747, 8, true);
	}
	
	public static void sendLeftClickOption(Player player) {
		player.getPackets().sendConfig(1493, player.getAttributes().getSummoningLeftClickOption());
		player.getPackets().sendConfig(1494, player.getAttributes().getSummoningLeftClickOption());
	}
	
	@Override
	public void processNPC() {
		if (isDead()) {
			return;
		}
		unlockOrb();
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (trackDrain) {
				owner.getSkills().drainSummoning(1);
			}
			trackDrain = !trackDrain;
			if (ticks == 2) {
				owner.getPackets().sendMessage("You have 1 minute before your familiar vanishes.");
			} else if (ticks == 1) {
				owner.getPackets().sendMessage("You have 30 seconds before your familiar vanishes.");
			} else if (ticks == 0) {
				dissmissFamiliar(false);
				return;
			}
			sendTimeRemaining();
		}
		if (owner.getAttributes().isCanPvp() && getId() != pouch.getNpcId()) {
			transformIntoNPC(pouch.getNpcId());
			call(false);
			return;
		} else if (!owner.getAttributes().isCanPvp() && getId() == pouch.getNpcId()) {
			transformIntoNPC(pouch.getNpcId() - 1);
			call(false);
			return;
		} else if (!withinDistance(owner, 12)) {
			call(false);
			return;
		}
		if (!getCombat().process()) {
			if (isAgressive() && owner.getAttackedBy() != null && owner.getAttackedByDelay() > Misc.currentTimeMillis() && canAttack(owner.getAttackedBy()) && Misc.getRandom(25) == 0) {
				getCombat().setTarget(owner.getAttackedBy());
			} else {
				sendFollow();
			}
		}
	}
	
	@Override
	public void sendDeath(Actor source) {
		if (dead) {
			return;
		}
		dead = true;
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		setCantInteract(true);
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathAnim()));
					owner.getPackets().sendMessage("Your familiar slowly begins to fade away..");
				} else if (loop >= defs.getDeathDelay()) {
					dissmissFamiliar(false);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	public void dissmissFamiliar(boolean logged) {
		if (!logged) {
			owner.setFamiliar(null);
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			// if (bob != null)
			// bob.dropBob(); // can cause a dup method?
		}
		finish();
	}
	
	public static void selectLeftOption(Player player) {
		boolean res = player.getInterfaceManager().hasRezizableScreen();
		player.getPackets().sendInterface(true, res ? 746 : 548, res ? 98 : 212, 880);
		sendLeftClickOption(player);
		player.getPackets().sendGlobalConfig(168, 8);// tab id
	}
	
	public static void confirmLeftOption(Player player) {
		player.getPackets().sendGlobalConfig(168, 4);// inv tab id
		boolean res = player.getInterfaceManager().hasRezizableScreen();
		player.getPackets().closeInterface(res ? 98 : 212);
	}
	
	public static void setLeftclickOption(Player player, int summoningLeftClickOption) {
		if (summoningLeftClickOption == player.getAttributes().getSummoningLeftClickOption()) {
			return;
		}
		player.getAttributes().setSummoningLeftClickOption(summoningLeftClickOption);
		sendLeftClickOption(player);
	}
	
	public void store() {
		if (bob == null) {
			return;
		}
		bob.open();
	}
	
	public boolean canStoreEssOnly() {
		return pouch.getNpcId() == 6818;
	}
	
	public int getOriginalId() {
		return pouch.getNpcId();
	}
	
	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex()) {
			setNextFaceActor(owner);
		}
		if (getFreezeDelay() >= Misc.currentTimeMillis()) {
			return; // if freeze cant move ofc
		}
		int size = getSize();
		
		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		// if is under
		if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size - 1, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(owner.getX(), getY() + 1)) {
						resetWalkSteps();
						if (!addWalkSteps(owner.getX(), getY() - size - 1)) {
							return;
						}
					}
				}
			}
			return;
		}
		if ((!clipedProjectile(owner, true)) || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), 2, size, true);
			return;
		} else {
			resetWalkSteps();
		}
		
	}
	
	public boolean canAttack(Actor target) {
		if (target instanceof Player) {
			Player player = (Player) target;
			if (!owner.getAttributes().isCanPvp() || !player.getAttributes().isCanPvp()) {
				return false;
			}
		}
		return !target.isDead() && owner.isInMultiArea() && isInMultiArea() && target.isInMultiArea() && owner.getControllerManager().canAttack(target);
	}
	
	public boolean renewFamiliar() {
		if (ticks > 5) {
			owner.getPackets().sendMessage("You need to have at least 2 minutes 50 seconds remaining before you can renew your familiar.", true);
			return false;
		} else if (!owner.getInventory().getItems().contains(new Item(pouch.getPouchId(), 1))) {
			owner.getPackets().sendMessage("You need a " + ItemDefinitions.getItemDefinitions(pouch.getPouchId()).getName().toLowerCase() + " to renew your familiar's timer.");
			return false;
		}
		resetTickets();
		owner.getInventory().deleteItem(pouch.getPouchId(), 1);
		call(true);
		owner.getPackets().sendMessage("You use your remaining pouch to renew your familiar.");
		return true;
	}
	
	public void takeBob() {
		if (bob == null) {
			return;
		}
		bob.takeBob();
	}
	
	public void sendFollowerDetails() {
		boolean res = owner.getInterfaceManager().hasRezizableScreen();
		owner.getPackets().sendInterface(true, res ? 746 : 548, res ? 98 : 212, 662);
		owner.getPackets().sendHideIComponent(662, 44, true);
		owner.getPackets().sendHideIComponent(662, 45, true);
		owner.getPackets().sendHideIComponent(662, 46, true);
		owner.getPackets().sendHideIComponent(662, 47, true);
		owner.getPackets().sendHideIComponent(662, 48, true);
		owner.getPackets().sendHideIComponent(662, 71, false);
		owner.getPackets().sendHideIComponent(662, 72, false);
		unlock();
		owner.getPackets().sendGlobalConfig(168, 8);// tab id
	}
	
	public void call() {
		if (getAttackedBy() != null && getAttackedByDelay() > Misc.currentTimeMillis()) {
			owner.getPackets().sendMessage("You cant call your familiar while it under combat.");
			return;
		}
		call(false);
	}
	
	public void respawnFamiliar(Player owner) {
		this.owner = owner;
		initEntity();
		deserialize();
		call(true);
	}
	
	public boolean isAgressive() {
		return true;
	}
	
	public BeastOfBurden getBob() {
		return bob;
	}
	
	public void restoreSpecialAttack(int energy) {
		if (specialEnergy >= 60) {
			return;
		}
		specialEnergy = energy + specialEnergy >= 60 ? 60 : specialEnergy + energy;
		refreshSpecialEnergy();
	}
	
	public void setSpecial(boolean on) {
		if (!on) {
			owner.getTemporaryAttributes().remove("FamiliarSpec");
		} else {
			if (specialEnergy < getSpecialAmount()) {
				owner.getPackets().sendMessage("You familiar doesn't have enough special energy.");
				return;
			}
			owner.getTemporaryAttributes().put("FamiliarSpec", Boolean.TRUE);
		}
	}
	
	public void drainSpecial(int specialReduction) {
		specialEnergy -= specialReduction;
		if (specialEnergy < 0) {
			specialEnergy = 0;
		}
		refreshSpecialEnergy();
	}
	
	public boolean hasSpecialOn() {
		if (owner.getTemporaryAttributes().remove("FamiliarSpec") != null) {
			if (!owner.getInventory().containsItem(pouch.getScrollId(), 1)) {
				owner.getPackets().sendMessage("You don't have the scrolls to use this move.");
				return false;
			}
			owner.getInventory().deleteItem(pouch.getScrollId(), 1);
			drainSpecial();
			return true;
		}
		return false;
	}
	
	public void drainSpecial() {
		specialEnergy -= getSpecialAmount();
		refreshSpecialEnergy();
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public enum SpecialAttack {
		ITEM,
		ENTITY,
		CLICK,
		OBJECT
	}
}
