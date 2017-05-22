package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.components.PlayerAppearance;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;
import org.redrune.utility.Misc;

/**
 * Represents a player's appearance update flag.
 * @author Emperor
 *
 */
public class AppearanceUpdate extends UpdateFlag {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The player's appearance.
	 */
	private final PlayerAppearance appearance;

	/**
	 * Constructs a new {@code AppearanceUpdate} {@code Object}.
	 * @param player The player.
	 */
	public AppearanceUpdate(Player player) {
		this.player = player;
		this.appearance = player.getDetails().getAppearance();
	}

	@Override
	public void write(IoWriteEvent bldr) {
		IoWriteEvent playerUpdate = new IoWriteEvent();
		int bitSet = 0;
		bitSet |= 0x4; //Enable combat colouring.
		if (!appearance.isMale()) {
			bitSet |= 0x1;
		}
		if (appearance.getNpcId() != -1) {
			bitSet |= (NPCDefinitions.getNPCDefinitions(appearance.getNpcId()).getSize() - 1) << 3;
		}
		playerUpdate.write(bitSet);
		playerUpdate.write(1); // title
		playerUpdate.write(-1); //skull icon
		playerUpdate.write(1); //Headicon.
		playerUpdate.write(0);// TODO:refactor
		if (appearance.getNpcId() == -1) {
			for (int i = 0; i < ItemDefinitions.getBodyData().length; i++) {
				if (ItemDefinitions.getBodyData()[i] != 1) {
					int d = appearance.getBodyPart(i);
					if (d == 0) {
						playerUpdate.write(0);
					} else {
						playerUpdate.writeShort((short) d);
					}
				}
			}
			bitSet = 0;
			int part = 0;
			int slotHash = 0;
			for (int i = 0; i < ItemDefinitions.getBodyData().length; i++) {
				if (ItemDefinitions.getBodyData()[i] != 1) {
					/*int itemId = player.getEquipment().get(i) == null ? -1 : player.getEquipment().get(i).getId();
					if (i == 1) {
						if ((itemId == 20767 || itemId == 20769 || itemId == 20771) && player.getCapeRecolouring().isRecolourable(ItemDefinition.getItemDefinition(itemId))) {
							bitSet |= 1 << part;
							slotHash |= 0x1;
						}
					} */
				}
				part++;
			}
			playerUpdate.writeShort(bitSet);
			/*if ((slotHash & 0x1) != 0) { //Only with recolored cape.
				playerUpdate.writeByte(0x4);
				int[] colors = player.getCapeRecolouring().getColours();
				if (colors == null) {
					colors = ItemDefinitionParser.forId(player.getEquipment().getSlotById(Equipment.SLOT_CAPE)).originalModelColors;
				}
				int[] data = { 12816, colors[1], colors[0], colors[3], colors[2]};
				for (int i = 0; i < data.length; i++) {
					playerUpdate.putShort(data[i]);
				}
			}*/
			if (appearance.getBodyPart(14) > 0) { //Only with aura.
				playerUpdate.write(0x1);
				playerUpdate.writeIntSmart(8719);
				playerUpdate.writeIntSmart(8719);
			}
		} else {
			playerUpdate.writeShort(-1);
			playerUpdate.writeShort(appearance.getNpcId());
			playerUpdate.write(0);
		}
		for (byte i = 0; i < 10; i++) {
			playerUpdate.write(appearance.getColor(i));
		}
		playerUpdate.writeShort(appearance.getRenderEmote());
		playerUpdate.writeRS2String(Misc.formatPlayerNameForDisplay(player.getDetails().getUsername()));
		playerUpdate.write(player.getSkills().getCombatLevel());
		playerUpdate.writeShort(0);
		playerUpdate.write(0);
		bldr.write(playerUpdate.getBuffer().writerIndex());
		bldr.writeBytesA(playerUpdate.getBuffer().array(), 0, playerUpdate.getBuffer().writerIndex());
	}

	@Override
	public int getMaskData() {
		return 0x2;
	}

	@Override
	public int getOrdinal() {
		return 6;
	}

}