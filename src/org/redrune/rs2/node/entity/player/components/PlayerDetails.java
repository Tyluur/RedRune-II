package org.redrune.rs2.node.entity.player.components;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.redrune.rs2.GameFlags;
import org.redrune.rs2.world.Location;
import org.redrune.utility.Misc;

import lombok.Getter;
import lombok.Setter;

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
	 * The password of the player
	 */
	@Getter
	private final String password;
	
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
	 * The last location the player was at
	 */
	@Getter
	@Setter
	private transient Location lastLocation;
	
	/**
	 * Constructs a new {@code Credentials} {@code Object}
	 *
	 * @param username
	 * 		The username
	 * @param password
	 * 		The password
	 */
	public PlayerDetails(String username, String password) {
		this.username = username;
		this.password = password;
		this.rights = new TreeSet<>(Comparator.comparingInt(Enum::ordinal));
		this.appearance = new PlayerAppearance();
		this.rights.add(GameFlags.debugMode ? PlayerRight.OWNER : PlayerRight.PLAYER);
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
	
}