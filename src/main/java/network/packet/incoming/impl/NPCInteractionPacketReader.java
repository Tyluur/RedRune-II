package network.packet.incoming.impl;

import game.GameFlags;
import game.content.entity.actor.player.event.npc.NPCAttackEvent;
import game.content.entity.actor.player.event.npc.NPCInterfaceInteractionEvent;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import game.global.World;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.NPCInteractionPacketContext;
import network.packet.incoming.IncomingPacketReader;
import utility.functions.Misc;
import utility.game.ClickOption;
import utility.game.repository.npc.characteristic.NPCCharacteristicRepository;
import utility.game.repository.npc.spawn.NPCSpawnRepository;

import static utility.game.ClickOption.*;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
				decodeNPCExamine(player, packet);
				break;
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
	
	/**
	 * Decodes the examine packet
	 *
	 * @param player
	 * 		The player
	 * @param stream
	 * 		The stream
	 */
	private static void decodeNPCExamine(Player player, Packet stream) {
		boolean running = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || player.getLocks().isInteractionLocked()) {
			return;
		}
		if (player.getTemporaryAttribute("removing_npcs", false)) {
			NPCSpawnRepository.removeSpawn(npc);
			npc.finish();
			return;
		}
		player.getPackets().sendNPCMessage(0, npc, NPCCharacteristicRepository.getExamine(npc.getId()));
		if (GameFlags.debugMode) {
			player.getPackets().sendMessage(npc.toString());
		}
	}
	
}
