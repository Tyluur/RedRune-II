package org.redrune.rs2.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.components.Credentials;
import org.redrune.rs2.node.entity.player.render.RenderInformation;
import org.redrune.rs2.world.Location;
import org.redrune.rs2.world.World;

/**
 * The player that created in the game.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Player extends Entity {
	
	/**
	 * The credentials of the player
	 */
	@Getter
	private final Credentials credentials;
	
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
	private transient RenderInformation renderInformation;
	
	public Player(String username, String password, NetworkSession session) {
		super(new Location(3333, 3333));
		this.credentials = new Credentials(username, password);
		this.setNetworkSession(session);
		this.getNetworkSession().setPlayer(this);
	}
	
	@Override
	public void register() {
		generateTransients();
		World.get().getPlayers().add(this);
		transmitter.sendLoginComponents();
		setCreated(true);
		
		System.out.println(this);
	}
	
	@Override
	public void deregister() {
		World.get().getPlayers().remove(this);
		setCreated(false);
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public String toString() {
		return "[username=" + credentials.getUsername() + ", index=" + getIndex() + ", right=" + credentials.getDominantRight() + "]";
	}
	
	/**
	 * Generates the transient objects (objects which will not save)
	 */
	public void generateTransients() {
		this.transmitter = new NetworkTransmitter(this);
		this.renderInformation = new RenderInformation(this);
	}
}
