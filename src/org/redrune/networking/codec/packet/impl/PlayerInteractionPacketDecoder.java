package org.redrune.networking.codec.packet.impl;

import org.redrune.game.content.entity.actor.player.event.player.PlayerAttackEvent;
import org.redrune.game.content.entity.actor.player.event.player.PlayerFollowEvent;
import org.redrune.game.content.entity.actor.player.event.player.PlayerMagicCastEvent;
import org.redrune.game.content.entity.actor.player.event.player.PlayerTradeEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.game.ClickOption.FIRST;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class PlayerInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(ACCEPT_TRADE_CHAT_PACKET, PLAYER_TRADE_OPTION_PACKET, PLAYER_OPTION_1_PACKET, PLAYER_OPTION_2_PACKET, INTERFACE_ON_PLAYER);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case PLAYER_OPTION_1_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player target = World.getPlayers().get(playerIndex);
				if (target == null || target.isDead() || target.hasFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					return;
				}
				if (player.getLocks().isInteractionLocked() || !player.getControllerManager().canEntityClick(target, FIRST) || !player.isCanPvp()) {
					return;
				}
				player.setNextFaceActor(target);
				if (!player.getControllerManager().canAttack(target)) {
					return;
				}
				player.getEventManager().start(new PlayerAttackEvent(target));
				break;
			}
			case PLAYER_OPTION_2_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player target = World.getPlayers().get(playerIndex);
				// null and online checks individually
				if (target == null || target.isDead() || target.hasFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					return;
				}
				// game verification checks
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				player.getEventManager().start(new PlayerFollowEvent(target));
				break;
			}
			case ACCEPT_TRADE_CHAT_PACKET:
			case PLAYER_TRADE_OPTION_PACKET: {
				stream.readByte();
				int playerIndex = stream.readUnsignedShort();
				final Player target = World.getPlayers().get(playerIndex);
				if (target == null || target.isDead() || target.hasFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				player.getEventManager().start(new PlayerTradeEvent(target));
				break;
			}
			case INTERFACE_ON_PLAYER: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				int playerIndex = stream.readUnsignedShortLE();
				int interfaceHash = stream.readIntLE();
				@SuppressWarnings("unused") int slotId = stream.readUnsignedShort();
				@SuppressWarnings("unused") boolean unknown = stream.read128Byte() == 1;
				@SuppressWarnings("unused") int junk2 = stream.readUnsignedShortLE128();
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
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				player.getEventManager().start(new PlayerMagicCastEvent(p2, interfaceId, componentId, slotId));
				break;
			}
		}
	}
}
