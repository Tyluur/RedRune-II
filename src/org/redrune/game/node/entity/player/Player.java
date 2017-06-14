package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.SequencialUpdate;
import org.redrune.game.GameConstants;
import org.redrune.game.node.NodeInteractionTask;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.render.NPCRendering;
import org.redrune.game.node.entity.player.data.*;
import org.redrune.game.node.entity.player.render.PlayerRendering;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.packet.out.client.build.ClientSessionDisconnectionBuilder;
import org.redrune.network.master.packet.out.client.context.ClientSessionDisconnectionContext;
import org.redrune.network.master.server.login.MasterServerLogin;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.network.rs666.packet.outgoing.impl.*;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.rs.input.InputType;

import java.util.Arrays;

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
	 * The networkSession attached to the player
	 */
	@Getter
	@Setter
	private transient NetworkSession networkSession;
	
	/**
	 * The network transmitter object
	 */
	@Getter
	private transient NetworkTransmitter transmitter;
	
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
		
		// renderable must be after this because of map region building...
		
		setRenderable(true);
		
		SequencialUpdate.getRenderablePlayers().add(this);
		getUpdateMasks().register(new AppearanceUpdate(this));
		RegionManager.updateEntityRegion(this);
		variables.putAttribute(AttributeKey.LAST_LONGIN_STAMP, System.currentTimeMillis());
		details.setLastIp(Misc.getIpAddress(networkSession.getChannel()));
		
		System.out.println("Player registered into game:\t" + this);
	}
	
	@Override
	public void deregister() {
		setRenderable(false);
		
		World.get().getPlayers().remove(this);
		RegionManager.updateEntityRegion(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		RS2MasterCommunication.writeMasterPacket(new ClientSessionDisconnectionBuilder(new ClientSessionDisconnectionContext(networkSession.getUid(), networkSession.isInLobby(), getDetails().getUsername(), MasterServerLogin.generateJsonFileText(this, false))).build());
		
		System.out.println("Player deregistered from game:\t" + this);
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public Player toPlayer() {
		return this;
	}
	
	/**
	 * Generates the transient objects (objects which will not save)
	 */
	@Override
	public void registerTransients() {
		super.registerTransients();
		
		this.transmitter = new NetworkTransmitter(this);
		this.renderData = new PlayerRenderData(this);
		this.manager.registerTransients(this);
		
		// actual player things
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
		this.inventory.setPlayer(this);
		this.bank.setPlayer(this);
	}
	
	@Override
	public void loadMapRegions() {
		super.loadMapRegions();
		getTransmitter().send(new MapRegionBuilder(!isRenderable()).build(this));
		getRegion().handleRegionEntry(this);
	}
	
	@Override
	public int getHitpoints() {
		return variables.getHealthPoints();
	}
	
	@Override
	public int getMaxHitpoints() {
		return skills.getLevelForXp(SkillConstants.HITPOINTS) * 10;
	}
	
	@Override
	public void tick() {
		checkInteractionTask();
		manager.getEvents().process(this);
		manager.getActions().process();
		manager.getPrayers().process();
	}
	
	/**
	 * Checks if the interaction task should be removed, after processing it.
	 */
	public void checkInteractionTask() {
		if (interactionTask != null && interactionTask.process(this)) {
			setInteractionTask(null);
		}
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + ", lobby=" + networkSession.isInLobby() + "]";
	}
	
	/**
	 * Logs the player out
	 *
	 * @param lobby
	 * 		If they should be sent to the lobby
	 */
	public void logout(boolean lobby) {
		transmitter.send(new LogoutBuilder(lobby).build(this));
	}
	
	/**
	 * Removes the player from the lobby
	 */
	public void leaveLobby() {
		RS2MasterCommunication.writeMasterPacket(new ClientSessionDisconnectionBuilder(new ClientSessionDisconnectionContext(networkSession.getUid(), networkSession.isInLobby(), getDetails().getUsername(), MasterServerLogin.generateJsonFileText(this, false))).build());
		
		System.out.println("Player deregistered from lobby:\t" + this);
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		getTransmitter().send(new PlayerRendering().build(this));
		getTransmitter().send(new NPCRendering().build(this));
	}
	/**
	 * Sends the settings to the client
	 */
	public void sendSettings() {
		getTransmitter().send(new ConfigFilePacketBuilder(8780, variables.getAttribute(AttributeKey.FILTERING_PROFANITY, false) ? 0 : 1).build(this));
		getTransmitter().send(new ConfigPacketBuilder(170, getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 0 : 1).build(this));
		getTransmitter().send(new ConfigPacketBuilder(171, getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true) ? 0 : 1).build(this));
		getTransmitter().send(new ConfigPacketBuilder(427, getVariables().getAttribute(AttributeKey.ACCEPTING_AID, true) ? 1 : 0).build(this));
		
		getTransmitter().send(new ConfigPacketBuilder(173, getVariables().isRunToggled() ? 1 : 0).build(this));
		getTransmitter().send(new ConfigPacketBuilder(1240, getVariables().getHealthPoints() * 2).build(this));
		getTransmitter().send(new ConfigPacketBuilder(2382, getVariables().getPrayerPoints()).build(this));
		getTransmitter().send(new RunEnergyBuilder(getVariables().getRunEnergy()).build(this));
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
			Arrays.stream(InputType.values()).forEach(type -> removeAttribute(type.getName()));
			getTransmitter().closeInputBox();
			getManager().getInterfaces().closeAllInterfaces();
		}
		if (travel) {
			setInteractionTask(null);
			getMovement().resetWalkSteps();
			getTransmitter().sendMinimapFlagReset();
		}
		if (actions) {
			getManager().getActions().forceStop();
		}
		turnTo(null);
		
		NPC interactingNPC = getAttribute(AttributeKey.INTERACTING_NPC);
		if (interactingNPC != null) {
			interactingNPC.endPlayerInteraction(this);
		}
	}
	
}