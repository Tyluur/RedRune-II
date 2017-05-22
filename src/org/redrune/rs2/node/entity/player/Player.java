package org.redrune.rs2.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.network.rs666.packet.structure.out.MapRegionBuilder;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.components.*;
import org.redrune.rs2.node.entity.player.render.PlayerRendering;
import org.redrune.rs2.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.rs2.world.Location;
import org.redrune.rs2.world.SequencialUpdate;
import org.redrune.rs2.world.World;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.SkillConstants;

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
	 * The variables of the player that are saved
	 */
	@Getter
	private final PlayerVariables variables;
	
	/**
	 * The skills of the player
	 */
	@Getter
	private final PlayerSkills skills;
	
	/**
	 * The equipment of the player
	 */
	@Getter
	private final PlayerEquipment equipment = new PlayerEquipment();
	
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
		super(new Location(3333, 3333));
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username, password);
		this.variables = new PlayerVariables();
		this.setNetworkSession(session);
		this.getNetworkSession().setPlayer(this);
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		if (getAttribute(AttributeKey.MAP_REGION_CHANGED, false)) {
			getTransmitter().send(new MapRegionBuilder(false).build(this));
		}
		getTransmitter().send(new PlayerRendering().build(this));
		getUpdateMasks().register(new AppearanceUpdate(this));
	}
	
	@Override
	public void register() {
		registerTransients();
		World.get().getPlayers().add(this);
		SequencialUpdate.getRenderablePlayers().add(this);
		transmitter.sendLoginComponents();
		equipment.sendFullContainer();
		skills.refreshAll();
		setRenderable(true);
		
		System.out.println("Player registered:\t" + this);
	}
	
	@Override
	public void deregister() {
		World.get().getPlayers().remove(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
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
		this.transmitter = new NetworkTransmitter(this);
		this.renderData = new PlayerRenderData(this);
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
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
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + "]";
	}
}