package org.redrune.rs2.node.entity.player.components;

import lombok.Getter;
import org.redrune.rs2.GameFlags;
import org.redrune.rs2.node.entity.player.Right;
import org.redrune.utility.Misc;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Credentials {
	
	/**
	 * Constructs a logger
	 */
	private static final Logger logger = Misc.constructLogger(Credentials.class);
	
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
	
	@Getter
	private final SortedSet<Right> rights;
	
	/**
	 * Constructs a new {@code Credentials} {@code Object}
	 *
	 * @param username
	 * 		The username
	 * @param password
	 * 		The password
	 */
	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
		this.rights = new TreeSet<>(Comparator.comparingInt(Enum::ordinal));
		this.rights.add(GameFlags.debugMode ? Right.OWNER : Right.PLAYER);
	}
	
	/**
	 * Gets the most dominant right. The {@link #rights} are sorted based on the position of the right in the enum
	 * (ordinal), so the first right will be the most dominant  .
	 *
	 * @return A {@code Right} instance
	 */
	public Right getDominantRight() {
		if (rights.size() == 0) {
			logger.log(Level.SEVERE, "Unexpected situation - rights set was empty!");
			return Right.PLAYER;
		} else {
			return rights.first();
		}
	}
	
	/**
	 * If there are donator rights in the {@link #rights} set
	 */
	public boolean isDonator() {
		return rights.contains(Right.DONATOR) || rights.contains(Right.EXTREME_DONATOR);
	}
	
}