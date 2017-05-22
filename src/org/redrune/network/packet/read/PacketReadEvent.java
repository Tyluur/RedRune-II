package org.redrune.network.packet.read;

import org.redrune.network.stream.IoReadEvent;
import org.redrune.rs2.node.entity.player.Player;

public interface PacketReadEvent {

	public void decodePacket(Player player, IoReadEvent packet);

}
