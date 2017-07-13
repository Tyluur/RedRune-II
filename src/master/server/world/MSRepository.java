package master.server.world;

import master.server.network.MSSession;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public final class MSRepository {
	
	/**
	 * The array of worlds that we hold
	 */
	private static final MSWorld[] worlds = new MSWorld[10];
	
	/**
	 * Creates a new world
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public static MSWorld createNewWorld(int worldId) {
		final int index = worldId - 1;
		if (index < 0 || index >= worlds.length) {
			throw new IllegalStateException("Unexpected world id: " + worldId);
		}
		// we've already made this world.
		if (worlds[index] != null) {
			System.out.println("Attempted to create a new world when it was already made: " + worldId);
			return null;
		}
		// creates a new world
		final MSWorld world = new MSWorld(worldId);
		// saves the index of the world
		worlds[index] = world;
		
		System.out.println("World " + worldId + " was just registered & verified.");
		return world;
	}
	
	/**
	 * Checks if a player is online on any of the worlds
	 *
	 * @param username
	 * 		The username of the player
	 * @param checkLobby
	 * 		If we should check if the player is in the world's lobby as well
	 */
	public static boolean isOnline(String username, boolean checkLobby) {
		boolean online = false;
		
		// loop through all the worlds
		for (MSWorld world : worlds) {
			if (world == null) {
				continue;
			}
			// found the player is online, loop doesn't need to continue.
			if (world.isOnline(username, checkLobby)) {
				online = true;
				break;
			}
		}
		return online;
	}
	
	/**
	 * Gets a world by ids id
	 *
	 * @param worldId
	 * 		The world id
	 */
	public static Optional<MSWorld> getWorld(int worldId) {
		final int index = worldId - 1;
		if (index < 0 || index >= worlds.length) {
			throw new IllegalStateException("Unexpected world id: " + worldId);
		}
		return Optional.of(worlds[index]);
	}
	
	/**
	 * Unregisters the world
	 *
	 * @param world
	 * 		The world
	 */
	public static void unregister(MSWorld world) {
		worlds[world.getId() - 1] = null;
		world.unregister();
		System.out.println("World " + world.getId() + " was just unregistered.");
	}
	
	/**
	 * If the player exists in a world, we return the master session that that player is in
	 *
	 * @param username
	 * 		The name of the player
	 */
	public static Optional<MSSession> getSessionByUsername(String username) {
		// loop through all the worlds
		for (MSWorld world : worlds) {
			if (world == null) {
				continue;
			}
			// there is a player by that name in this world so this is the right world
			if (world.playerRegistered(username)) {
				return Optional.of(world.getSession());
			}
		}
		return Optional.empty();
	}
}