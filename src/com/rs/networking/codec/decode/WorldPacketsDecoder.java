package com.rs.networking.codec.decode;

import com.rs.game.content.SkillCapeCustomizer;
import com.rs.game.content.action.impl.PlayerCombatAction;
import com.rs.game.content.action.impl.PlayerFollowAction;
import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerInventory;
import com.rs.game.entity.actor.player.data.RouteEvent;
import com.rs.game.entity.actor.player.link.FriendChatsManager;
import com.rs.game.entity.item.FloorItem;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.world.World;
import com.rs.game.world.punishment.PunishmentRepository;
import com.rs.game.world.punishment.PunishmentType;
import com.rs.game.world.region.RegionManager;
import com.rs.game.world.route.RouteFinder;
import com.rs.game.world.route.strategy.FixedTileStrategy;
import com.rs.networking.Session;
import com.rs.networking.codec.Decoder;
import com.rs.networking.codec.decode.handlers.ButtonHandler;
import com.rs.networking.codec.decode.handlers.InventoryOptionsHandler;
import com.rs.networking.codec.decode.handlers.NPCHandler;
import com.rs.networking.codec.decode.handlers.ObjectHandler;
import com.rs.networking.io.InputStream;
import com.rs.utility.Misc;
import com.rs.utility.cache.huffman.Huffman;
import com.rs.utility.constants.AttributeKey;
import com.rs.utility.game.InputEvent;
import com.rs.utility.game.player.PublicChatMessage;
import com.rs.utility.game.player.QuickChatMessage;
import com.rs.utility.repo.item.ItemCharacteristicRepository;

import static com.rs.utility.constants.PacketConstants.*;
import static com.rs.utility.game.ClickOption.*;

public final class WorldPacketsDecoder extends Decoder {
	
	static {
		loadPacketSizes();
	}
	
	private Player player;
	
