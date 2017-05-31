package org.redrune.game.node.entity.player.render;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRenderData;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.entity.player.render.update.GlobalUpdateStage;
import org.redrune.game.node.entity.player.render.update.LocalUpdateStage;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.utility.AttributeKey;

import java.util.PriorityQueue;

/**
 * Represents the player rendering outgoing packet.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/17
 */
public class PlayerRendering implements OutgoingPacketStructure {
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(112, PacketType.VAR_SHORT);
		writePlayerRendering(player, bldr);
		return bldr.toPacket();
	}
	
	/**
	 * Writes the player rendering on the packet.
	 *
	 * @param player
	 * 		The player.
	 * @param packet
	 * 		The packet.
	 */
	private static void writePlayerRendering(Player player, PacketBuilder packet) {
		PlayerRenderData info = player.getRenderData();
		int skipCount = -1;
		PacketBuilder flagBased = new PacketBuilder();
		packet.startBitAccess();
		for (int i = 0; i < info.localsCount; i++) {
			int index = info.getLocals()[i];
			LocalUpdateStage stage = LocalUpdateStage.getStage(player, World.get().getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				updateLocalPlayer(player, World.get().getPlayers().get(index), packet, stage, flagBased, index);
			}
		}
		putSkip(skipCount, packet);
		skipCount = -1;
		packet.finishBitAccess();
		packet.startBitAccess();
		for (int i = 0; i < info.globalsCount; i++) {
			int index = info.getGlobals()[i];
			GlobalUpdateStage stage = GlobalUpdateStage.getStage(player, World.get().getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				updateGlobalPlayer(player, World.get().getPlayers().get(index), packet, stage, flagBased);
			}
		}
		putSkip(skipCount, packet);
		skipCount = -1;
		packet.finishBitAccess();
		packet.writeBytes(flagBased.getBuffer());
	}
	
	/**
	 * Updates a local player
	 *
	 * @param player
	 * 		The player we're writing for
	 * @param p
	 * 		The player to update
	 * @param buffer
	 * 		The buffer
	 * @param stage
	 * 		The stage
	 * @param flagBased
	 * 		The flag based buffer
	 * @param index
	 * 		The index
	 */
	private static void updateLocalPlayer(Player player, Player p, PacketBuilder buffer, LocalUpdateStage stage, PacketBuilder flagBased, int index) {
		buffer.writeBits(1, 1);
		buffer.writeBits(1, stage.ordinal() == 0 ? 0 : (p.getUpdateMasks().isUpdateRequired() ? 1 : 0));
		buffer.writeBits(2, stage.ordinal() % 4);
		switch (stage) {
			case REMOVE_PLAYER:
				if (p != null) {
					if (p.getAttribute(AttributeKey.PLAYER_TELEPORTED, false)) {
						updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.TELEPORTED, flagBased);
					} else if (p.getLocation().getPlane() != p.getRenderData().getLastLocation().getPlane()) {
						updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.HEIGHT_UPDATED, flagBased);
					} else {
						buffer.writeBits(1, 0);
					}
				} else {
					buffer.writeBits(1, 0);
				}
				player.getRenderData().getIsLocal()[index] = false;
				break;
			case WALKING:
				//				System.out.println("Writing " + stage + " for " + p + ", walk=" + p.getWalkingQueue().getWalkDir() + ", run=" + p.getWalkingQueue().getRunDir());
				buffer.writeBits(3, p.getWalkingQueue().getWalkDir());
				break;
			case RUNNING:
				//				System.out.println("Writing " + stage + " for " + p + ", walk=" + p.getWalkingQueue().getWalkDir() + ", run=" + p.getWalkingQueue().getRunDir());
				buffer.writeBits(4, p.getWalkingQueue().getRunDir());
				break;
			case TELEPORTED:
				Location delta = Location.getDelta(p.getRenderData().getLastLocation(), p.getLocation());
				int deltaX = delta.getX() < 0 ? -delta.getX() : delta.getX();
				int deltaY = delta.getY() < 0 ? -delta.getY() : delta.getY();
				if (deltaX <= 15 && deltaY <= 15) {
					buffer.writeBits(1, 0);
					int deltaZ;
					deltaX = delta.getX() < 0 ? delta.getX() + 32 : delta.getX();
					deltaY = delta.getY() < 0 ? delta.getY() + 32 : delta.getY();
					deltaZ = delta.getPlane();
					buffer.writeBits(12, (deltaY & 0x1f) | ((deltaX & 0x1f) << 5) | ((deltaZ & 0x3) << 10));
				} else {
					buffer.writeBits(1, 1);
					buffer.writeBits(30, (delta.getY() & 0x3fff) | ((delta.getX() & 0x3fff) << 14) | ((delta.getPlane() & 0x3) << 28));
				}
				break;
			default:
				break;
		}
		if (p != null && stage != LocalUpdateStage.REMOVE_PLAYER && p.getUpdateMasks().isUpdateRequired()) {
			writeMasks(player, p, flagBased, false);
		}
	}
	
	/**
	 * Updates the global player
	 *
	 * @param player
	 * 		The player
	 * @param p
	 * 		The player to write for
	 * @param buffer
	 * 		The bufffer
	 * @param stage
	 * 		The global update stage
	 * @param flagBased
	 * 		The flag based buffer
	 */
	private static void updateGlobalPlayer(Player player, Player p, PacketBuilder buffer, GlobalUpdateStage stage, PacketBuilder flagBased) {
		buffer.writeBits(1, 1);
		buffer.writeBits(2, stage.ordinal());
		switch (stage) {
			case ADD_PLAYER:
				if (p.getRenderData().getLastLocation() != null && p.getLocation().getPlane() != p.getRenderData().getLastLocation().getPlane()) {
					updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.HEIGHT_UPDATED, flagBased);
				} else {
					updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.TELEPORTED, flagBased);
				}
				buffer.writeBits(6, p.getLocation().getX() - (p.getLocation().getRegionX() << 6)); // 6
				buffer.writeBits(6, p.getLocation().getY() - (p.getLocation().getRegionY() << 6)); // 6
				buffer.writeBits(1, 1);
				player.getRenderData().getIsLocal()[p.getIndex()] = true;
				writeMasks(player, p, flagBased, true);
				break;
			case HEIGHT_UPDATED:
				int z = p.getLocation().getPlane() - p.getRenderData().getLastLocation().getPlane();
				buffer.writeBits(2, z);
				break;
			case TELEPORTED:
				buffer.writeBits(18, (p.getLocation().getPlane() << 16) | (((p.getLocation().getRegionX() >> 3) & 0xFF) << 8) | ((p.getLocation().getRegionY() >> 3) & 0xFF));
				break;
			default:
				break;
		}
	}
	
	/**
	 * Writes the update masks for a player on this packet.
	 *
	 * @param writingFor
	 * 		The player we're writing for.
	 * @param updatable
	 * 		The player to update.
	 * @param composer
	 * 		The packet to write on.
	 * @param forceSync
	 * 		If we should force the appearance update mask.
	 */
	private static void writeMasks(Player writingFor, Player updatable, PacketBuilder composer, boolean forceSync) {
		int maskdata = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<>(updatable.getUpdateMasks().getFlagQueue());
		for (UpdateFlag flag : flags) {
			maskdata |= flag.getMaskData();
		}
		if (forceSync && (maskdata & 0x2) == 0) {
			maskdata |= 0x2;
			flags.add(new AppearanceUpdate(updatable));
		}
		if (maskdata > 128) {
			maskdata |= 0x1;
		}
		if (maskdata > 32768) {
			maskdata |= 0x200;
		}
		composer.writeByte((byte) maskdata);
		if (maskdata > 128) {
			composer.writeByte((byte) (maskdata >> 8));
		}
		if (maskdata > 32768) {
			composer.writeByte((byte) (maskdata >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(composer);
		}
	}
	
	/**
	 * Puts the skipcount on the packet.
	 *
	 * @param skipCount
	 * 		The skip count.
	 * @param packet
	 * 		The packet to write on.
	 */
	private static void putSkip(int skipCount, PacketBuilder packet) {
		if (skipCount > -1) {
			packet.writeBits(1, 0);
			if (skipCount == 0) {
				packet.writeBits(2, 0);
			} else if (skipCount < 32) {
				packet.writeBits(2, 1);
				packet.writeBits(5, skipCount);
			} else if (skipCount < 256) {
				packet.writeBits(2, 2);
				packet.writeBits(8, skipCount);
			} else if (skipCount < 2048) {
				packet.writeBits(2, 3);
				packet.writeBits(11, skipCount);
			}
		}
	}
	
}