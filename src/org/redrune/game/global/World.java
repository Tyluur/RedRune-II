package org.redrune.game.global;

import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter.HunterNPC;
import org.redrune.game.content.entity.actor.player.skills.slayer.SlayerHelp;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.ActorList;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtension;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtensionHolder;
import org.redrune.game.entity.actor.npc.impl.corp.CorporealBeast;
import org.redrune.game.entity.actor.npc.impl.dragons.KingBlackDragon;
import org.redrune.game.entity.actor.npc.impl.jad.TzTokJad;
import org.redrune.game.entity.actor.npc.impl.kalph.KalphiteQueen;
import org.redrune.game.entity.actor.npc.impl.normal.Jadinko;
import org.redrune.game.entity.actor.npc.impl.normal.Polypore;
import org.redrune.game.entity.actor.npc.impl.normal.Slayer;
import org.redrune.game.entity.actor.npc.impl.others.*;
import org.redrune.game.entity.actor.npc.impl.slayer.Strykewyrm;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.functions.Misc.Direction;
import org.redrune.utility.game.session.AntiFlood;

import java.util.List;
import java.util.Optional;

import static org.redrune.game.global.map.region.RegionManager.getRegion;

/**
 * This class denotes a world
 */
public final class World {
	
	/**
	 * The list of players in the world
	 */
	private static final ActorList<Player> lobbyPlayers = new ActorList<>(GameConstants.PLAYERS_LIMIT);
	
	/**
	 * The list of players in the world
	 */
	private static final ActorList<Player> players = new ActorList<>(GameConstants.PLAYERS_LIMIT);
	
	/**
	 * The list of npcs in the world
	 */
	private static final ActorList<NPC> npcs = new ActorList<>(GameConstants.NPCS_LIMIT);
	
	/**
	 * Sends a message to all players in the world
	 *
	 * @param message
	 * 		The message
	 * @param forStaff
	 * 		If the message is only for staff members
	 */
	public static void sendWorldMessage(String message, boolean forStaff) {
		for (Player p : getPlayers()) {
			if (p == null || !p.isRunning() || (forStaff && !p.isStaff())) {
				continue;
			}
			p.getPackets().sendGameMessage(message);
		}
	}
	