	private int chatType;
	
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if (packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1) {
				length = stream.readUnsignedByte();
			} else if (length == -2) {
				length = stream.readUnsignedShort();
			} else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Unregistered packet size for packet # " + packetId + " - size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("Packet # " + packetId + " has fake size - expected size " + length);
			}
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	
	private void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(Misc.currentTimeMillis());
		switch (packetId) {
			case PING_PACKET:
				// kk we ping :)
				break;
			case MOVE_MOUSE_PACKET:
				// USELESS PACKET
				break;
			case KEY_TYPED_PACKET:
				// USELESS PACKET
				break;
			case RECEIVE_PACKET_COUNT_PACKET:
				// interface packets
				stream.readInt();
				break;
			case ITEM_ON_ITEM_PACKET:
				InventoryOptionsHandler.handleItemOnItem(player, stream);
				break;
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
			case MAGIC_ON_GROUND_PACKET: {
				int inventoryInter = stream.readInt() >> 16;
				int itemId = stream.readShort();
				int junk = stream.readShort();
				int itemSlot = stream.readShortLE();
				int interfaceSet = stream.readIntV1();
				int spellId = interfaceSet & 0xFFF;
				int magicInter = interfaceSet >> 16;
				System.out.println("Item:" + itemId + "slot:" + itemSlot + "spell:" + spellId + "i:" + interfaceSet + "l:" + magicInter + "x:" + junk + "k:" + inventoryInter);
				break;
			}
			/*case MAGIC_ON_ITEM_PACKET: {
				int inventoryInter = stream.readInt() >> 16;
				int itemId = stream.readShort128();
				@SuppressWarnings("unused") int junk = stream.readShort();
				@SuppressWarnings("unused") int itemSlot = stream.readShortLE();
				int interfaceSet = stream.readIntV1();
				int spellId = interfaceSet & 0xFFF;
				int magicInter = interfaceSet >> 16;
				if (inventoryInter == 149 && magicInter == 192) {
					switch (spellId) {
						case 59:// High Alch
							if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
								player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
								return;
							}
							if (itemId == 995) {
								player.getPackets().sendGameMessage("You can't alch this!");
								return;
							}
							if (player.getEquipment().getWeaponId() == 1401 || player.getEquipment().getWeaponId() == 3054 || player.getEquipment().getWeaponId() == 19323) {
								if (!player.getInventory().containsItem(561, 1)) {
									player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
									return;
								}
								player.setNextAnimation(new Animation(9633));
								player.setNextGraphics(new Graphics(112));
								player.getInventory().deleteItem(561, 1);
								player.getInventory().deleteItem(itemId, 1);
								player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue(itemId) >> 6);
							} else {
								if (!player.getInventory().containsItem(561, 1) || !player.getInventory().containsItem(554, 5)) {
									player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
									return;
								}
								player.setNextAnimation(new Animation(713));
								player.setNextGraphics(new Graphics(113));
								player.getInventory().deleteItem(561, 1);
								player.getInventory().deleteItem(554, 5);
								player.getInventory().deleteItem(itemId, 1);
								player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue(itemId) >> 6);
							}
							break;
						default:
							System.out.println("Spell:" + spellId + ", Item:" + itemId);
					}
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
				}
				break;
			}*/
			case CLOSE_INTERFACE_PACKET:
				if (!player.isRunning()) {
					player.run();
					return;
				}
				player.stopAll();
				break;
			case MOVE_CAMERA_PACKET:
				// not using it atm
				stream.readUnsignedShort();
				stream.readUnsignedShort();
				break;
			case IN_OUT_SCREEN_PACKET:
				// not using this check because not 100% efficient
				@SuppressWarnings("unused") boolean inScreen = stream.readByte() == 1;
				break;
			case SCREEN_PACKET:
				int displayMode = stream.readUnsignedByte();
				player.setScreenWidth(stream.readUnsignedShort());
				player.setScreenHeight(stream.readUnsignedShort());
				@SuppressWarnings("unused") boolean switchScreenMode = stream.readUnsignedByte() == 1;
				if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742)) {
					return;
				}
				player.setDisplayMode(displayMode);
				player.getInterfaceManager().removeAll();
				player.getInterfaceManager().sendInterfaces();
				player.getInterfaceManager().sendInterface(742);
				break;
			case CLICK_PACKET: {
				int mouseHash = stream.readShortLE128();
				int mouseButton = mouseHash >> 15;
				int time = mouseHash - (mouseButton << 15); // time
				
				int positionHash = stream.readIntV1();
				int y = positionHash >> 16; // y;
				
				int x = positionHash - (y << 16); // x
				
				@SuppressWarnings("unused") boolean clicked;
				// mass click or stupid autoclicker, lets stop lagg
				if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
					// player.getSession().getChannel().close();
					clicked = false;
					return;
				}
				clicked = true;
				break;
			}
			case DIALOGUE_CONTINUE_PACKET: {
				int interfaceHash = stream.readIntV2();
				@SuppressWarnings("unused") int junk = stream.readShortLE128();
				int interfaceId = interfaceHash >> 16;
				@SuppressWarnings("unused") int buttonId = (interfaceHash & 0xFF);
				if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					// hack, or server error or client error
					// player.getSession().getChannel().close();
					return;
				}
				if (!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId)) {
					return;
				}
				int componentId = interfaceHash - (interfaceId << 16);
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			}
			case ACTION_BUTTON1_PACKET:
			case ACTION_BUTTON2_PACKET:
			case ACTION_BUTTON4_PACKET:
			case ACTION_BUTTON5_PACKET:
			case ACTION_BUTTON6_PACKET:
			case ACTION_BUTTON7_PACKET:
			case ACTION_BUTTON8_PACKET:
			case ACTION_BUTTON3_PACKET:
			case ACTION_BUTTON9_PACKET:
			case ACTION_BUTTON10_PACKET:
				ButtonHandler.decodeInterfaceStream(player, stream, packetId);
				break;
			case ENTER_STRING_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				String value = stream.readString();
				if (value.equals("")) {
					return;
				}
				if (player.getAttribute("input_event", null) != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
					return;
				}
				if (player.getInterfaceManager().containsInterface(1108)) {
					player.getContactManager().setChatPrefix(value);
				}
				break;
			}
			case ENTER_LONG_STRING_PACKET: {
				String value = stream.readString();
				if (value.equals("")) {
					return;
				}
				if (player.getAttribute("input_event") != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
				}
				break;
			}
			case ENTER_INTEGER_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				int value = stream.readInt();
				if (player.getAttribute("input_event") != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
					return;
				}
				break;
			}
			case SWITCH_INTERFACE_ITEM_PACKET:
				stream.readUnsignedShort();
				int fromSlot = stream.readUnsignedShortLE();
				stream.readUnsignedShort128();
				int interface1Hash = stream.readIntV1();
				int toSlot = stream.readUnsignedShortLE();
				int interface2Hash = stream.readIntV2();
				
				int fromInterfaceId = interface1Hash >> 16;
				int fromComponentId = interface1Hash - (fromInterfaceId << 16);
				
				int toInterfaceId = interface2Hash >> 16;
				int toComponentId = interface2Hash - (toInterfaceId << 16);
				
				if (Misc.getInterfaceDefinitionsSize() <= fromInterfaceId || Misc.getInterfaceDefinitionsSize() <= toInterfaceId) {
					return;
				}
				if (!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager().containsInterface(toInterfaceId)) {
					return;
				}
				if (fromComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId) {
					return;
				}
				if (toComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId) {
					return;
				}
				if (fromInterfaceId == PlayerInventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == PlayerInventory.INVENTORY_INTERFACE && toComponentId == 0) {
					toSlot -= 28;
					if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize()) {
						return;
					}
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
					if (toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize()) {
						return;
					}
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
					player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
				}
				System.out.println("Switch item " + fromInterfaceId + ", " + fromSlot + ", " + toSlot);
				break;
			case DONE_LOADING_REGION:
				if (!player.clientHasLoadedMapRegion()) {
					player.setClientHasLoadedMapRegion();
				}
				player.getPacketSender().refreshSpawnedObjects();
				player.getPacketSender().refreshSpawnedItems();
				break;
			case OBJECT_CLICK1_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, FIRST);
				break;
			case OBJECT_CLICK2_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, SECOND);
				break;
			case OBJECT_CLICK3_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, THIRD);
				break;
			case OBJECT_EXAMINE_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, EXAMINE);
				break;
			case ITEM_ON_OBJECT_PACKET:
				ObjectHandler.handleItemOnObject(player, stream);
				break;
			case WALKING_PACKET:
			case MINI_WALKING_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				long currentTime = Misc.currentTimeMillis();
				if (player.getLocks().isMovementLocked()) {
					return;
				}
				if (player.getFreezeDelay() >= currentTime) {
					player.getPackets().sendGameMessage("A magical force prevents you from moving.");
					return;
				}
				// the x to walk to
				int destX = stream.readUnsignedShortLE128();
				// the y to walk to
				int destY = stream.readUnsignedShortLE128();
				// if the player should force running
				boolean forceRun = stream.readByte() == 1;
				player.stopAll();
				// forces the new run flag
				if (forceRun) {
					player.setRun(true);
				}
				// calculates the amount of steps in the path
				int calculatedSteps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), new FixedTileStrategy(destX, destY), true);
				// the buffer with the x steps
				int[] bufferX = RouteFinder.getLastPathBufferX();
				// the buffer with they steps
				int[] bufferY = RouteFinder.getLastPathBufferY();
				
				// adds walk steps to the movement queue
				int last = -1;
				for (int i = calculatedSteps - 1; i >= 0; i--) {
					if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
						break;
					}
					last = i;
				}
				
				// sends destination on the minimap
				if (last != -1) {
					WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
					player.getPackets().sendMinimapFlag(tile.getLocalX(player.getLastLoadedMapRegionTile()), tile.getLocalY(player.getLastLoadedMapRegionTile()));
				} else {
					player.getPackets().sendResetMinimapFlag();
				}
			}
			break;
			case ITEM_TAKE_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isFrozen()) {
					return;
				}
				final int id = stream.readUnsignedShort128();
				boolean forceRun = stream.readByte() == 1;
				int y = stream.readUnsignedShort();
				int x = stream.readUnsignedShortLE();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				if (player.getLocks().isInteractionLocked() || !player.getMapRegionsIds().contains(regionId)) {
					return;
				}
				final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
				if (item == null) {
					return;
				}
				if (forceRun) {
					player.setRun(true);
				}
				player.stopAll(false);
				player.setRouteEvent(new RouteEvent(item, () -> {
					final FloorItem item1 = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
					if (item1 == null) {
						return;
					}
					player.setNextFaceWorldTile(tile);
					RegionManager.removeGroundItem(player, item1);
				}, true));
			}
			break;
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
			case ATTACK_NPC: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				if (player.getLocks().isInteractionLocked()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte128() == 1;
				int npcIndex = stream.readUnsignedShort128();
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
					return;
				}
				if (!player.getControllerManager().canAttack(npc)) {
					return;
				}
				if (npc instanceof Familiar) {
					Familiar familiar = (Familiar) npc;
					if (familiar == player.getFamiliar()) {
						player.getPackets().sendGameMessage("You can't attack your own familiar.");
						return;
					}
					if (!familiar.canAttack(player)) {
						player.getPackets().sendGameMessage("You can't attack this npc.");
						return;
					}
				} else if (!npc.isForceMultiAttacked()) {
					if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
						if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
							player.getPackets().sendGameMessage("You are already in combat.");
							return;
						}
						if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
							player.getPackets().sendGameMessage("This npc is already in combat.");
							return;
						}
					}
				}
				player.stopAll(false);
				player.getActionManager().setAction(new PlayerCombatAction(npc));
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
			case INTERFACE_ON_NPC: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
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
				if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
					return;
				}
				if (!npc.getDefinitions().hasAttackOption()) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
				player.stopAll(false);
				switch (interfaceId) {
					case PlayerInventory.INVENTORY_INTERFACE:
						Item item = player.getInventory().getItem(slot);
						if (item == null) {
							return;
						}
						if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
							return;
						}
						if (!player.getControllerManager().processItemOnNPC(npc, item)) {
							return;
						}
						// InventoryOptionsHandler.handleItemOnNPC(npc, item);
						break;
					case 662:
					case 747:
						if (player.getFamiliar() == null) {
							return;
						}
						player.resetWalkSteps();
						if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17 || interfaceId == 747 && componentId == 23) {
							if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17)) {
								if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
									return;
								}
							}
							if (npc == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!player.getFamiliar().canAttack(npc)) {
								player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
								return;
							} else {
								player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
								player.getFamiliar().setTarget(npc);
							}
						}
						break;
					case 192:
					case 193:
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
									player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
									if (!player.getControllerManager().canAttack(npc)) {
										return;
									}
									if (npc instanceof Familiar) {
										Familiar familiar = (Familiar) npc;
										if (familiar == player.getFamiliar()) {
											player.getPackets().sendGameMessage("You can't attack your own familiar.");
											return;
										}
										if (!familiar.canAttack(player)) {
											player.getPackets().sendGameMessage("You can't attack this npc.");
											return;
										}
									} else if (!npc.isForceMultiAttacked()) {
										if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
											if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
												player.getPackets().sendGameMessage("You are already in combat.");
												return;
											}
											if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
												player.getPackets().sendGameMessage("This npc is already in combat.");
												return;
											}
										}
									}
									player.getActionManager().setAction(new PlayerCombatAction(npc));
								}
								break;
						}
						break;
				}
				System.out.println("Spell:" + componentId);
				break;
			}
			case NPC_CLICK1_PACKET:
				NPCHandler.decodeNPCStream(player, stream, FIRST);
				break;
			case NPC_CLICK2_PACKET:
				NPCHandler.decodeNPCStream(player, stream, SECOND);
				break;
			case NPC_CLICK3_PACKET:
				NPCHandler.decodeNPCStream(player, stream, THIRD);
				break;
			case NPC_CLICK4_PACKET:
				NPCHandler.decodeNPCStream(player, stream, FOURTH);
				break;
			case NPC_EXAMINE_PACKET:
				NPCHandler.decodeNPCStream(player, stream, EXAMINE);
				break;
			case CLAN_FORUM_THREAD_PACKET:
				break;
			case ITEM_EXAMINE_PACKET: {
				final int id = stream.readUnsignedShort128();
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;// Dont delete this.
				
				int y = stream.readUnsignedShort();
				int x = stream.readUnsignedShortLE();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
				player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item.getId()));
				break;
			}
			case JOIN_FRIEND_CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				FriendChatsManager.joinChat(stream.readString(), player);
				break;
			case KICK_FRIEND_CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.setLastPublicMessage(Misc.currentTimeMillis() + 1000);
				player.kickPlayerFromFriendsChannel(stream.readString());
				break;
			case CHANGE_FRIEND_CHAT_PACKET:
				if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108)) {
					return;
				}
				player.getContactManager().changeRank(stream.readString(), stream.readUnsignedByteC());
				break;
			case ADD_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getContactManager().addFriend(stream.readString());
				break;
			case REMOVE_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getContactManager().removeFriend(stream.readString());
				break;
			case SEND_FRIEND_MESSAGE_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				String username = stream.readString();
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					return;
				}
				player.getContactManager().sendMessage(p2, Misc.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
				break;
			}
			case SEND_FRIEND_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				String username = stream.readString();
				int fileId = stream.readUnsignedShort();
				byte[] data = null;
				if (length > 3 + username.length()) {
					data = new byte[length - (3 + username.length())];
					stream.readBytes(data);
				}
				data = Misc.completeQuickMessage(player, fileId, data);
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					return;
				}
				player.getContactManager().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
				break;
			}
			case PUBLIC_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				if (player.getLastPublicMessage() > Misc.currentTimeMillis()) {
					return;
				}
				if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
					player.getPackets().sendGameMessage("You are muted.");
					return;
				}
				player.setLastPublicMessage(Misc.currentTimeMillis() + 300);
				// just tells you which client script created packet
				@SuppressWarnings("unused") boolean secondClientScript = stream.readByte() == 1;// script 5059
				
				// or 5061
				int fileId = stream.readUnsignedShort();
				byte[] data = null;
				if (length > 3) {
					data = new byte[length - 3];
					stream.readBytes(data);
				}
				data = Misc.completeQuickMessage(player, fileId, data);
				if (chatType == 0) {
					player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
				} else if (chatType == 1) {
					player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
				} else {
					System.out.println("Unknown chat type: " + chatType);
				}
				break;
			}
			case CHAT_TYPE_PACKET:
				chatType = stream.readUnsignedByte();
				break;
			case CHAT_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				if (player.getLastPublicMessage() > Misc.currentTimeMillis()) {
					return;
				}
				
				player.setLastPublicMessage(Misc.currentTimeMillis() + 300);
				int colorEffect = stream.readUnsignedByte();
				int moveEffect = stream.readUnsignedByte();
				String message = Huffman.readEncryptedMessage(250, stream);
				if (message == null || message.replaceAll(" ", "").equals("")) {
					return;
				}
				if (message.startsWith("::")) {
					PluginRepository.handleCommand(player, message.replaceFirst("::", "").split(" "), false, false);
					return;
				}
				if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_MUTE, PunishmentType.ADDRESS_MUTE)) {
					player.getPackets().sendGameMessage("You are muted.");
					return;
				}
				int effects = (colorEffect << 8) | (moveEffect & 0xff);
				if (chatType == 1) {
					player.sendFriendsChannelMessage(Misc.fixChatMessage(message));
				} else {
					player.sendPublicChatMessage(new PublicChatMessage(Misc.fixChatMessage(message), effects));
				}
				break;
			case COMMANDS_PACKET: {
				if (!player.isRunning()) {
					return;
				}
				boolean clientCommand = stream.readUnsignedByte() == 1;
				@SuppressWarnings("unused") boolean unknown = stream.readUnsignedByte() == 1;
				String command = stream.readString();
				PluginRepository.handleCommand(player, command.replaceFirst("::", "").split(" "), true, clientCommand);
				break;
			}
			case COLOR_ID_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				int colorId = stream.readUnsignedShort();
				if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null) {
					SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
				}
				break;
			case WORLD_LIST_REQUEST_PACKET:
				int updateType = stream.readInt();
				player.getPackets().sendWorldList(updateType == 0);
				break;
			case WINDOW_SWITCH_PACKET:
				boolean dominant = stream.readByte() == 1;
				break;
			default:
				System.out.println("Missing packet " + packetId + ", expected size: " + length + ", actual size: " + PACKET_SIZES[packetId]);
				break;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
