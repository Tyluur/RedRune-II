package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.backend.MapDataParser;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class MapRegionBuilder implements OutgoingPacketBuilder {
	
	/**
	 * If the packet is being sent from a login request
	 */
	private final boolean onLogin;
	
	public MapRegionBuilder(boolean onLogin) {
		this.onLogin = onLogin;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(19, PacketType.VAR_SHORT);
		Location pos = player.getLocation();
		if (onLogin) {
			player.getRenderData().enterWorld(bldr);
		}
		int regionX = pos.getRegionX();
		int regionY = pos.getRegionY();
		bldr.writeByteC(1); //Force refresh? 1 : 0
		bldr.writeLEShort(regionY);
		bldr.writeLEShortA(regionX);
		bldr.writeByteS(0); //Scene graph size index.
		for (int regionId : player.getMapRegionsIds()) {
			int[] keys = MapDataParser.getMapData().get(regionId);
			if (keys == null) {
				keys = new int[4];
			}
			for (int i = 0; i < 4; i++) {
				bldr.writeInt(keys[i]);
			}
			RegionManager.getRegion(regionId).loadLandscape(keys);
		}
		return bldr.toPacket();
	}
	
}
