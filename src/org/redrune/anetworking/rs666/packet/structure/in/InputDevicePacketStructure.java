package org.redrune.network.rs666.packet.structure.in;

import org.redrune.anetworking.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;
import org.redrune.utility.io.BufferUtils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class InputDevicePacketStructure implements IncomingPacketStructure {
	
	/**
	 * The opcode for mouse clicking
	 */
	private static final int MOUSE_CLICK = 36;
	
	/**
	 * Thet opcode for mouse motions
	 */
	private static final int MOUSE_MOTION = 88;
	
	/**
	 * The opcode for keyboard typing
	 */
	private static final int KEYPRESS = 89;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(88, 36, 89);
	}
	
	@Override
	@SuppressWarnings("unused")
	public void read(Player player, Packet packet) {
		if (packet.getOpcode() == MOUSE_CLICK) {
			int clickData = packet.readLEShortA();
			int timePassed = clickData & 0x7FFF;
			boolean leftClick = (clickData >> 15) == 0;
			int positionData = packet.readLEInt();
			int screenClickX = positionData >> 16;
			int screenClickY = positionData & 0xFFFF;
		} else if (packet.getOpcode() == MOUSE_MOTION) {
			packet.readByte();
		} else if (packet.getOpcode() == KEYPRESS) {
			int byte1 = packet.readByte();
			int idk2 = packet.readByte();
			int idk3 = packet.readShort();
			
			System.out.println(idk2 + ", " + idk3);
			System.out.println(BufferUtils.readableBytes(packet.getBuffer()));
		}
	}
}
