package org.redrune.rs2.node.entity.player.components;

import java.util.LinkedList;
import java.util.List;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.Location;
import org.redrune.rs2.world.World;
import org.redrune.utility.AttributeKey;

import lombok.Getter;

/**
 * Holds the player's rendering data.
 *
 * @author Jolt environment v2 development team
 * @author Emperor (converted to Java + NPC information).
 */
public class PlayerRenderData {
	
	/**
	 * Holds the players' hash locations.
	 */
	@Getter
	private final int[] hashLocations = new int[2048];
	
	/**
	 * The local player indexes.
	 */
	@Getter
	private final short[] locals = new short[2048];
	
	/**
	 * The global player indexes.
	 */
	@Getter
	private final short[] globals = new short[2048];
	
	/**
	 * The local players.
	 */
	@Getter
	private final boolean[] isLocal = new boolean[2048];
	
	/**
	 * The skipped player indexes.
	 */
	@Getter
	private final byte[] skips = new byte[2048];
	
	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The amount of local players.
	 */
	public int localsCount = 0;
	
	/**
	 * The amount of global players.
	 */
	public int globalsCount = 0;
	
	/**
	 * The list of local NPCs.
	 */
	@Getter
	private List<NPC> localNpcs = new LinkedList<>();
	
	/**
	 * The player's last location.
	 */
	@Getter
	private Location lastLocation;
	
	/**
	 * If the player has just logged in.
	 */
	@Getter
	private boolean onFirstCycle;
	
	/**
	 * The amount of added players in the current update cycle.
	 */
	private int added;
	
	/**
	 * Constructs a new {@code RenderInformation} {@code Object}.
	 *
	 * @param player
	 * 		The player.
	 */
	public PlayerRenderData(Player player) {
		this.player = player;
		this.onFirstCycle = true;
	}
	
	/**
	 * Updates the player's map region packet with player information.
	 *
	 * @param buffer
	 * 		The packet.
	 */
	public void enterWorld(IoWriteEvent buffer) {
		int myIndex = player.getIndex();
		locals[localsCount++] = (short) myIndex;
		isLocal[myIndex] = true;
		hashLocations[myIndex] = 0;
		buffer.initBitAccess();
		buffer.writeBits(30, player.getLocation().get30BitsHash());
		for (short index = 1; index < GameConstants.PLAYERS_LIMIT; index++) {
			if (index == myIndex) {
				continue;
			}
			globals[globalsCount++] = index;
			Player p = World.get().getPlayers().get(index);
			if (p == null || !p.isRenderable()) {
				buffer.writeBits(18, 0);
				continue;
			}
			buffer.writeBits(18, p.getLocation().get18BitsHash());
		}
		buffer.finishBitAccess();
	}
	
	/**
	 * Updates the player rendering information.
	 */
	public void updateInformation() {
		localsCount = 0;
		globalsCount = 0;
		added = 0;
		onFirstCycle = false;
		lastLocation = player.getLocation();
		for (short i = 1; i < GameConstants.PLAYERS_LIMIT; i++) {
			skips[i] >>= 1;
			if (isLocal[i]) {
				locals[localsCount++] = i;
			} else {
				globals[globalsCount++] = i;
			}
			Player p = World.get().getPlayers().get(i);
			if (p != null && p.isRenderable()) {
				hashLocations[i] = p.getLocation().get18BitsHash();
			}
		}
		player.putAttribute(AttributeKey.PLAYER_TELEPORTED, false);
	}
	
	/**
	 * Gets the amount of currently added players in this cycle.
	 *
	 * @return The amount, incremented.
	 */
	public int getAddedIncr() {
		return added++;
	}
	
}