	/**
	 * Gets all the players in the world
	 */
	public static ActorList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets all the players in the lobby
	 */
	public static ActorList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}
	
	/**
	 * Gets all the npcs in the world
	 */
	public static ActorList<NPC> getNPCs() {
		return npcs;
	}
	
	/**
	 * Adds a player to the world
	 */
	public static void addWorldPlayer(Player player) {
		players.add(player);
		AntiFlood.add(player.getSession().getIp());
	}
	
	/**
	 * Adds a player to the lobby
	 *
	 * @param player
	 * 		The player
	 */
	public static void addLobbyPlayer(Player player) {
		lobbyPlayers.add(player);
	}
	
	/**
	 * Removes a player from the world
	 */
	public static void removePlayer(Player player) {
		if (player.getSession().isInLobby()) {
			lobbyPlayers.remove(player);
		} else {
			players.remove(player);
			AntiFlood.remove(player.getSession().getIp());
		}
	}
	
	/**
	 * Adds an npc to the world
	 */
	public static void addNPC(NPC npc) {
		npcs.add(npc);
	}
	
	/**
	 * Removes an npc from the world
	 */
	public static void removeNPC(NPC npc) {
		npcs.remove(npc);
	}
	
	/**
	 * Checks the controllers that the player should be in after they move. This is only fired if they don't have a
	 * controller
	 */
	public static void checkControllersAtMove(Player player) {
		if (Wilderness.isAtWild(player)) {
			player.getControllerManager().startController("Wilderness");
		}
	}
	
	/**
	 * Spawns an npc to the world with a direction
	 */
	public static NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, Direction direction) {
		NPC npc = spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
		npc.setFaceDirection(direction.getValue());
		return npc;
	}
	
	/**
	 * Spawns an npc
	 */
	public static NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		NPC n = null;
		HunterNPC hunterNPCs = HunterNPC.forId(id);
		if (hunterNPCs != null) {
			if (id == hunterNPCs.getNpcId()) {
				n = new HuntNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
			}
		} else if (SlayerHelp.isSlayer(id)) {
			n = new Slayer(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6142 || id == 6144 || id == 6145 || id == 6143) {
			n = new PestMonsters(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14688 || id == 14690 || id == 14692 || id == 14694 || id == 14696 || id == 14698 || id == 14700) {
			n = new Polypore(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13820 || id == 13821 || id == 13822) {
			n = new Jadinko(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 1158 || id == 1160) {
			n = new KalphiteQueen(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 2745) {
			n = new TzTokJad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8528) {
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 50 || id == 2642) {
			n = new KingBlackDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 9462 && id <= 9467) {
			n = new Strykewyrm(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 8133) {
			n = new CorporealBeast(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14256) {
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8349 || id == 8450 || id == 8451) {
			n = new TormentedDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14301) {
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else {
			n = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		}
		Optional<NPCExtension> optional = NPCExtensionHolder.getExtension(id);
		if (n != null && optional.isPresent()) {
			n.addExtension(optional.get());
		}
		return n;
	}
	
	/**
	 * Checks if the world contains a player with a certain username
	 */
	public static boolean containsPlayer(String username) {
		for (Player p2 : players) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the {@code Player} {@code Object} of a player whose username matches
	 */
	public static Player getPlayer(String username) {
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Gets a {@code Player} {@code Object} of a player whose username/display name matches
	 */
	public static Player getPlayerByDisplayName(String username) {
		String playerNameForProtocol = Misc.formatPlayerNameForProtocol(username);
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(playerNameForProtocol) || player.getDisplayName().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a tile is a multi area
	 */
	public static boolean isMultiArea(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return (destX >= 3200 && destX <= 3390 && destY >= 3840 && destY <= 3967) // wild
				       || (destX >= 2835 && destX <= 2880 && destY >= 5905 && destY <= 5950) || (destX >= 2992 && destX <= 3007 && destY >= 3912 && destY <= 3967) || (destX >= 2946 && destX <= 2959 && destY >= 3816 && destY <= 3831) || (destX >= 3008 && destX <= 3199 && destY >= 3856 && destY <= 3903) || (destX >= 3008 && destX <= 3071 && destY >= 3600 && destY <= 3711) || (destX >= 3270 && destX <= 3346 && destY >= 3532 && destY <= 3625) || (destX >= 2965 && destX <= 3050 && destY >= 3904 && destY <= 3959) // wild
				       || (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375) || (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				       || (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				       // godwars
				       || KingBlackDragon.atKBD(tile) // King Black Dragon lair
				       || TormentedDemon.atTD(tile) // Tormented demon's area
				       || (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				       || (destX >= 3136 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY)) || (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				       || (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				       || (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				       || (destX >= 3086 && destY >= 5536 && destX <= 3315 && destY <= 5530) // Bork
				       || (tile.getX() >= 3526 && tile.getX() <= 3550 && tile.getY() >= 5185 && tile.getY() <= 5215) // out
				       || (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532); // castlewars
		// in
		
		// multi
	}
	
	public static void sendObjectAnimation(WorldObject object, Animation animation) {
		sendObjectAnimation(null, object, animation);
	}
	
	public static void sendObjectAnimation(Actor creator, WorldObject object, Animation animation) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(object)) {
					continue;
				}
				player.getPackets().sendObjectAnimation(object, animation);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(object)) {
						continue;
					}
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}
	
	/**
	 * Checks if a tile is a pvp area
	 */
	public static boolean isPvpArea(WorldTile tile) {
		return Wilderness.isAtWild(tile);
	}
	
}
