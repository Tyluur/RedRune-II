package org.redrune.networking.codec.packet.impl;

import org.redrune.game.content.action.impl.PlayerCombatAction;
import org.redrune.game.content.action.impl.PlayerFollowAction;
import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.constants.AttributeKey;
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
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				if (player.getLocks().isInteractionLocked() || !player.getControllerManager().canEntityClick(p2, FIRST)) {
					return;
				}
				if (!player.isCanPvp()) {
					return;
				}
				if (!player.getControllerManager().canAttack(p2)) {
					return;
				}
				
				if (!player.isCanPvp() || !p2.isCanPvp()) {
					player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
					return;
				}
				if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
						player.getPackets().sendGameMessage("You are already in combat.");
						return;
					}
					if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Misc.currentTimeMillis()) {
						if (p2.getAttackedBy() instanceof NPC) {
							p2.setAttackedBy(player); // changes enemy to player,
							// player has priority over
							// npc on single areas
						} else {
							player.getPackets().sendGameMessage("That player is already in combat.");
							return;
						}
					}
				}
				player.stopAll(false);
				player.getActionManager().setAction(new PlayerCombatAction(p2));
				break;
			}
			case PLAYER_OPTION_2_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()) || player.getLocks().isInteractionLocked()) {
					return;
				}
				player.stopAll(false);
				player.getActionManager().setAction(new PlayerFollowAction(p2));
				break;
			}
			case ACCEPT_TRADE_CHAT_PACKET:
			case PLAYER_TRADE_OPTION_PACKET: {
				stream.readByte();
				int playerIndex = stream.readUnsignedShort();
				final Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				player.setRouteEvent(new RouteEvent(p2, () -> {
					player.setNextFaceActor(p2);
					if (p2.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("The other player is busy.");
						return;
					}
					if (!p2.withinDistance(player, 14)) {
						player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
						return;
					}
					if (p2.getAttribute(AttributeKey.TRADE_TARGET) == player) {
						p2.removeAttribute(AttributeKey.TRADE_TARGET);
						player.getTradeManager().openTrade(p2);
						p2.getTradeManager().openTrade(player);
						return;
					}
					player.setNextFaceWorldTile(p2);
					player.putAttribute(AttributeKey.TRADE_TARGET, p2);
					player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
					p2.getPackets().sendTradeRequestMessage(player);
				}));
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
				@SuppressWarnings("unused") int junk1 = stream.readUnsignedShort();
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
				player.stopAll(false);
				switch (interfaceId) {
					case PlayerInventory.INVENTORY_INTERFACE:
						Item item = player.getInventory().getItem(junk1);
						if (item == null) {
							return;
						}
						if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
							return;
						}
						if (!player.getControllerManager().processItemOnPlayer(p2, item)) {
							return;
						}
						break;
					case 662:
					case 747:
						if (player.getFamiliar() == null) {
							return;
						}
						player.resetWalkSteps();
						if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17) {
							if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 23 || interfaceId == 747 && componentId == 17)) {
								if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
									return;
								}
							}
							if (!player.isCanPvp() || !p2.isCanPvp()) {
								player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
								return;
							}
							if (!player.getFamiliar().canAttack(p2)) {
								player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
								return;
							} else {
								player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
								player.getFamiliar().setTarget(p2);
							}
						}
						break;
					case 193:
					case 192:
						switch (componentId) {
							case 25: // air strike
							case 28: // water strike
							case 30: // earth strike
							case 32: // fire strike
							case 34: // air bolt
							case 42: // earth bolt
							case 45: // fire bolt
							case 49: // air blast
							case 52: // water blast
							case 58: // earth blast
							case 63: // fire blast
							case 70: // air wave
							case 73: // water wave
							case 77: // earth wave
							case 80: // fire wave
							case 84: // air surge
							case 87: // water surge
							case 89: // earth surge
							case 66: // Sara Strike
							case 67: // Guthix Claws
							case 68: // Flame of Zammy
							case 93:
							case 91: // fire surge
							case 99: // storm of Armadyl
							case 55: // snare
							case 81: // entangle
							case 24:
							case 20:
							case 26:
							case 22:
							case 29:
							case 33:
							case 21:
							case 31:
							case 35:
							case 27:
							case 23:
							case 75:
							case 78:
							case 82:
							case 86: // teleblock
							case 36: // bind
							case 37:
							case 38:
							case 39: // water bolt
								if (CombatAlgorithm.checkCombatSpell(player, componentId, 1, false)) {
									player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
									if (!player.getControllerManager().canAttack(p2)) {
										return;
									}
									if (!player.isCanPvp() || !p2.isCanPvp()) {
										player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
										return;
									}
									if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
										if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
											player.getPackets().sendGameMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
											return;
										}
										if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Misc.currentTimeMillis()) {
											if (p2.getAttackedBy() instanceof NPC) {
												p2.setAttackedBy(player);
											} else {
												player.getPackets().sendGameMessage("That player is already in combat.");
												return;
											}
										}
									}
									player.getActionManager().setAction(new PlayerCombatAction(p2));
								}
								break;
						}
						break;
				}
				break;
			}
		}
	}
}
