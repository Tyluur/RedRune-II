package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.GameConstants;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.render.NPCRendering;
import org.redrune.game.node.entity.player.data.*;
import org.redrune.game.node.entity.player.render.PlayerRendering;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.world.SequencialUpdate;
import org.redrune.game.world.World;
import org.redrune.game.world.path.NodeInteractionTask;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.MapRegionBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.RunEnergyBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.SkillConstants;

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
	
	public Player(String username, String password, NetworkSession session) {
		super(GameConstants.HOME_LOCATION);
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username, password);
		this.equipment = new PlayerEquipment();
		this.inventory = new PlayerInventory();
		this.variables = new PlayerVariables();
		this.manager = new PlayerManager();
		this.bank = new PlayerBank();
		
		this.setNetworkSession(session);
		this.getNetworkSession().setPlayer(this);
	}
	
	@Override
	public void register() {
		registerTransients();
		
		World.get().getPlayers().add(this);
		
		transmitter.sendLoginComponents();
		equipment.sendContainer();
		inventory.sendContainer();
		skills.refreshAll();
		manager.getNotes().sendLoginConfiguration();
		
		// renderable must be after this because of map region building...
		
		setRenderable(true);
		
		SequencialUpdate.getRenderablePlayers().add(this);
		getUpdateMasks().register(new AppearanceUpdate(this));
		RegionManager.updateEntityRegion(this);
		
		System.out.println("Player registered:\t" + this);
	}
	
	@Override
	public void deregister() {
		setRenderable(false);
		
		World.get().getPlayers().remove(this);
		RegionManager.updateEntityRegion(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		
		System.out.println("Player deregistered:\t" + this);
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
		
		this.manager.registerTransients(this);
		this.transmitter = new NetworkTransmitter(this);
		this.renderData = new PlayerRenderData(this);
		
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
		manager.getEvents().process(this);
		if (interactionTask != null && interactionTask.process(this)) {
			setInteractionTask(null);
		}
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + "]";
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
		//		getTransmitter().send(new VarpPacketBuilder(8780, variables.getAttribute(AttributeKey.FILTERING_PROFANITY, false) ? 0 : 1).build(this));s));
		getTransmitter().send(new ConfigPacketBuilder(170, getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 0 : 1).build(this));
		getTransmitter().send(new ConfigPacketBuilder(171, getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true) ? 0 : 1).build(this));
		getTransmitter().send(new ConfigPacketBuilder(427, getVariables().getAttribute(AttributeKey.ACCEPTING_AID, false) ? 1 : 0).build(this));
		
		getTransmitter().send(new ConfigPacketBuilder(173, getVariables().isRunToggled() ? 1 : 0).build(this));
		getTransmitter().send(new ConfigPacketBuilder(1240, getVariables().getHealthPoints() * 2).build(this));
		getTransmitter().send(new ConfigPacketBuilder(2382, getVariables().getPrayerPoints()).build(this));
		getTransmitter().send(new RunEnergyBuilder(getVariables().getRunEnergy()).build(this));
	}
	
}