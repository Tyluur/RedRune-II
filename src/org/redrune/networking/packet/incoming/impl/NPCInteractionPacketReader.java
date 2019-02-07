package org.redrune.networking.packet.incoming.impl;

import org.redrune.game.content.entity.actor.player.event.npc.NPCAttackEvent;
import org.redrune.game.content.entity.actor.player.event.npc.NPCInterfaceInteractionEvent;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.NPCInteractionPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.ClickOption;

import static org.redrune.utility.game.ClickOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class NPCInteractionPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(NPC_CLICK1_PACKET, NPC_CLICK2_PACKET, NPC_CLICK3_PACKET, NPC_CLICK4_PACKET, NPC_EXAMINE_PACKET, ATTACK_NPC, INTERFACE_ON_NPC);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case NPC_CLICK1_PACKET:
				return decodeNPCStream(player, packet, FIRST);
			case NPC_CLICK2_PACKET:
				return decodeNPCStream(player, packet, SECOND);
			case NPC_CLICK3_PACKET:
				return decodeNPCStream(player, packet, THIRD);
			case NPC_CLICK4_PACKET:
				return decodeNPCStream(player, packet, FOURTH);
			case NPC_EXAMINE_PACKET:
				return decodeNPCStream(player, packet, EXAMINE);
			case ATTACK_NPC: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				if (player.getLocks().isInteractionLocked()) {
					break;
				}
				@SuppressWarnings("unused") boolean unknown = packet.readByte128() == 1;
				int npcIndex = packet.readUnsignedShort128();
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.setNextFaceActor(npc);
						if (!player.getControllerManager().canAttack(npc)) {
							return;
						}
						player.getEventManager().start(new NPCAttackEvent(npc));
					}
				};
			}
			case INTERFACE_ON_NPC: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				if (player.getLocks().isInteractionLocked()) {
					break;
				}
				int slot = packet.readUnsignedShortLE128();
				packet.readUnsignedShortLE();
				int npcIndex = packet.readUnsignedShortLE();
				int interfaceHash = packet.readIntV2();
				packet.readByte();
				int interfaceId = interfaceHash >> 16;
				int componentId = interfaceHash - (interfaceId << 16);
				
				if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					break;
				}
				if (!player.getInterfaceManager().containsInterface(interfaceId)) {
					break;
				}
				if (componentId == 65535) {
					componentId = -1;
				}
				if (componentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
					break;
				}
				final int finalComponentId = componentId;
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getEventManager().start(new NPCInterfaceInteractionEvent(npc, interfaceId, finalComponentId, slot));
					}
				};
			}
		}
		return null;
	}
	
	/**
	 * Decodes the npc stream and passes the npc to the correct handler
	 *
	 * @param player
	 * 		The player
	 * @param stream
	 * 		The stream
	 * @param option
	 * 		The option clicked, used for handler identification
	 */
	private static PacketContext decodeNPCStream(final Player player, Packet stream, ClickOption option) {
		boolean running = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || player.getLocks().isInteractionLocked()) {
			return null;
		}
		return new NPCInteractionPacketContext(npc, option, running);
	}
}
