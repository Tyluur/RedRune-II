package org.redrune.rs2.world.connection.impl;

import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.EntityList;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.connection.LobbyWorld;
import org.redrune.rs2.world.connection.WorldConstants;

public class World1 extends LobbyWorld {

	private final EntityList<Player> players = new EntityList<Player>(GameConstants.PLAYERS_LIMIT);

//	private final EntityList<NPC> npcs = new EntityList<NPC>(20000);

	@Override
	public int getWorldId() {
		return 1;
	}

	@Override
	public String getWorldAddress() {
		return "127.0.0.1";
	}

	@Override
	public String getWorldActivity() {
		return "RedRune";
	}

	@Override
	public int getCountryId() {
		return WorldConstants.COUNTRY_BELGIUM;
	}

	@Override
	public boolean isMembersOnly() {
		return true;
	}

	@Override
	public boolean isLootshareEnabled() {
		return true;
	}

	@Override
	public void addPlayer(Player player) {
		players.add(player);
	}

	@Override
	public void removePlayer(Player player) {
		players.remove(player);
	}

	@Override
	public EntityList<Player> getWorldPlayers() {
		return players;
	}

//	@Override
//	public EntityChamber<NPC> getNpcs() {
//		return npcs;
//	}

//	@Override
//	public void addNPC(NPC npc) {
//		npcs.add(npc);
//	}
//
//	@Override
//	public void removeNPC(NPC npc) {
//		npcs.remove(npc);
//	}

	@Override
	public boolean containsPlayer(String username) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}

			if (player.getDetails().getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Player getPlayer(String username) {
		for (Player player : players) {
			if (player == null)
				continue;
			if (player.getDetails().getUsername().equalsIgnoreCase(username)) {
				return player;
			}
		}
		return null;
	}

}
