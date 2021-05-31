package org.redrune.game.global;

import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.content.entity.actor.player.controller.impl.activity.pvp.PvPWorld;
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
import org.redrune.utility.functions.Misc.FaceDirection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
     * @param message  The message
     * @param forStaff If the message is only for staff members
     */
    public static void sendWorldMessage(String message, boolean forStaff) {
        for (Player p : getPlayers()) {
            if (p == null || !p.isRunning() || (forStaff && !p.isStaff())) {
                continue;
            }
            p.getPackets().sendMessage(message);
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
    }

    /**
     * Adds a player to the lobby
     *
     * @param player The player
     */
    public static void addLobbyPlayer(Player player) {
        lobbyPlayers.add(player);
    }

    /**
     * Removes a player from the world
     */
    public static void removePlayer(Player player, boolean lobby) {
        (lobby ? lobbyPlayers : players).remove(player);
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
     * Checks the controllers that the player should be in after they move.
     */
    public static void checkControllersAtMove(Player player) {
        if (player.getControllerManager().getController() == null) {
            if (Wilderness.isAtWild(player)) {
                player.getControllerManager().startController("Wilderness");
            } else if (GameFlags.pvpWorld && PvPWorld.Companion.isAtWildy(player)) {
                player.getControllerManager().startController("PvPWorld");
            }
        }
    }

    /**
     * Spawns an npc to the world with a direction
     */
    public static NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, FaceDirection direction) {
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
    public static boolean containsPlayer(String username, boolean lobby) {
        for (Player p2 : (lobby ? lobbyPlayers : players)) {
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
        return (destX >= 3462 && destX <= 3511 && destY >= 9481 && destY <= 9521 && tile.getPlane() == 0) // kalphite
                // queen
                // lair
                || (destX >= 4540 && destX <= 4799 && destY >= 5052 && destY <= 5183 && tile.getPlane() == 0) // thzaar
                // city
                || tile.getRegionId() == 11051
                || tile.getRegionId() == 16729 // glacors
                || tile.getRegionId() == 11589 // dags
                || tile.getRegionId() == 10894 // monkey skeles
                || tile.getRegionId() == 11573 // sea troll queen
                || tile.getRegionId() == 10554 || tile.getRegionId() == 10810 // rock crabs
                || (destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249) // mole
                || (destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903)// wild
                || (destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720) || (destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970) || (destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759) || (destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937) || (destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720) || (destX >= 2450 && destX <= 3520 && destY >= 9450 && destY <= 9550) || (destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710) || (destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646) || (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)// wild
                || (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
                || (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
                || (destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714) // chaos dwarf battlefield
                // godwars
                || KingBlackDragon.atKBD(tile) // King Black Dragon lair
                || TormentedDemon.atTD(tile) // Tormented demon's area
//                || Bork.atBork(tile) // Bork's area
                || tile.getRegionId() == 12590 || (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
                || (destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY)) || (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
                || (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
                || (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
                // out
                || (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532) // castlewars
                || (destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631) // Risk
                // ffa.
                || (destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631) // Safe
                // ffa
                || tile.getRegionId() == 1089

                || tile.getRegionId() == 12341 || (tile.getX() >= 3011 && tile.getX() <= 3132 && tile.getY() >= 10052 && tile.getY() <= 10175 && (tile.getY() >= 10066 || tile.getX() >= 3094)) // fortihrny
                // dungeon
                ;
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
     * Converts the {@link #players} {@code EntityList} to a {@code Stream} of players. These are the players that match
     * these requirements: {@link Player#hasStarted()} and !{@link Player#isFinished()}
     *
     * @return A {@code Stream}
     */
    public static Stream<Player> playerStream() {
        return players.stream().filter(p -> p != null && p.hasStarted() && !p.isFinished());
    }

    /**
     * Streams all of the npcs that are live and not finished
     *
     * @return A {@code Stream}
     */
    public static Stream<NPC> npcStream() {
        return npcs.stream().filter(npc -> npc != null && !npc.isFinished());
    }

    /**
     * Checks if a tile is a pvp area
     */
    public static boolean isPvpArea(WorldTile tile) {
        return PvPWorld.Companion.isAtWildy(tile) || Wilderness.isAtWild(tile);
    }

}
