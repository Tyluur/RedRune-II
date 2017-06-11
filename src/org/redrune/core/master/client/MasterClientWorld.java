package org.redrune.core.master.client;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterClientWorld {
	
	/**
	 * The id of the world
	 */
	@Getter
	private final int worldId;
	
	/**
	 * The amount of players in the world
	 */
	@Getter
	@Setter
	private int size = 0;
	
	/**
	 * If the world is online
	 */
	@Getter
	@Setter
	private boolean isOnline = false;
	
	public MasterClientWorld(int worldId) {
		this.worldId = worldId;
	}
}
