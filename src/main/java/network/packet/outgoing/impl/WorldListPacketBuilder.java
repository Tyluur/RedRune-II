package network.packet.outgoing.impl;

import utility.global.wordlist.WorldEntry;
import utility.global.wordlist.WorldList;
import network.packet.Packet;
import network.packet.PacketBuilder;
import network.packet.PacketType;
import network.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class WorldListPacketBuilder extends OutgoingPacketBuilder {
	
	/**
	 * If the request is for a full update
	 */
	private final boolean full;
	
	public WorldListPacketBuilder(boolean full) {
		super(new PacketBuilder(88, PacketType.VAR_SHORT));
		this.full = full;
	}
	
	@Override
	public Packet build() {
		bldr.writeByte(1);// This was 0
		bldr.writeByte(2);
		bldr.writeByte(full ? 1 : 0);
		int size = WorldList.INSTANCE.getWorlds().size();
		if (full) {
			bldr.writeSmart(size);
			for (WorldEntry world : WorldList.INSTANCE.getWorlds().values()) {
				bldr.writeSmart(world.getCountryId());
				bldr.writeGJString(world.getCountryName());
			}
			bldr.writeSmart(0);
			bldr.writeSmart(size + 1);
			bldr.writeSmart(size);
			for (int world = 1; world <= WorldList.INSTANCE.getWorlds().size(); world++) {
				bldr.writeSmart(world); // wid
				bldr.writeByte(0); // loc (idx in list) ^ KEEP THIS 0
				bldr.writeInt(WorldList.INSTANCE.getWorlds().get(world).getFlag());
				bldr.writeGJString(WorldList.INSTANCE.getWorlds().get(world).getActivity()); // activity
				bldr.writeGJString(WorldList.INSTANCE.getWorlds().get(world).getIp()); // ip
			}
			bldr.writeInt(0x94DA4A87);
		}
		for (int world = 1; world <= WorldList.INSTANCE.getWorlds().size(); world++) {
			bldr.writeSmart(world); // wid
			bldr.writeShort(1337/*WorldList.INSTANCE.getWorlds().get(world)*/);
		}
		return bldr.toPacket();
	}
}
