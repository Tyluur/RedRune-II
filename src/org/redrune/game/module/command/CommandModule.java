package org.redrune.game.module.command;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/30/2017
 */
public abstract class CommandModule {
	
	/**
	 * The identifiers of the string
	 */
	public abstract String[] identifiers();
	
	/**
	 * Handles the command
	 *
	 * @param player
	 * 		The player handling the command
	 */
	public abstract void handle(Player player, String[] args, boolean console);
	
	/**
	 * The right the player must have to use this command
	 */
	@Getter
	@Setter
	private PlayerRight rightRequired;
	
	/**
	 * The manifest of the command
	 */
	@Getter
	@Setter
	private CommandManifest manifest;
	
	/**
	 * Sends a message over to the player.
	 *
	 * @param player
	 * 		The player
	 * @param message
	 * 		The message
	 * @param clientCommand
	 * 		If it is a client response
	 */
	public static void sendResponse(Player player, String message, boolean clientCommand) {
		if (clientCommand) {
			player.getTransmitter().sendConsoleMessage(message);
		} else {
			player.getTransmitter().sendMessage(message);
		}
	}
	
	/**
	 * If the command can be used in danger zones.
	 */
	// TODO usage of this when implemented
	public boolean canBeUsedInDangerZone() {
		return false;
	}
	
	/**
	 * If a command is only allowed to be used by the console.
	 */
	public boolean consoleUsageOnly() {
		return false;
	}
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	protected String[] arguments(String... varArgs) {
		return varArgs;
	}
	
	/**
	 * Gets an integer from the parameter
	 *
	 * @param args
	 * 		The parameters
	 * @param slot
	 * 		The slot
	 */
	protected Integer intParam(String[] args, int slot) {
		return Integer.parseInt(args[slot]);
	}
	
	/**
	 * Gets a boolean from the parameter
	 *
	 * @param args
	 * 		The parameter
	 * @param slot
	 * 		The slot
	 */
	protected Boolean boolParam(String[] args, int slot) {
		return Boolean.parseBoolean(args[slot]);
	}
	
	/**
	 * Gets a string from the parameters
	 *
	 * @param args
	 * 		The parameters
	 * @param slot
	 * 		The slot
	 */
	protected String stringParam(String[] args, int slot) {
		return args[slot];
	}
	
	/**
	 * Gets a completed version of a string array
	 *
	 * @param array
	 * 		The array
	 * @param index
	 * 		The index to start at
	 */
	protected String getCompleted(String[] array, int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < array.length; i++) {
			if (i == array.length - 1 || array[i + 1].startsWith("+")) {
				return sb.append(array[i]).toString();
			}
			sb.append(array[i]).append(" ");
		}
		return "null";
	}
	
	/**
	 * Removes the underscores from the name and replaces them with spaces
	 *
	 * @param unformattedUsername
	 * 		The username
	 */
	protected String formattedUsername(String unformattedUsername) {
		return unformattedUsername.replaceAll("_", " ");
	}
}