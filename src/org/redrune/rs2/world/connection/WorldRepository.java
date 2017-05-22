package org.redrune.rs2.world.connection;

import java.util.ArrayList;
import java.util.List;

import org.redrune.rs2.world.connection.impl.World1;

/**
 * WorldRepository.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class WorldRepository {

	private static final List<LobbyWorld> worlds = new ArrayList<LobbyWorld>();

	public static void startWorlds() {
		worlds.add(new World1());
//		worlds.add(new World2());
	}

	public static void add(LobbyWorld e) {
		worlds.add(e);
	}

	public static List<LobbyWorld> getWorlds() {
		return worlds;
	}

	public static LobbyWorld getWorld(int worldId) {
		return worlds.get(worldId - 1);
	}

}
