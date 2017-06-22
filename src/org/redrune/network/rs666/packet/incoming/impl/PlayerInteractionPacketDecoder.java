package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.content.action.combat.PlayerCombatAction;
import org.redrune.game.content.action.interaction.PlayerFollowAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public class PlayerInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The attack player opcode
	 */
	private static final byte ATTACK_PLAYER = 43;
	
	/**
	 * The follow player opcode
	 */
	private static final byte FOLLOW_PLAYER = 44;
	
	/**
	 * The trade player opcode
	 */
	private static final byte PLAYER_REQUEST_PROCEED = 90;
	
	@Override
	public int[] bindings() {
		return arguments(ATTACK_PLAYER, FOLLOW_PLAYER, PLAYER_REQUEST_PROCEED);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int index = packet.readShort();
		boolean running = packet.readByte() == 1;
		if (index > 2047 || index < 1) {
			return;
		}
		Player p2 = World.get().getPlayers().get(index);
		switch (packet.getOpcode()) {
			case ATTACK_PLAYER:
				decodePlayerAttack(player, p2);
				break;
			case FOLLOW_PLAYER:
				decodePlayerFollow(player, p2);
				break;
			case PLAYER_REQUEST_PROCEED:
				decodePlayerRequest(player, p2);
				break;
		}
	}
	
	/**
	 * Decodes the player attack packet
	 *
	 * @param player
	 * 		The player
	 * @param other
	 * 		The other player we're attacking
	 */
	private void decodePlayerAttack(Player player, Player other) {
		player.stop(true, true, true, false);
		player.getManager().getActions().startAction(new PlayerCombatAction(other));
	}
	
	/**
	 * Decodes the player follow packet
	 *
	 * @param player
	 * 		The player
	 * @param other
	 * 		The other player we're following
	 */
	private void decodePlayerFollow(Player player, Player other) {
		player.stop(true, true, true, false);
		player.getManager().getActions().startAction(new PlayerFollowAction(other));
	}
	
	/**
	 * Decodes the player request packet
	 *
	 * @param player
	 * 		The player
	 * @param other
	 * 		The other player we're requesting
	 */
	private void decodePlayerRequest(Player player, Player other) {
	
	}
}
