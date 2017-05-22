package org.redrune.network.packet.read.impl.rsinterface;

import org.redrune.rs2.node.entity.player.Player;

public interface RSInterface {

	public void handleInterface(Player player, int interfaceId, int buttonId, int slotId, int itemId, int packetId);

	public int[] getPossibleInterfaces();

}
