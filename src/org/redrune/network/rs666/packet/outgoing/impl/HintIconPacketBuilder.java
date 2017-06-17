package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.HintIcon;
import org.redrune.utility.rs.HintIcon.HintIconType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class HintIconPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The hint icon
	 */
	private final HintIcon icon;
	
	public HintIconPacketBuilder(HintIcon icon) {
		this.icon = icon;
	}
	
	/*
	public void sendHintIcon(HintIcon icon) {
		            OutputStream stream = new OutputStream(13);
		            stream.writePacket(player, 81);
		            stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		            if (icon.getTargetType() == 0) {
		                stream.skip(11);
		            } else {
		                stream.writeByte(icon.getArrowType());
		                if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
		                    stream.writeShort(icon.getTargetIndex());
		                    stream.writeShort(0); // unknown
		                    stream.skip(4);
		                } else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
		                    stream.writeByte(0); // unknown
		                    stream.writeShort(icon.getCoordX());
		                    stream.writeShort(icon.getCoordY());
		                    stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
		                    stream.writeShort(0); // unknown
		                }
		                stream.writeShort(icon.getModelId());
		            }
		            session.write(stream);

		        }
			}
	 */
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(116);
		// icon encoding
		bldr.writeByte((icon.getIconType().getValue() & 0x1f) | (icon.getSlot() << 5));
		// arrow stuff
		bldr.writeByte(icon.getArrowType().getValue());
		
		// the removal overrides
		if (icon.getIconType() == HintIconType.REMOVAL) {
			bldr.skip(11);
		} else if (icon.getLocation() == null) {
			// icon is being sent to an entity
			bldr.writeShort(icon.getTargetIndex());
			// how often the arrow [non-minimap] flashes, [2500 ideal, 0 never]
			bldr.writeShort(2500);
			// skip
			bldr.skip(4);
		} else {
			// unknown
			bldr.writeByte(0);
			// location
			bldr.writeShort(icon.getLocation().getX());
			bldr.writeShort(icon.getLocation().getY());
			// distance from floor
			bldr.writeByte(icon.getFloorDistance() * 4 >> 2);
			// distance to start showing on minimap [0 doesnt show, -1 infinite]
			bldr.writeShort(-1);
		}
		// model stuff
		bldr.writeShort(icon.getModelId());
		return bldr.toPacket();
	}
}
