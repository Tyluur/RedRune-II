package org.redrune.network.master.server.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.jboss.netty.channel.Channel;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.master.MasterConstants;
import org.redrune.utility.Misc;

import java.io.File;
import java.lang.reflect.Modifier;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class MasterServerLogin {
	
	/**
	 * The gson builder using pretty printing
	 */
	private static final GsonBuilder PRETTY_BUILDER = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC);
	
	/**
	 * The gson builder
	 */
	private static final GsonBuilder BUILDER = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC);
	
	/**
	 * The channel we're writing back to
	 */
	@Getter
	private final Channel channel;
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username attempting to log in
	 */
	@Getter
	private final String username;
	
	/**
	 * The password we're attempting to log in with
	 */
	@Getter
	private final String password;
	
	/**
	 * If we're connecting to the lobby
	 */
	@Getter
	private final boolean lobbyConnection;
	
	/**
	 * The world id we're attempting to connect to
	 */
	@Getter
	private final int worldId;
	
	public MasterServerLogin(Channel channel, long uid, String username, String password, boolean lobbyConnection, int worldId) {
		this.channel = channel;
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.lobbyConnection = lobbyConnection;
		this.worldId = worldId;
	}
	
	/**
	 * Checks if an account exists
	 *
	 * @param username
	 * 		The username of the account
	 */
	public static boolean accountExists(String username) {
		return new File(MasterConstants.SAVE_LOCATION + username + ".json").exists();
	}
	
	/**
	 * Gets the string format of the account from the file
	 *
	 * @param username
	 * 		The username of the account
	 */
	public static String getAccountFileText(String username) {
		return Misc.getText(MasterConstants.SAVE_LOCATION + username + ".json");
	}
	
	/**
	 * Generates the text version of the player object
	 *
	 * @param player
	 * 		The player
	 * @param prettyPrint
	 * 		If we should print text pretty
	 */
	public static String generateJsonFileText(Player player, boolean prettyPrint) {
		Gson gson = (prettyPrint ? PRETTY_BUILDER : BUILDER).create();
		return gson.toJson(player);
	}
	
	/**
	 * Saves the player to the json file
	 *
	 * @param player
	 * 		The player
	 */
	public static void savePlayer(Player player) {
		final String text = generateJsonFileText(player, true);
		Misc.writeTextToFile(MasterConstants.SAVE_LOCATION + Misc.formatPlayerNameForProtocol(player.getDetails().getUsername()) + ".json", text, false);
	}
	
}