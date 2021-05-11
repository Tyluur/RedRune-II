package network.packet.incoming.impl;

import game.content.entity.actor.player.event.player.PlayerAttackEvent;
import game.content.entity.actor.player.event.player.PlayerFollowEvent;
import game.content.entity.actor.player.event.player.PlayerInterfaceInteractionEvent;
import game.content.entity.actor.player.event.player.PlayerTradeEvent;
import game.entity.actor.player.Player;
import game.global.World;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.incoming.IncomingPacketReader;
import utility.functions.Misc;

import static utility.game.ClickOption.FIRST;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class PlayerInteractionPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(ACCEPT_TRADE_CHAT_PACKET, PLAYER_TRADE_OPTION_PACKET, PLAYER_OPTION_1_PACKET, PLAYER_OPTION_2_PACKET, INTERFACE_ON_PLAYER);
	}
	
	@Override
	public PacketContext read(Player player, Packet stream) {
		int packetId = stream.getOpcode();
		switch (packetId) {
			case PLAYER_OPTION_1_PACKET: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				final Player target = World.getPlayers().get(playerIndex);
				if (target == null || target.isDead() || target.isFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					break;
				}
				if (player.getLocks().isInteractionLocked() || !player.getControllerManager().canEntityClick(target, FIRST) || !player.getAttributes().isCanPvp()) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.setNextFaceActor(target);
						if (!player.getControllerManager().canAttack(target)) {
							return;
						}
						player.getEventManager().start(new PlayerAttackEvent(target));
					}
				};
			}
			case PLAYER_OPTION_2_PACKET: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player target = World.getPlayers().get(playerIndex);
				// null and online checks individually
				if (target == null || target.isDead() || target.isFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					break;
				}
				// game verification checks
				if (player.getLocks().isInteractionLocked()) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getEventManager().start(new PlayerFollowEvent(target));
					}
				};
			}
			case ACCEPT_TRADE_CHAT_PACKET:
			case PLAYER_TRADE_OPTION_PACKET: {
				stream.readByte();
				int playerIndex = stream.readUnsignedShort();
				final Player target = World.getPlayers().get(playerIndex);
				if (target == null || target.isDead() || target.isFinished() || !player.getMapRegionsIds().contains(target.getRegionId())) {
					break;
				}
				if (player.getLocks().isInteractionLocked()) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getEventManager().start(new PlayerTradeEvent(target));
					}
				};
			}
			case INTERFACE_ON_PLAYER: {
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				if (player.getLocks().isInteractionLocked()) {
					break;
				}
				int playerIndex = stream.readUnsignedShortLE();
				int interfaceHash = stream.readIntLE();
				final int junk1 = stream.readUnsignedShort();
				@SuppressWarnings("unused") boolean unknown = stream.read128Byte() == 1;
				@SuppressWarnings("unused") int slotId = stream.readUnsignedShortLE128();
				final int interfaceId = interfaceHash >> 16;
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
				final int finalComponentId = componentId;
				if (componentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
					break;
				}
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.isFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						player.getEventManager().start(new PlayerInterfaceInteractionEvent(p2, interfaceId, finalComponentId, slotId));
					}
				};
			}
		}
		return null;
	}
}
