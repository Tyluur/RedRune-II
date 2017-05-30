package org.redrune.rs2.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.network.rs666.packet.structure.out.MapRegionBuilder;
import org.redrune.network.rs666.packet.structure.out.RunEnergyBuilder;
import org.redrune.network.rs666.packet.structure.out.VarpPacketBuilder;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.npc.render.NPCRendering;
import org.redrune.rs2.node.entity.player.data.*;
import org.redrune.rs2.node.entity.player.render.PlayerRendering;
import org.redrune.rs2.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.rs2.world.SequencialUpdate;
import org.redrune.rs2.world.World;
import org.redrune.rs2.world.map.region.RegionManager;
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
	 * The skills of the player
	 */
	@Getter
	private final PlayerSkills skills;
	
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
	
	public Player(String username, String password, NetworkSession session) {
		super(GameConstants.HOME_LOCATION);
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username, password);
		this.equipment = new PlayerEquipment();
		this.inventory = new PlayerInventory();
		this.variables = new PlayerVariables();
		this.manager = new PlayerManager();
		
		this.setNetworkSession(session);
		this.getNetworkSession().setPlayer(this);
	}
	
	@Override
	public void register() {
		registerTransients();
		
		World.get().getPlayers().add(this);
		SequencialUpdate.getRenderablePlayers().add(this);
		RegionManager.getRegion(getLocation().getX(), getLocation().getY()).addEntity(this);
		
		transmitter.sendLoginComponents();
		equipment.sendContainer();
		inventory.sendContainer();
		skills.refreshAll();
		manager.getNotes().sendLoginConfiguration();
		setRenderable(true);
		
		System.out.println("Player registered:\t" + this);
	}
	
	@Override
	public void deregister() {
		World.get().getPlayers().remove(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		RegionManager.getRegion(getLocation().getX(), getLocation().getY()).removeEntity(this);
		setRenderable(false);
		
		System.out.println("Player deregistered:\t" + this);
	}
	
	@Override
	public int getSize() {
		return 1;
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
		
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
		this.inventory.setPlayer(this);
	}
	
	@Override
	public Player toPlayer() {
		return this;
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
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + "]";
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		if (getAttribute(AttributeKey.MAP_REGION_CHANGED, false)) {
			getTransmitter().send(new MapRegionBuilder(false).build(this));
		}
		getTransmitter().send(new NPCRendering().build(this));
		getTransmitter().send(new PlayerRendering().build(this));
		getUpdateMasks().register(new AppearanceUpdate(this));
	}
	
	/**
	 * Sends the settings to the client
	 */
	public void sendSettings() {
		//		getTransmitter().send(new VarpPacketBuilder(8780, variables.getAttribute(AttributeKey.FILTERING_PROFANITY, false) ? 0 : 1).build(this));s));
		getTransmitter().send(new VarpPacketBuilder(170, getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 0 : 1).build(this));
		getTransmitter().send(new VarpPacketBuilder(171, getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true) ? 0 : 1).build(this));
		getTransmitter().send(new VarpPacketBuilder(427, getVariables().getAttribute(AttributeKey.ACCEPTING_AID, false) ? 1 : 0).build(this));
		
		getTransmitter().send(new VarpPacketBuilder(173, getVariables().isRunToggled() ? 1 : 0).build(this));
		getTransmitter().send(new VarpPacketBuilder(1240, getVariables().getHealthPoints() * 2).build(this));
		getTransmitter().send(new VarpPacketBuilder(2382, getVariables().getPrayerPoints()).build(this));
		getTransmitter().send(new RunEnergyBuilder(getVariables().getRunEnergy()).build(this));
	}
}