package org.redrune.utility.game.entity.actor.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-29
 */
public class PlayerSaving {
	
	/**
	 * The suffix of the file
	 */
	private static final String SUFFIX = ".json";
	
	/**
	 * The location in which player files are saved
	 */
	private static final String FILES_LOCATION = GameConstants.FILES_PATH + "players/accounts/";
	
	/**
	 * The gson instance for reading from files
	 */
	private static final Gson GSON = new Gson();
	
	/**
	 * Saves the player to the json file
	 *
	 * @param player
	 * 		The player
	 */
	public static void savePlayer(Player player) {
		try (Writer writer = new FileWriter(FILES_LOCATION + player.getUsername() + SUFFIX)) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC);
			Gson gson = builder.create();
			gson.toJson(player, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creating a player object from a saved player file
	 *
	 * @param name
	 * 		The name of the file
	 */
	public static Player fromFile(String name) {
		try {
			File file = new File(getFileLocation(name));
			// The file is too big; its nulled. Instead of dedicating resources we will return a null player
			// which will stop the login
			if (file.length() > 1_000_000) {
				System.err.println("Error reading file: " + file.getAbsolutePath());
				return null;
			}
			return GSON.fromJson(Misc.getText(getFileLocation(name)), Player.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The location of the file for the player
	 *
	 * @param name
	 * 		The name of the player
	 */
	private static String getFileLocation(String name) {
		return FILES_LOCATION + name + SUFFIX;
	}
	
	/**
	 * @param name
	 * 		The name of the player to check for
	 */
	public static boolean playerExists(String name) {
		return new File(getFileLocation(name)).exists();
	}
	
}