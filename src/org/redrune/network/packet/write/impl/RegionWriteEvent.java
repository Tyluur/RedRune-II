package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.RegionPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.utility.backend.MapDataParser;

@PacketHeader(packet = PacketType.VAR_SHORT)
public class RegionWriteEvent implements PacketWriteEvent<RegionPacket> {

	@Override
	public IoWriteEvent encodePacket(RegionPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(78);
		if (context.isLogin()) {
			context.getPlayer().getRenderData().enterWorld(buffer);
		}
		int regionX = context.getPlayer().getLocation().getRegionX();
		int regionY = context.getPlayer().getLocation().getRegionY();
		
		buffer.writeByteC(1).writeLEShort(regionY).writeLEShortA(regionX).writeByteS(0);
		
		for (int sectorX = (regionX - 6) >> 3; sectorX <= (regionX + 6) >> 3; sectorX++) {
			for (int sectorY = (regionY - 6) >> 3; sectorY <= (regionY + 6) >> 3; sectorY++) {
				int region = sectorY | (sectorX << 8);
				int[] mapData = MapDataParser.getMapData().get(region);
				if (mapData == null) {
					mapData = new int[4];
				}
				for (int i = 0; i < 4; i++) {
					buffer.writeInt(mapData[i]);
				}
//				MapRegionParser.parseMap(region, mapData);
			}
		}
		
		// for(int regionX = (context.getPlayer().getChunkX() - 6) / 8; regionX
		// <= ((context.getPlayer().getChunkX() + 6) / 8); regionX++) {
		// for(int regionY = (context.getPlayer().getChunkY() - 6) / 8; regionY
		// <= ((context.getPlayer().getChunkY() + 6) / 8); regionY++) {
		// final int regionId = regionY | (regionX << 8);
		// int[] xteaKeys = MapXTEAKeys.getKeys(regionId);
		// if(xteaKeys == null) {
		// xteaKeys = new int[4];
		// }
		// for(int i = 0; i < 4; i++) {
		// buffer.writeInt(xteaKeys[i]);
		// }
		// }
		// }
		//
		return buffer;
	}

}
