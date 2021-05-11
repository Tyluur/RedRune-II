package network.packet.context.impl;

import game.entity.actor.player.Player;
import network.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class RegionLoadPacketContext extends PacketContext {
	
	@Override
	public void handle(Player player) {
		if (!player.getAttributes().clientHasLoadedMapRegion()) {
			player.getAttributes().setClientHasLoadedMapRegion();
		}
		player.getPackets().refreshSpawnedObjects();
		player.getPackets().refreshSpawnedItems();
	}
}
