package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.link.*;

import java.util.HashMap;

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
	
	/**
	 * The lock manager object
	 */
	@Getter
	@Setter
	private transient LockManager locks;
	
	/**
	 * The action manager object
	 */
	@Getter
	@Setter
	private ActionManager actions;
	
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
		this.setActions(new ActionManager());
		this.setLocks(new LockManager());
		
		this.interfaces.setPlayer(player);
		this.notes.setPlayer(player);
		this.actions.setPlayer(player);
		player.getSkills().setLevelsAdvanced(new HashMap<>());
	}
	
}