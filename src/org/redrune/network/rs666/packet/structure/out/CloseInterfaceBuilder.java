package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class CloseInterfaceBuilder implements OutgoingPacketStructure {
	
	/**
	 * The window id.
	 */
	private final int windowId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * Constructs a new close interface builder. The window id is the window the interface is displayed on. Most likely
	 * {@link InterfaceConstants#SCREEN_FIXED_WINDOW_ID}. The child id is the child id
	 * that the interface was displayed on. The actual interface id is irrelevant when closing.
	 *
	 * @param windowId
	 * 		The windowId
	 * @param childId
	 * 		The child if
	 */
	public CloseInterfaceBuilder(int windowId, int childId) {
		this.windowId = windowId;
		this.childId = childId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(29);
		bldr.writeInt2(windowId << 16 | childId);
		return bldr.toPacket();
	}
}
