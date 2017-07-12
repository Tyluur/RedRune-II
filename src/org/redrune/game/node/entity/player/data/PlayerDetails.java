package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.utility.tool.Misc;
import org.redrune.game.GameFlags;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class PlayerDetails {
	
	/**
	 * Constructs a logger
	 */
	private static final Logger logger = Misc.constructLogger(PlayerDetails.class);
	
	/**
	 * The username of the player
	 */
	@Getter
	private final String username;
	
	/**
	 * The set of the rights the player has
	 */
	@Getter
	private final SortedSet<PlayerRight> rights;
	
	/**
	 * The player's appearance
	 */
	@Getter
	private final PlayerAppearance appearance;
	
	/**
	 * The password of the player
	 */
	@Getter
	@Setter
	private String password;
	
	/**
	 * The last ip address the player last from
	 */
	@Getter
	@Setter
	private String lastIp;
	
	/**
	 * Constructs a new {@code Credentials} {@code Object}
	 *
	 * @param username
	 * 		The username
	 */
	public PlayerDetails(String username) {
		this.username = username;
		this.rights = new TreeSet<>(Comparator.comparingInt(Enum::ordinal));
		this.appearance = new PlayerAppearance();
		this.rights.add(GameFlags.debugMode ? PlayerRight.OWNER : PlayerRight.PLAYER);
		this.rights.add(PlayerRight.PLAYER);
	}
	
	/**
	 * Gets the most dominant right. The {@link #rights} are sorted based on the position of the right in the enum
	 * (ordinal), so the first right will be the most dominant  .
	 *
	 * @return A {@code Right} instance
	 */
	public PlayerRight getDominantRight() {
		if (rights.size() == 0) {
			logger.log(Level.SEVERE, "Unexpected situation - rights set was empty!");
			return PlayerRight.PLAYER;
		} else {
			return rights.first();
		}
	}
	
	/**
	 * If there are donator rights in the {@link #rights} set
	 */
	public boolean isDonator() {
		return rights.contains(PlayerRight.DONATOR) || rights.contains(PlayerRight.EXTREME_DONATOR);
	}
	
	/**
	 * If the {@link #rights} set has any of these parameters, this is true
	 *
	 * @param rights
	 * 		The rights
	 */
	public boolean rightsContains(PlayerRight... rights) {
		for (PlayerRight right : rights) {
			if (this.rights.contains(right)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The display name of the player
	 */
	public String getDisplayName() {
		return Misc.formatPlayerNameForDisplay(username);
	}
	
}