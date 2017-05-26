package org.redrune.network.rs666.packet.structure.out;

import org.redrune.cache.parse.MapRegionParser;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.world.map.Location;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.MapDataParser;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class MapRegionBuilder implements OutgoingPacketStructure {
	
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
		for (int sectorX = (regionX - 6) >> 3; sectorX <= (regionX + 6) >> 3; sectorX++) {
			for (int sectorY = (regionY - 6) >> 3; sectorY <= (regionY + 6) >> 3; sectorY++) {
				int region = sectorY | (sectorX << 8);
				int[] mapData = MapDataParser.getMapData().get(region);
				if (mapData == null) {
					mapData = new int[4];
				}
				for (int i = 0; i < 4; i++) {
					bldr.writeInt(mapData[i]);
				}
				MapRegionParser.parseMap(region, mapData);
			}
		}
		player.getDetails().setLastLocation(player.getLocation());
		player.putAttribute(AttributeKey.MAP_REGION_CHANGED, false);
		return bldr.toPacket();
	}
	
}
