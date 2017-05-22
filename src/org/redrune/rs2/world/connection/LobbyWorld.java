package org.redrune.rs2.world.connection;

import org.redrune.rs2.node.entity.EntityList;
import org.redrune.rs2.node.entity.player.Player;

public abstract class LobbyWorld {

	public static final int MAX_PLAYER_CAP = 2048;

//	private final RegionManager regionManager = new RegionManager();

	public abstract int getWorldId();

	public abstract String getWorldAddress();

	public abstract String getWorldActivity();

	public abstract int getCountryId();

	public abstract boolean isMembersOnly();

	public abstract boolean isLootshareEnabled();

	public abstract void addPlayer(Player player);

	public abstract void removePlayer(Player player);

//	public abstract void addNPC(NPC npc);

//	public abstract void removeNPC(NPC npc);

	public abstract EntityList<Player> getWorldPlayers();

//	public abstract EntityChamber<NPC> getNpcs();

	public abstract boolean containsPlayer(String username);

	public abstract Player getPlayer(String username);

//	public RegionManager getRegionManager() {
//		return regionManager;
//	}

}
