package org.redrune.rs2.node.entity.player.render.update;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.AttributeKey;

/**
 * @author Sean
 */
public enum GlobalUpdateStage {
	
	ADD_PLAYER,
	HEIGHT_UPDATED,
	MAP_REGION_DIRECTION,
	TELEPORTED;
	
	/**
	 * Gets the global update stages.
	 * @param player The player for the update,
	 * @param otherPlayer The players to update for.
	 * @return The state.
	 */
	public static GlobalUpdateStage getStage(Player player, Player otherPlayer) {
		if (otherPlayer == null || !otherPlayer.isRenderable()) {
			return null;
		} else if (player != otherPlayer && player.getLocation().isWithinDistance(otherPlayer.getLocation())) {
			return ADD_PLAYER;
		} else if (otherPlayer.getRenderData().getLastLocation() != null && otherPlayer.getLocation().getZ() != otherPlayer.getRenderData().getLastLocation().getZ()) {
			return HEIGHT_UPDATED;
		} else if (otherPlayer.getAttribute(AttributeKey.PLAYER_TELEPORTED, false) || otherPlayer.getRenderData().isOnFirstCycle()) {
			return TELEPORTED;
		}
		return null;
	}
}