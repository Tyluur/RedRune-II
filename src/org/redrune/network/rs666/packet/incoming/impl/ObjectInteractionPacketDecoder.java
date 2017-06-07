package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.NodeReachEventContext;
import org.redrune.game.node.entity.player.event.context.ObjectEventContext;
import org.redrune.game.node.entity.player.event.impl.NodeReachEvent;
import org.redrune.game.node.entity.player.event.impl.ObjectEvent;
import org.redrune.game.node.object.GameObject;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.InteractionOption;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class ObjectInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(1, 39, 86, 58, 38, 75);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int y = packet.readShortA();
		int x = packet.readLEShortA();
		int id = packet.readLEShort();
		boolean forceRun = packet.readByte() == 1;
		int packetId = packet.getOpcode();
		
		Optional<GameObject> optional = player.getRegion().findAnyGameObject(id, x, y, player.getLocation().getPlane(), -1);
		if (!optional.isPresent()) {
			return;
		}
		GameObject object = optional.get();
		InteractionOption option = getOption(packetId);
		if (option == null) {
			throw new IllegalStateException("Unexpected packet id " + packetId + ", could not find option.");
		}
		if (option != InteractionOption.EXAMINE) {
			player.getWalkingQueue().reset(forceRun);
			player.getManager().getEvents().executeEvent(player, new NodeReachEvent(new NodeReachEventContext(object, () -> player.getManager().getEvents().executeEvent(player, new ObjectEvent(new ObjectEventContext(object, option))))));
		} else {
			// TODO object examines
			player.getTransmitter().sendMessage("Examining: [" +object.getDefinitions().getName() + ": " + id + ", [" + x + ", " + y + "], " + object.getType() + ", " + object.getRotation(), true);
		}
	}
	
	/**
	 * Gets the option that the player clicked by the packet id
	 *
	 * @param packetId
	 * 		The packet id
	 */
	private InteractionOption getOption(int packetId) {
		switch (packetId) {
			case 1:
				return InteractionOption.FIRST_OPTION;
			case 39:
				return InteractionOption.SECOND_OPTION;
			case 86:
				return InteractionOption.THIRD_OPTION;
			case 58:
				return InteractionOption.FOURTH_OPTION;
			case 38:
				return InteractionOption.FIFTH_OPTION;
			case 75:
				return InteractionOption.EXAMINE;
			default:
				return null;
		}
	}
}
