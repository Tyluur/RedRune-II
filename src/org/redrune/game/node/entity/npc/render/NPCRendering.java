package org.redrune.game.node.entity.npc.render;

import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketStructure;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class NPCRendering implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		// the packet for npc rendering
		PacketBuilder bldr = new PacketBuilder(6, PacketType.VAR_SHORT);
		
		// The update block. Any updates that are pending will be added to this block.
		PacketBuilder updateBlock = new PacketBuilder();
		
		List<NPC> localNpcs = player.getRenderData().getLocalNpcs();
		bldr.startBitAccess();
		bldr.writeBits(8, localNpcs.size());
		for (Iterator<NPC> it$ = localNpcs.iterator(); it$.hasNext(); ) {
			NPC npc = it$.next();
			if (npc.isRenderable() && npc.getLocation().isWithinDistance(player.getLocation())) {
				updateNPCMovement(npc, bldr);
				// Update the npc is required, since it is conditionally valid.
				if (npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
				}
			} else {
				// Signify the client that this npc needs to be removed.
				bldr.writeBits(1, 1);
				bldr.writeBits(2, 3);
				
				it$.remove();
			}
		}
		
		for (Integer regionId : player.getMapRegionsIds()) {
			Region region = RegionManager.getRegion(regionId);
			List<NPC> npcs = region.getNpcs();
			for (NPC npc : npcs) {
				if (localNpcs.size() >= 255) {
					break;
				}
				if (!npc.isRenderable() || !npc.getLocation().isWithinDistance(player.getLocation()) || localNpcs.contains(npc)) {
					continue;
				}
				localNpcs.add(npc);
				addNewNpc(player, npc, bldr);
				if (npc.getUpdateMasks() != null && npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
				}
			}
		}
		bldr.writeBits(15, 32767);
		return bldr.finishBitAccess().writeBytes(updateBlock.getBuffer()).toPacket();
	}
	
	/**
	 * Updates an npcs movement.
	 *
	 * @param npc
	 * 		The npc.
	 * @param buf
	 * 		The buffer.
	 */
	private static void updateNPCMovement(NPC npc, PacketBuilder buf) {
		if (npc.getWalkingQueue().getRunDir() == -1) {
			if (npc.getWalkingQueue().getWalkDir() == -1) {
				if (npc.getUpdateMasks().isUpdateRequired()) {
					buf.writeBits(1, 1);
					buf.writeBits(2, 0);
				} else {
					buf.writeBits(1, 0);
				}
			} else {
				buf.writeBits(1, 1);
				buf.writeBits(2, 1);
				buf.writeBits(3, npc.getWalkingQueue().getWalkDir());
				buf.writeBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
			}
		} else {
			buf.writeBits(1, 1);
			buf.writeBits(2, 2);
			buf.writeBits(1, 1);
			buf.writeBits(3, npc.getWalkingQueue().getWalkDir());
			buf.writeBits(3, npc.getWalkingQueue().getRunDir());
			buf.writeBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
		}
	}
	
	/**
	 * Writes the NPC flag-based updating.
	 *
	 * @param packet
	 * 		The packet to write on.
	 * @param npc
	 * 		The npc.
	 */
	private static void updateNPC(PacketBuilder packet, NPC npc) {
		int maskdata = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<>(npc.getUpdateMasks().getFlagQueue());
		for (UpdateFlag flag : flags) {
			maskdata |= flag.getMaskData();
		}
		if (maskdata > 128) {
			maskdata |= 0x2;
		}
		if (maskdata > 32768) {
			maskdata |= 0x2000;
		}
		packet.writeByte((byte) maskdata);
		if (maskdata > 128) {
			packet.writeByte((byte) (maskdata >> 8));
		}
		if (maskdata > 32768) {
			packet.writeByte((byte) (maskdata >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(packet);
		}
	}
	
	/**
	 * Adds an NPC.
	 *
	 * @param player
	 * 		The player.
	 * @param npc
	 * 		The npc.
	 * @param buf
	 * 		The outgoing packet.
	 */
	private static void addNewNpc(Player player, NPC npc, PacketBuilder buf) {
		try {
			buf.writeBits(15, npc.getIndex());
			buf.writeBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
			buf.writeBits(2, npc.getLocation().getPlane());
			int xDelta = npc.getLocation().getX() - player.getLocation().getX();
			int yDelta = npc.getLocation().getY() - player.getLocation().getY();
			if (xDelta < 0) {
				xDelta += 32;
			}
			if (yDelta < 0) {
				yDelta += 32;
			}
			buf.writeBits(5, xDelta);
			buf.writeBits(1, 0);
			buf.writeBits(3, npc.getFaceDirection());
			buf.writeBits(5, yDelta);
			buf.writeBits(15, npc.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
