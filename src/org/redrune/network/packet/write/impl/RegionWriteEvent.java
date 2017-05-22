package org.redrune.network.packet.write.impl;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.network.packet.event.impl.RegionPacket;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

@PacketHeader(packet = PacketType.VAR_SHORT)
public class RegionWriteEvent implements PacketWriteEvent<RegionPacket> {

	@Override
	public IoWriteEvent encodePacket(RegionPacket context) {
		IoWriteEvent buffer = IoWriteEvent.create(78);
		if (context.isLogin()) {

			context.getPlayer().getPlayerRendering().enterWorld(buffer);
		}
		int chunkX = context.getPlayer().getChunkX();
		int chunkY = context.getPlayer().getChunkY();

		buffer.writeLEShort(chunkY).writeA(context.isLogin() ? 1 : 0).writeShort(chunkX).writeS(0);

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
		for (int i = 0; i < context.getPlayer().getMapRegions().size(); i++) {
			for (int index = 0; index < 4; index++) {
				buffer.writeInt(0);
			}
		}
		return buffer;
	}

}
