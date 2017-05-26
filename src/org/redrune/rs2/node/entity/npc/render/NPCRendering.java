package org.redrune.rs2.node.entity.npc.render;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;
import org.redrune.rs2.world.map.Location;
import org.redrune.rs2.world.map.region.RegionManager;

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
		PacketBuilder bldr = new PacketBuilder(6, PacketType.VAR_SHORT);
		writeNPCRendering(player, bldr);
		return bldr.toPacket();
	}
	
	/**
	 * Writes the npc rendering
	 *
	 * @param p
	 * 		The player
	 * @param bldr
	 * 		The packet builder
	 */
	private static void writeNPCRendering(Player p, PacketBuilder bldr) {
		Location playerLocation = p.getLocation();
		PacketBuilder updateBlock = new PacketBuilder();
		List<NPC> localNpcs = p.getRenderData().getLocalNpcs();
		bldr.startBitAccess();
		bldr.writeBits(8, localNpcs.size());
		for (Iterator<NPC> it$ = localNpcs.iterator(); it$.hasNext(); ) {
			NPC npc = it$.next();
			if (npc.isRenderable() && npc.getLocation().isWithinDistance(playerLocation)) {
				updateNPCMovement(npc, bldr);
				if (npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
				}
			} else {
				it$.remove();
				bldr.writeBits(1, 1);
				bldr.writeBits(2, 3);
			}
		}
		for (NPC npc : RegionManager.getLocalNPCs(p.getLocation())) {
			if (localNpcs.size() >= 255) {
				break;
			}
			if (localNpcs.contains(npc) || !npc.isRenderable()) {
				continue;
			}
			addNpc(p, npc, bldr);
			if (npc.getUpdateMasks() != null && npc.getUpdateMasks().isUpdateRequired()) {
				updateNPC(updateBlock, npc);
			}
		}
		bldr.writeBits(15, 32767);
		bldr.finishBitAccess();
		bldr.writeBytes(updateBlock.getBuffer());
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
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<UpdateFlag>(npc.getUpdateMasks().getFlagQueue());
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
	private static void addNpc(Player player, NPC npc, PacketBuilder buf) {
		try {
			int x = npc.getLocation().getX() - player.getLocation().getX();
			int y = npc.getLocation().getY() - player.getLocation().getY();
			if (x < 0) {
				x += 32;
			}
			if (y < 0) {
				y += 32;
			}
			buf.writeBits(15, npc.getIndex());
			buf.writeBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
			buf.writeBits(2, npc.getLocation().getZ());
			buf.writeBits(5, x);
			buf.writeBits(1, 0); // 1
			buf.writeBits(3, npc.getFaceDirection());
			buf.writeBits(5, y);
			buf.writeBits(15, npc.getId());
			player.getRenderData().getLocalNpcs().add(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
