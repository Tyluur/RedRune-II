package org.redrune.networking.codec.packet.impl;

import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.event.npc.NPCAttackEvent;
import org.redrune.game.content.entity.actor.player.event.npc.NPCInteractionEvent;
import org.redrune.game.content.entity.actor.player.event.npc.NPCMagicCastEvent;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.ClickOption;
import org.redrune.utility.game.repository.npc.characteristic.NPCCharacteristicRepository;
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository;

import static org.redrune.utility.game.ClickOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class NPCInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(NPC_CLICK1_PACKET, NPC_CLICK2_PACKET, NPC_CLICK3_PACKET, NPC_CLICK4_PACKET, NPC_EXAMINE_PACKET, ATTACK_NPC, INTERFACE_ON_NPC);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case NPC_CLICK1_PACKET:
				decodeNPCStream(player, stream, FIRST);
				break;
			case NPC_CLICK2_PACKET:
				decodeNPCStream(player, stream, SECOND);
				break;
			case NPC_CLICK3_PACKET:
				decodeNPCStream(player, stream, THIRD);
				break;
			case NPC_CLICK4_PACKET:
				decodeNPCStream(player, stream, FOURTH);
				break;
			case NPC_EXAMINE_PACKET:
				decodeNPCStream(player, stream, EXAMINE);
				break;
			case ATTACK_NPC: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte128() == 1;
				int npcIndex = stream.readUnsignedShort128();
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
					return;
				}
				player.setNextFaceActor(npc);
				if (!player.getControllerManager().canAttack(npc)) {
					return;
				}
				player.getEventManager().start(new NPCAttackEvent(npc));
				break;
			}
			case INTERFACE_ON_NPC: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				int slot = stream.readUnsignedShortLE128();
				stream.readUnsignedShortLE();
				int npcIndex = stream.readUnsignedShortLE();
				int interfaceHash = stream.readIntV2();
				stream.readByte();
				int interfaceId = interfaceHash >> 16;
				int componentId = interfaceHash - (interfaceId << 16);
				
				if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					return;
				}
				if (!player.getInterfaceManager().containsInterface(interfaceId)) {
					return;
				}
				if (componentId == 65535) {
					componentId = -1;
				}
				if (componentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
					return;
				}
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
					return;
				}
				player.getEventManager().start(new NPCMagicCastEvent(npc, interfaceId, componentId, slot));
			}
		}
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
	private static void decodeNPCStream(final Player player, InputStream stream, ClickOption option) {
		boolean running = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || player.getLocks().isInteractionLocked()) {
			return;
		}
		if (running) {
			player.setRunModeOn(true);
		}
		switch (option) {
			case FIRST:
			case SECOND:
			case THIRD:
			case FOURTH:
				player.getEventManager().start(new NPCInteractionEvent(npc, option));
				break;
			case EXAMINE:
				if (player.getTemporaryAttribute("removing_npcs", false)) {
					NPCSpawnRepository.removeSpawn(npc);
					npc.finish();
					return;
				}
				player.getPackets().sendNPCMessage(0, npc, NPCCharacteristicRepository.getExamine(npc.getId()));
				if (GameFlags.debugMode) {
					System.out.println("Examined npc [" + npc + "]");
				}
				break;
		}
	}
}
