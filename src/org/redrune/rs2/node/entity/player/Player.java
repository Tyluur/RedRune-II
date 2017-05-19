package org.redrune.rs2.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.NetworkTransmitter;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.components.Credentials;

/**
 * The player that exists in the game.
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
	
	@Getter
	private transient NetworkTransmitter transmitter;
	
	public Player(String username, String password) {
		this.credentials = new Credentials(username, password);
		this.transmitter = new NetworkTransmitter(this);
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public String toString() {
		return "[username=" + credentials.getUsername() + "]";
	}
}
