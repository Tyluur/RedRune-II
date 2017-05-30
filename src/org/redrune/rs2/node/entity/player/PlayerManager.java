package org.redrune.rs2.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.rs2.node.entity.player.link.EventManager;
import org.redrune.rs2.node.entity.player.link.InterfaceManager;
import org.redrune.rs2.node.entity.player.link.NoteManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class PlayerManager {
	
	/**
	 * The note manager instance for the player
	 */
	@Getter
	private final NoteManager notes;
	
	/**
	 * The interface manager of the player
	 */
	@Getter
	@Setter
	private transient InterfaceManager interfaces;
	
	/**
	 * The event manager object
	 */
	@Getter
	@Setter
	private transient EventManager events;
	
	PlayerManager() {
		this.notes = new NoteManager();
	}
	
	/**
	 * Registers the transient variables
	 *
	 * @param player
	 * 		The player
	 */
	void registerTransients(Player player) {
		this.setEvents(new EventManager());
		this.setInterfaces(new InterfaceManager());
		
		this.interfaces.setPlayer(player);
		this.notes.setPlayer(player);
	}
	
}