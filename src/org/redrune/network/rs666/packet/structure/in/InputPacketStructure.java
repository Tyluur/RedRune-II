package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.input.InputResponse;
import org.redrune.network.rs666.packet.input.InputType;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class InputPacketStructure implements IncomingPacketStructure {
	
	/**
	 * The packet id for integer input
	 */
	private static final int INPUT_INTEGER = 15;
	
	/**
	 * The packet id for string input
	 */
	private static final int INPUT_STRING = 59;
	
	/**
	 * The packet id for long string input
	 */
	private static final int INPUT_LONG_STRING = 82;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(INPUT_INTEGER, INPUT_STRING, INPUT_LONG_STRING);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		InputType type = packet.getOpcode() == INPUT_INTEGER ? InputType.INTEGER : packet.getOpcode() == INPUT_STRING ? InputType.NAME : InputType.LONG_TEXT;
		InputResponse response = player.getAttribute("input_" + type.name().toLowerCase());
		if (response == null) {
			System.out.println("Expected to receive input (type=" + type + "), with no attribute stored.");
			return;
		}
		switch (packet.getOpcode()) {
			case INPUT_INTEGER:
				int numbers = packet.readInt();
				response.run(String.valueOf(numbers));
				break;
			case INPUT_STRING:
			case INPUT_LONG_STRING:
				String text = packet.readRS2String();
				response.run(text);
				break;
		}
	}
}
