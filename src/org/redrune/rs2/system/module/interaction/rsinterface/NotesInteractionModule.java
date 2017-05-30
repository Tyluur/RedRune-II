package org.redrune.rs2.system.module.interaction.rsinterface;

import org.redrune.network.NetworkConstants;
import org.redrune.network.rs666.packet.input.InputType;
import org.redrune.network.rs666.packet.structure.out.InterfaceChangeBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.system.module.type.InterfaceInteractionModule;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class NotesInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(34);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (componentId) {
			case 35:
			case 37:
			case 39:
			case 41:
				player.getManager().getNotes().colour((componentId - 35) / 2);
				player.getTransmitter().send(new InterfaceChangeBuilder(34, 16, true).build(player));
				break;
			case 3:
				player.getTransmitter().requestInput(input -> player.getManager().getNotes().add(input), InputType.LONG_TEXT, "Add note:");
				break;
			case 9:
				switch (packetId) {
					case NetworkConstants.FIRST_PACKET_ID:
						if (player.getManager().getNotes().getCurrentNote() == slotId) {
							player.getManager().getNotes().removeCurrentNote();
						} else {
							player.getManager().getNotes().setCurrentNote(slotId);
						}
						break;
					case NetworkConstants.SECOND_PACKET_ID:
						player.getManager().getNotes().setCurrentNote(slotId);
						player.getTransmitter().requestInput(input -> player.getManager().getNotes().edit(input), InputType.LONG_TEXT, "Edit note:");
						break;
					case NetworkConstants.THIRD_PACKET_ID:
						player.getManager().getNotes().setCurrentNote(slotId);
						player.getTransmitter().send(new InterfaceChangeBuilder(34, 16, false).build(player));
						break;
					case NetworkConstants.LAST_PACKET_ID:
						player.getManager().getNotes().delete(slotId);
						break;
				}
				break;
			case 11:
				switch (packetId) {
					case NetworkConstants.FIRST_PACKET_ID:
						player.getManager().getNotes().delete();
						break;
					case NetworkConstants.SECOND_PACKET_ID:
						player.getManager().getNotes().deleteAll();
						break;
				}
				break;
		}
		return true;
	}
}
