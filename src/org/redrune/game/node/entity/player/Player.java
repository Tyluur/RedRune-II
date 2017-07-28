package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.SequencialUpdate;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.GameConstants;
import org.redrune.game.content.action.interaction.PlayerCombatAction;
import org.redrune.game.content.activity.impl.WildernessActivity;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.node.NodeInteractionTask;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.PlayerCombatDefinitions;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.render.NPCRendering;
import org.redrune.game.node.entity.player.data.*;
import org.redrune.game.node.entity.player.render.PlayerRendering;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.lobby.packet.outgoing.LobbyResponseBuilder;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.master.client.packet.out.PlayerFilePacketOut;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.world.Transmitter;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.outgoing.impl.*;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The player that renderable in the game.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Player extends Entity {
	
	/**
	 * The credentials of the player
	 */
	@Getter
	private final PlayerDetails details;
	
	/**
	 * The skills of the player
	 */
	@Getter
	private final PlayerSkills skills;
	
	/**
	 * The inventory of the player
	 */
	@Getter
	private final PlayerInventory inventory;
	
	/**
	 * The equipment of the player
	 */
	@Getter
	private final PlayerEquipment equipment;
	
	/**
	 * The bank of the player
	 */
	@Getter
	private final PlayerBank bank;
	
	/**
	 * The manager instance
	 */
	@Getter
	private final PlayerManager manager;
	
	/**
	 * The variables of the player that are saved
	 */
	@Getter
	private final PlayerVariables variables;
	
	/**
	 * The combat definitions of the entity. These are saved
	 */
	@Getter
	private final PlayerCombatDefinitions combatDefinitions = new PlayerCombatDefinitions();
	
	/**
	 * The networkSession attached to the player
	 */
	@Getter
	@Setter
	private transient WorldSession session;
	
	/**
	 * The network transmitter object
	 */
	@Getter
	private transient Transmitter transmitter;
	
	/**
	 * The render information object
	 */
	@Getter
	private transient PlayerRenderData renderData;
	
	/**
	 * The path event
	 */
	@Getter
	@Setter
	private transient NodeInteractionTask interactionTask;
	
	public Player(String username) {
		super(GameConstants.HOME_LOCATION);
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username);
		this.equipment = new PlayerEquipment();
		this.inventory = new PlayerInventory();
		this.variables = new PlayerVariables();
		this.manager = new PlayerManager();
		this.bank = new PlayerBank();
	}
	
	@Override
	public void register() {
		registerTransients();
		
		World.get().getPlayers().add(this);
		
		transmitter.sendLoginComponents();
		equipment.sendContainer();
		inventory.initialize();
		skills.refreshAll();
		manager.getNotes().sendLoginConfiguration();
		
		// renderable must be after this because of map region building...
		setRenderable(true);
		
		getUpdateMasks().register(new AppearanceUpdate(this));
		SequencialUpdate.getRenderablePlayers().add(this);
		RegionManager.updateEntityRegion(this);
		checkMultiArea();
		
		session.write(new PlayerOptionPacketBuilder("Follow", false, 2).build(this));
		session.write(new PlayerOptionPacketBuilder("Trade with", false, 3).build(this));
		
		System.out.println("Player registered to game:\t" + this);
	}
	
	@Override
	public void deregister() {
		setRenderable(false);
		session.notifyDisconnection(getWorld());
		
		World.get().removePlayer(this);
		RegionManager.updateEntityRegion(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		
		System.out.println("Player de-registered from game:\t" + this);
	}
	
	@Override
	public Player toPlayer() {
		return this;
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public int getMaxHealth() {
		return skills.getLevelForXp(SkillConstants.HITPOINTS) * 10;
	}
	
	@Override
	public void tick() {
		if (isDead() || isDying()) {
			return;
		}
		super.tick();
		checkInteractionTask();
		getRegion().increaseTimeSpent(this);
		manager.getActions().process();
		manager.getPrayers().process();
		manager.getHintIcons().process();
		manager.getActivities().process();
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + "]";
	}
	
	/**
	 * Generates the transient objects (objects which will not save)
	 */
	@Override
	public void registerTransients() {
		super.registerTransients();
		
		this.manager.registerTransients(this);
		this.transmitter = new Transmitter(this);
		this.renderData = new PlayerRenderData(this);
		
		// actual player objects
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
		this.inventory.setPlayer(this);
		this.bank.setPlayer(this);
		this.combatDefinitions.setPlayer(this);
	}
	
	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		if (isAtDynamicRegion()) {
			transmitter.send(new DynamicMapRegionBuilder().build(this));
			if (!wasAtDynamicRegion) {
				getRenderData().getLocalNpcs().clear();
			}
		} else {
			transmitter.send(new MapRegionBuilder(!isRenderable()).build(this));
			if (wasAtDynamicRegion) {
				getRenderData().getLocalNpcs().clear();
			}
		}
		removeAttribute(AttributeKey.FORCE_NEXT_MAP_LOAD);
		getRegion().handleRegionEntry(this);
	}
	
	/**
	 * Gets the amount of health points we have
	 */
	@Override
	public int getHealthPoints() {
		return variables.getHealthPoints();
	}
	
	@Override
	public void setHealthPoints(int healthPoints) {
		variables.setHealthPoints(healthPoints);
		transmitter.refreshHealthPoints(healthPoints);
	}
	
	@Override
	public void receiveHit(Hit hit) {
		// only hitsplats we care about are combat ones
		if (!hit.getSplat().isDefaultCombatSplat()) {
			return;
		}
		StaticCombatFormulae.autoRetaliate(hit.getSource(), this);
		// adjust hit so we don't hit too high
		if (hit.getDamage() > getHealthPoints()) {
			hit.setDamage(getHealthPoints());
		}
		// prayers handle the hit first
		manager.getPrayers().handleHit(hit);
		// absorption after prayer so the actual hit isn't affected
		equipment.handleAbsorption(hit);
		// the player is unhittable so we don't do this
		if (!getAttribute("unhittable", false)) {
			// if we should die after damage lands
			if (variables.reduceHealth(hit.getDamage())) {
				checkDeathEvent();
			}
		}
		// we don't remove the attribute when the hit is received in case the damage < 4
		if (getAttribute("cast_veng", false) && hit.getDamage() >= 4) {
			removeAttribute("cast_veng");
			sendForcedChat("Taste vengeance!");
			hit.getSource().getHitMap().applyHit(new Hit(this, (int) Math.floor(hit.getDamage() * 0.75), HitSplat.REGULAR_DAMAGE));
		}
		// the hit is no longer modifiable, the actual damage received will be stored now.
		transmitter.refreshHealthPoints(variables.getHealthPoints());
	}
	
	@Override
	public boolean fighting() {
		return manager.getActions().getAction() instanceof PlayerCombatAction;
	}
	
	/**
	 * Fire this later
	 */
	@Override
	public void fireDeathEvent() {
		// if the activity should handle death instead
		if (manager.getActivities().handleEntityDeath(this)) {
			return;
		}
		// vars
		final Player player = this;
		final Entity killer = getHitMap().getMostDamageEntity();
		
		// we can't walk anymore
		getMovement().resetWalkSteps();
		// we can't do anything while dying.
		manager.getLocks().lockAll();
		// the event
		SystemManager.getScheduler().schedule(new ScheduledTask(1, 6) {
			@Override
			public void run() {
				if (getTicksPassed() == 1) {
					sendAnimation(836);
				} else if (getTicksPassed() == 2) {
					transmitter.sendMessage("Oh dear, you have died.");
					/*if (source instanceof Player) {
						Player killer = (Player) source;
						killer.setAttackedByDelay(4);
					}*/
				} else if (getTicksPassed() == 5) {
					Item[] kept = WildernessActivity.sendDeathContainer(player, killer);
					
					player.equipment.getItems().clear();
					player.inventory.getItems().clear();
					player.equipment.sendContainer();
					player.inventory.sendContainer();
					player.restoreAll();
					
					teleport(GameConstants.DEATH_LOCATION);
					sendAnimation(-1);
					
					// add the items we should've kept to our inventory...
					if (kept != null) {
						for (Item item : kept) {
							player.inventory.addItem(item.getId(), item.getAmount());
						}
					}
				} else if (getTicksPassed() == 6) {
					// TODO:	getPackets().sendMusicEffect(90);
				}
			}
		});
	}
	
	/**
	 * Sends the settings to the client
	 */
	public void sendSettings() {
		transmitter.send(new ConfigFilePacketBuilder(8780, variables.isFilteringProfanity() ? 0 : 1).build(this));
		transmitter.send(new ConfigPacketBuilder(170, getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 0 : 1).build(this));
		transmitter.send(new ConfigPacketBuilder(171, getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true) ? 0 : 1).build(this));
		transmitter.send(new ConfigPacketBuilder(427, variables.isAcceptingAid() ? 1 : 0).build(this));
		
		transmitter.send(new ConfigPacketBuilder(1240, getVariables().getHealthPoints() * 2).build(this));
		transmitter.send(new ConfigPacketBuilder(2382, getVariables().getPrayerPoints()).build(this));
		
		transmitter.refreshRunOrbStatus();
		transmitter.refreshEnergy();
		transmitter.refreshHealthPoints(getHealthPoints());
	}
	
	@Override
	public void restoreAll() {
		skills.restoreAll();
		manager.getHintIcons().removeAll();
		manager.getActions().stopAction();
		variables.setRunEnergy(100);
		removeAttribute("dying");
		
		getCombatDefinitions().setSpecialEnergy((byte) 100);
		getCombatDefinitions().setSpecialActivated(false);
		getCombatDefinitions().resetSpells(true);
		unfreeze();
		sendSettings();
		
		getUpdateMasks().register(new AppearanceUpdate(this));
		manager.getLocks().unlockAll();
	}
	
	@Override
	public void checkMultiArea() {
		super.checkMultiArea();
		transmitter.send(new CS2ConfigBuilder(616, isAtMultiArea() ? 1 : 0).build(this));
	}
	
	/**
	 * Checks if the interaction task should be removed, after processing it.
	 */
	public void checkInteractionTask() {
		if (interactionTask != null && interactionTask.process(this)) {
			setInteractionTask(null);
		}
	}
	
	/**
	 * Registers a player to the lobby
	 */
	public void registerToLobby() {
		registerTransients();
		
		session.write(new LobbyResponseBuilder().build(this));
		manager.getContacts().sendLogin();
		System.out.println("Player registered to lobby:\t" + this);
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		transmitter.send(new PlayerRendering().build(this));
		transmitter.send(new NPCRendering().build(this));
	}
	
	/**
	 * Stops specified events
	 *
	 * @param actions
	 * 		If we should stop actions
	 * @param travel
	 * 		If we should stop travels
	 * @param interfaces
	 * 		If we should stop interfaces
	 * @param animations
	 * 		If we should stop animations
	 */
	public void stop(boolean actions, boolean travel, boolean interfaces, boolean animations) {
		if (animations) {
			sendAnimation(-1);
		}
		if (interfaces) {
			manager.getInterfaces().closeAll();
		}
		if (travel) {
			setInteractionTask(null);
			getMovement().resetWalkSteps();
			transmitter.sendMinimapFlagReset();
		}
		if (actions) {
			getManager().getActions().stopAction();
		}
		NPC interactingNPC = getAttribute(AttributeKey.INTERACTING_NPC);
		if (interactingNPC != null) {
			interactingNPC.endPlayerInteraction(this);
		}
		turnTo(null);
	}
	
	/**
	 * De-registers a player from the lobby
	 */
	public void deregisterLobby() {
		System.out.println("Player deregistered from lobby:" + this);
	}
	
	/**
	 * Restores the run energy by 1.
	 */
	public void restoreRunEnergy() {
		if (getMovement().getNextRunDirection() != -1 || getVariables().getRunEnergy() >= 100) {
			return;
		}
		getVariables().setRunEnergy(getVariables().getRunEnergy() + 1);
		transmitter.refreshEnergy();
		transmitter.refreshRunOrbStatus();
	}
	
	/**
	 * Updates the fight area flag
	 */
	public void setInFightArea(boolean inFightArea) {
		variables.setInFightArea(inFightArea);
		session.write(new PlayerOptionPacketBuilder(inFightArea ? "Attack" : "null", true, 1).build(this));
		//	TODO: getPackets().sendPlayerUnderNPCPriority(inFightArea);
	}
	
	/**
	 * This method finds all the items that the player contains on them.
	 */
	public CopyOnWriteArrayList<Item> findContainedItems() {
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<>();
		for (int i = 0; i < 14; i++) {
			final Item item = equipment.getItem(i);
			if (item != null && item.getId() != -1 && item.getAmount() != -1) {
				containedItems.add(new Item(item.getId(), item.getAmount()));
			}
		}
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItems().get(i);
			if (item != null && item.getId() != -1 && item.getAmount() != -1) {
				containedItems.add(new Item(item.getId(), item.getAmount()));
			}
		}
		return containedItems;
	}
	
	/**
	 * Saves the player
	 */
	public void save() {
		MasterCommunication.write(new PlayerFilePacketOut(details.getUsername(), Utility.getJsonText(this, true)));
	}
	
	/**
	 * Handles the logging out of a player
	 *
	 * @param lobby
	 * 		If the player should be sent to the lobby
	 */
	public void logout(boolean lobby) {
		long currentTime = System.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			transmitter.sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		transmitter.sendLogout(lobby);
	}
}