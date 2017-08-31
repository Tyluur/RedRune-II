package com.rs.networking.codec.decode;

import com.rs.game.content.Commands;
import com.rs.game.content.Magic;
import com.rs.game.content.SkillCapeCustomizer;
import com.rs.game.content.Trade;
import com.rs.game.content.Trade.TradeState;
import com.rs.game.content.action.impl.PlayerCombatAction;
import com.rs.game.content.action.impl.PlayerFollowAction;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Inventory;
import com.rs.game.entity.actor.player.data.RouteEvent;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.entity.actor.player.link.FriendChatsManager;
import com.rs.game.entity.item.FloorItem;
import com.rs.game.entity.item.Item;
import com.rs.game.world.World;
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
import com.rs.utility.game.item.ItemExamines;
import com.rs.utility.game.player.PublicChatMessage;
import com.rs.utility.game.player.QuickChatMessage;

public final class WorldPacketsDecoder extends Decoder {
	
	private static final byte[] PACKET_SIZES = new byte[256];
	
	private final static int ACCEPT_TRADE_CHAT_PACKET = 46;
	
	private final static int PLAYER_TRADE_OPTION_PACKET = 77;
	
	private final static int WALKING_PACKET = 12;
	
	private final static int MINI_WALKING_PACKET = 83;
	
	//public final static int AFK_PACKET = 85;
	// private final static int AFK_PACKET = 93; ?
	public final static int ACTION_BUTTON1_PACKET = 61;
	
	public final static int ACTION_BUTTON2_PACKET = 64;
	
	public final static int ACTION_BUTTON3_PACKET = 4;
	
	public final static int ACTION_BUTTON4_PACKET = 52;
	
	public final static int ACTION_BUTTON5_PACKET = 81;
	
	public final static int ACTION_BUTTON6_PACKET = 18;
	
	public final static int ACTION_BUTTON7_PACKET = 10;
	
	public final static int ACTION_BUTTON8_PACKET = 25;
	
	public final static int ACTION_BUTTON9_PACKET = 91;
	
	public final static int ACTION_BUTTON10_PACKET = 20;
	
	public final static int RECEIVE_PACKET_COUNT_PACKET = 15;
	
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	
	private final static int MOVE_CAMERA_PACKET = 5;
	
	private final static int MAGIC_ON_GROUND_PACKET = -1;
	
	private final static int CLICK_PACKET = 84;
	
	private final static int MOUVE_MOUSE_PACKET = 29;
	
	private final static int KEY_TYPED_PACKET = 68;
	
	private final static int CLOSE_INTERFACE_PACKET = 56;
	
	private final static int COMMANDS_PACKET = 70;
	
	private final static int ITEM_ON_ITEM_PACKET = 73;
	
	private final static int IN_OUT_SCREEN_PACKET = 75;
	
	private final static int SWITCH_DETAIL = 4;
	
	private final static int DONE_LOADING_REGION = 33;
	
	private final static int PING_PACKET = 16;
	
	private final static int SCREEN_PACKET = 87;
	
	private final static int CHAT_TYPE_PACKET = 23;
	
	private final static int CHAT_PACKET = 36;
	
	private final static int PUBLIC_QUICK_CHAT_PACKET = 30;
	
	private final static int ADD_FRIEND_PACKET = 51;
	
	private final static int JOIN_FRIEND_CHAT_PACKET = 1;
	
	private final static int CHANGE_FRIEND_CHAT_PACKET = 41;
	
	private final static int KICK_FRIEND_CHAT_PACKET = 32;
	
	private final static int REMOVE_FRIEND_PACKET = 8;
	
	private final static int SEND_FRIEND_MESSAGE_PACKET = 72;
	
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 79;
	
	private final static int OBJECT_CLICK1_PACKET = 11;
	
	private final static int OBJECT_CLICK2_PACKET = 2;
	
	private final static int OBJECT_CLICK3_PACKET = 76;
	
	private final static int OBJECT_EXAMINE_PACKET = 47;
	
	private final static int NPC_CLICK1_PACKET = 9;
	
	private final static int NPC_CLICK2_PACKET = 31;
	
	private final static int ATTACK_NPC = 66;
	
	private final static int REPORT_ABUSE_PACKET = 11;
	
	private final static int PLAYER_OPTION_1_PACKET = 14;
	
	private final static int PLAYER_OPTION_2_PACKET = 53;
	
	private final static int ITEM_TAKE_PACKET = 24;
	
	private final static int DIALOGUE_CONTINUE_PACKET = 54;
	
	private final static int ENTER_INTEGER_PACKET = 3;
	
	private final static int ENTER_STRING_PACKET = 59;
	
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 26;
	
	private final static int INTERFACE_ON_PLAYER = 40;
	
	private final static int INTERFACE_ON_NPC = 65;
	
	private final static int ITEM_ON_OBJECT_PACKET = 42;
	
	private final static int NPC_EXAMINE_PACKET = 92;
	
	private final static int ITEM_EXAMINE_PACKET = 27;
	
	private final static int COLOR_ID_PACKET = 22;
	
	// private final static int AFK_CLIENT_PACKET = 93;
	private final static int CLAN_NAME_PACKET = 7;
	
	private final static int CLAN_FORUM_THREAD_PACKET = 74;
	
	private static final int WORLD_LIST_REQUEST_PACKET = 34;
	
	static {
		loadPacketSizes();
	}
	
	public static void loadPacketSizes() {
		for (int id = 0; id < 256; id++) {
			PACKET_SIZES[id] = -4;
		}
		PACKET_SIZES[64] = 8;
		PACKET_SIZES[18] = 8;
		PACKET_SIZES[25] = 8;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[14] = 3;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[87] = 6;
		PACKET_SIZES[47] = 7;
		PACKET_SIZES[57] = 3;
		PACKET_SIZES[67] = 3;
		PACKET_SIZES[91] = 8;
		PACKET_SIZES[24] = 7;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[40] = 11;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[54] = 6;
		PACKET_SIZES[12] = 5;
		PACKET_SIZES[23] = 1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[17] = -1;
		PACKET_SIZES[44] = -1;
		PACKET_SIZES[88] = -1;
		PACKET_SIZES[42] = 17;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[21] = 15;
		PACKET_SIZES[59] = -1;
		PACKET_SIZES[37] = -1;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[55] = 7;
		PACKET_SIZES[69] = 9;
		PACKET_SIZES[26] = 16;
		PACKET_SIZES[39] = 12;
		PACKET_SIZES[71] = 4;
		PACKET_SIZES[22] = 2;
		PACKET_SIZES[32] = -1;
		PACKET_SIZES[79] = -1;
		PACKET_SIZES[89] = 4;
		PACKET_SIZES[90] = -1;
		PACKET_SIZES[15] = 4;
		PACKET_SIZES[72] = -2;
		PACKET_SIZES[20] = 8;
		PACKET_SIZES[92] = 3;
		PACKET_SIZES[82] = 3;
		PACKET_SIZES[28] = 3;
		PACKET_SIZES[81] = 8;
		PACKET_SIZES[7] = -1;
		PACKET_SIZES[4] = 8;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[13] = 2;
		PACKET_SIZES[52] = 8;
		PACKET_SIZES[65] = 11;
		PACKET_SIZES[85] = 2;
		PACKET_SIZES[86] = 7;
		PACKET_SIZES[78] = -1;
		PACKET_SIZES[83] = 18;
		PACKET_SIZES[27] = 7;
		PACKET_SIZES[2] = 7;
		PACKET_SIZES[93] = 1;
		PACKET_SIZES[70] = -1;
		PACKET_SIZES[1] = -1;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[11] = 7;
		PACKET_SIZES[0] = 9;
		PACKET_SIZES[51] = -1;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[75] = 4;
		PACKET_SIZES[53] = 3;
		PACKET_SIZES[33] = 0;
		PACKET_SIZES[50] = 3;
		PACKET_SIZES[76] = 7;
		PACKET_SIZES[80] = -1;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[68] = -1;
		PACKET_SIZES[43] = 3;
		PACKET_SIZES[30] = -1;
		PACKET_SIZES[19] = 3;
		PACKET_SIZES[16] = 0;
		PACKET_SIZES[34] = 4;
		PACKET_SIZES[48] = 0;
		PACKET_SIZES[56] = 0;
		PACKET_SIZES[58] = 2;
		PACKET_SIZES[10] = 8;
		PACKET_SIZES[35] = 7;
		PACKET_SIZES[84] = 6;
		PACKET_SIZES[66] = 3;
		PACKET_SIZES[61] = 8;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[62] = 3;
		PACKET_SIZES[3] = 4;
		PACKET_SIZES[63] = 4;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[38] = -1;
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
			} else if (length == -3) {
				length = stream.readInt();
			} else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				// break;
			}
			
			// System.out.println("PacketId " +packetId+
			// " has . - expected size " +length);
			
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	
	public void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(Misc.currentTimeMillis());
		switch (packetId) {
			case PING_PACKET:
				// kk we ping :)
				break;
			case MOUVE_MOUSE_PACKET:
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
			case PLAYER_TRADE_OPTION_PACKET: {
				long currentTime = System.currentTimeMillis();
				boolean unknown2 = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player other = World.getPlayers().get(playerIndex);
				if (player.getTrade() != null || other.getTrade() != null) {
					player.getPackets().sendGameMessage("You're already in a trade!");
					return;
				}
				if (player.getX() == other.getX() && player.getY() == other.getY()) {
					player.sendMessage("You can't trade in this position.");
					other.sendMessage("You can't trade in this position.");
					return;
				}
				if (other.getTemporaryAttributtes().get("didRequestTrade") == Boolean.TRUE && (Integer) other.getTemporaryAttributtes().get("tradeWithIndex") == player.getIndex()) {
					Trade session = new Trade(player, other, player);
					player.setTrade(session);
					other.setTrade(session);
					session.start();
					if (player.getX() == other.getX() && player.getY() == other.getY()) {
						player.sendMessage("You can't trade in this position.");
						player.getTrade().endSession();
						
						return;
					}
				} else {
					if (player.getX() == other.getX() && player.getY() == other.getY()) {
						player.sendMessage("You can't trade in this position.");
						return;
					}
					player.getPackets().sendGameMessage("Sending trade request...");
					other.getPackets().sendTradeRequestMessage(player);
					player.getTemporaryAttributtes().put("didRequestTrade", Boolean.TRUE);
					player.getTemporaryAttributtes().put("tradeWithIndex", other.getIndex());
					player.stopAll(false);
					
				}
				
				break;
			}
			case ACCEPT_TRADE_CHAT_PACKET: {
				long currentTime = System.currentTimeMillis();
				boolean unknown2 = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				Player other = World.getPlayers().get(playerIndex);
				if (player.getTrade() != null || other.getTrade() != null) {
					player.getPackets().sendGameMessage("You're already in a trade!");
				}
				if (player.getX() == other.getX() && player.getY() == other.getY()) {
					player.sendMessage("You can't trade in this position.");
					other.sendMessage("You can't trade in this position.");
					return;
				}
				if (other.getTemporaryAttributtes().get("didRequestTrade") == Boolean.TRUE && (Integer) other.getTemporaryAttributtes().get("tradeWithIndex") == player.getIndex()) {
					Trade session = new Trade(player, other, player);
					player.setTrade(session);
					other.setTrade(session);
					session.start();
					if (player.getX() == other.getX() && player.getY() == other.getY()) {
						player.sendMessage("You can't trade in this position.");
						other.sendMessage("You can't trade in this position.");
						player.getTrade().endSession();
						other.getTrade().endSession();
					}
				} else {
					if (player.getX() == other.getX() && player.getY() == other.getY()) {
						player.sendMessage("You can't trade in this position.");
						other.sendMessage("You can't trade in this position.");
						return;
					}
					player.getPackets().sendGameMessage("Sending trade request...");
					other.getPackets().sendTradeRequestMessage(player);
					player.getTemporaryAttributtes().put("didRequestTrade", Boolean.TRUE);
					player.getTemporaryAttributtes().put("tradeWithIndex", other.getIndex());
				}
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
				ButtonHandler.handleButtons(player, stream, packetId);
				break;
			case ENTER_STRING_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				String value = stream.readString();
				if (player.getInterfaceManager().containsInterface(1108)) {
					player.getFriendsIgnores().setChatPrefix(value);
				}
				break;
			}
			case ENTER_INTEGER_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				int value = stream.readInt();
				if ((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(763)) || player.getInterfaceManager().containsInterface(11)) {
					if (value < 0) {
						return;
					}
					Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
					if (bank_item_X_Slot == null) {
						return;
					}
					if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null) {
						player.getBank().withdrawItem(bank_item_X_Slot, value);
					} else {
						player.getBank().depositItem(bank_item_X_Slot, value, !player.getInterfaceManager().containsInterface(11));
					}
				} else if (player.getInterfaceManager().containsInterface(206) && player.getInterfaceManager().containsInterface(207)) {
					if (value < 0) {
						return;
					}
					Integer pc_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("pc_item_X_Slot");
					if (pc_item_X_Slot == null) {
						return;
					}
					if (player.getTemporaryAttributtes().remove("pc_isRemove") != null) {
						player.getPriceCheckManager().removeItem(pc_item_X_Slot, value);
					} else {
						player.getPriceCheckManager().addItem(pc_item_X_Slot, value);
					}
				} else if (player.getInterfaceManager().containsInterface(671) && player.getInterfaceManager().containsInterface(665)) {
					if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
						return;
					}
					if (value < 0) {
						return;
					}
					Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
					if (bob_item_X_Slot == null) {
						return;
					}
					if (player.getTemporaryAttributtes().remove("bob_isRemove") != null) {
						player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
					} else {
						player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
					}
				} else if (player.getTemporaryAttributtes().get("skillId") != null) {
					int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
					if (skillId == Skills.HITPOINTS && value == 1) {
						value = 10;
					} else if (value < 1) {
						value = 1;
					} else if (value > 99) {
						value = 99;
					}
					player.getSkills().set(skillId, value);
					player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
					player.getAppearence().generateAppearenceData();
					player.getDialogueManager().finishDialogue();
				} else if (player.getTemporaryAttributtes().get("offerX") != null && player.getInterfaceManager().containsInterface(335) && player.getTrade().getState() == TradeState.STATE_ONE) {
					player.getTrade().addItem(player, (Integer) player.getTemporaryAttributtes().get("offerX"), value);
					player.getTemporaryAttributtes().remove("offerX");
				} else if (player.getTemporaryAttributtes().get("removeX") != null && player.getInterfaceManager().containsInterface(335) && player.getTrade().getState() == TradeState.STATE_ONE) {
					player.getTrade().removeItem(player, (Integer) player.getTemporaryAttributtes().get("removeX"), value);
					player.getTemporaryAttributtes().remove("removeX");
					
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
				if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
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
				player.refreshSpawnedObjects();
				player.refreshSpawnedItems();
				break;
			case OBJECT_CLICK1_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, 1);
				break;
			case OBJECT_CLICK2_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, 2);
				break;
			case OBJECT_CLICK3_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, 3);
				break;
			case ITEM_ON_OBJECT_PACKET:
				ObjectHandler.handleItemOnObject(player, stream);
				break;
			case OBJECT_EXAMINE_PACKET:
				ObjectHandler.decodeObjectStream(player, stream, 10);
				break;
			case WALKING_PACKET:
			case MINI_WALKING_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				long currentTime = Misc.currentTimeMillis();
				if (player.getLockDelay() > currentTime) {
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
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				long currentTime = Misc.currentTimeMillis();
				if (player.getLockDelay() > currentTime || player.isFrozen()) {
					return;
				}
				final int id = stream.readUnsignedShort128();
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
				int y = stream.readUnsignedShort();
				int x = stream.readUnsignedShortLE();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				if (!player.getMapRegionsIds().contains(regionId)) {
					return;
				}
				final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
				if (item == null) {
					return;
				}
				player.stopAll(false);
				player.setRouteEvent(new RouteEvent(tile, () -> {
					final FloorItem item1 = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item1 == null) {
						return;
					}
					player.setNextFaceWorldTile(tile);
					World.removeGroundItem(player, item1);
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
				if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				if (player.getLockDelay() > Misc.currentTimeMillis()) {
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
				if (player.getLockDelay() > Misc.currentTimeMillis() || !player.getControlerManager().canPlayerOption1(p2)) {
					return;
				}
				if (!player.isCanPvp()) {
					return;
				}
				if (!player.getControlerManager().canAttack(p2)) {
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
				if (player.getLockDelay() > Misc.currentTimeMillis()) {
					return;
				}
				@SuppressWarnings("unused") boolean unknown = stream.readByte128() == 1;
				int npcIndex = stream.readUnsignedShort128();
				NPC npc = World.getNPCs().get(npcIndex);
				System.out.println("" + npc.getId());
				if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
					return;
				}
				if (!player.getControlerManager().canAttack(npc)) {
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
				if (player.getLockDelay() > Misc.currentTimeMillis()) {
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
						switch (componentId) {
							case 28:
							case 32:
							case 24:
							case 20:
							case 30:
							case 34:
							case 26:
							case 22:
							case 29:
							case 33:
							case 25:
							case 21:
							case 31:
							case 35:
							case 27:
							case 23:
								if (Magic.checkCombatSpell(player, componentId, 1, false)) {
									player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
									if (!player.getControlerManager().canAttack(p2)) {
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
												p2.setAttackedBy(player); // changes enemy
												// to player,
												// player has
												// priority over
												// npc on single
												// areas
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
					case 192:
						switch (componentId) {
							case 25: // air strike
							case 28: // water strike
							case 30: // earth strike
							case 32: // fire strike
							case 34: // air bolt
							case 39: // water bolt
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
							case 86: // teleblock
							case 84: // air surge
							case 87: // water surge
							case 89: // earth surge
							case 91: // fire surge
							case 99: // storm of armadyl
							case 36: // bind
							case 66: // Sara Strike
							case 67: // Guthix Claws
							case 68: // Flame of Zammy
							case 55: // snare
							case 81: // entangle
								if (Magic.checkCombatSpell(player, componentId, 1, false)) {
									player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
									if (!player.getControlerManager().canAttack(p2)) {
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
												p2.setAttackedBy(player); // changes enemy
												// to player,
												// player has
												// priority over
												// npc on single
												// areas
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
				System.out.println("Spell:" + componentId);
				break;
			}
			case INTERFACE_ON_NPC: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				if (player.getLockDelay() > Misc.currentTimeMillis()) {
					return;
				}
				int slot = stream.readUnsignedShortLE128();
				if (slot == 65535) {
					return;
				}
				@SuppressWarnings("unused") int junk2 = stream.readUnsignedShortLE();
				int npcIndex = stream.readUnsignedShortLE();
				int interfaceHash = stream.readIntV2();
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;
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
					case Inventory.INVENTORY_INTERFACE:
						Item item = player.getInventory().getItem(slot);// construct
						// only if
						// needed
						if (item == null) {
							return;
						}
						if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
							return;
						}
						if (!player.getControlerManager().processItemOnNPC(npc, item)) {
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
					case 193:
						switch (componentId) {
							case 28:
							case 32:
							case 24:
							case 20:
							case 30:
							case 34:
							case 26:
							case 22:
							case 29:
							case 33:
							case 25:
							case 21:
							case 31:
							case 35:
							case 27:
							case 23:
								if (Magic.checkCombatSpell(player, componentId, 1, false)) {
									player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
									if (!player.getControlerManager().canAttack(npc)) {
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
					case 192:
						switch (componentId) {
							case 25: // air strike
							case 28: // water strike
							case 30: // earth strike
							case 32: // fire strike
							case 34: // air bolt
							case 39: // water bolt
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
							case 36: // bind
							case 55: // snare
							case 81: // entangle
								if (Magic.checkCombatSpell(player, componentId, 1, false)) {
									player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
									if (!player.getControlerManager().canAttack(npc)) {
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
				NPCHandler.handleOption1(player, stream);
				break;
			case NPC_CLICK2_PACKET:
				NPCHandler.handleOption2(player, stream);
				break;
			case NPC_EXAMINE_PACKET:
				NPCHandler.handleExamine(player, stream);
				break;
			case CLAN_NAME_PACKET:
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
				final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
				player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
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
				player.setLastPublicMessage(Misc.currentTimeMillis() + 1000); // avoids
				
				// message
				// appearing
				player.kickPlayerFromFriendsChannel(stream.readString());
				break;
			case CHANGE_FRIEND_CHAT_PACKET:
				if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108)) {
					return;
				}
				player.getFriendsIgnores().changeRank(stream.readString(), stream.readUnsignedByteC());
				break;
			case ADD_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getFriendsIgnores().addFriend(stream.readString());
				break;
			case REMOVE_FRIEND_PACKET:
				if (!player.hasStarted()) {
					return;
				}
				player.getFriendsIgnores().removeFriend(stream.readString());
				break;
			case SEND_FRIEND_MESSAGE_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				if (player.getMuted() > Misc.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You're account has been muted.");
					player.getPackets().sendGameMessage("Read our Terms and Conditions for more information.");
					if (!player.isCanPvp()) {
						player.getInterfaceManager().sendInterface(801);
					}
					return;
				}
				String username = stream.readString();
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					return;
				}
				player.getFriendsIgnores().sendMessage(p2, Misc.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
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
				player.getFriendsIgnores().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
				break;
			}
			case PUBLIC_QUICK_CHAT_PACKET: {
				if (!player.hasStarted()) {
					return;
				}
				if (player.getLastPublicMessage() > Misc.currentTimeMillis()) {
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
				}
				System.out.println("Unknown chat type: " + chatType);
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
				if (message.startsWith("::") || message.startsWith(";")) {
					if (Commands.processCommand(player, message.replace("::", "").replace(";", ""), false, false)) {
						return;
					}
					return;
				}
				if (player.getMuted() > Misc.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You're account has been muted.");
					player.getPackets().sendGameMessage("Read our Terms and Conditions for more information.");
					if (!player.isCanPvp()) {
						player.getInterfaceManager().sendInterface(801);
					}
					return;
				}
				int effects = (colorEffect << 8) | (moveEffect & 0xff);
				if (chatType == 1) {
					player.sendFriendsChannelMessage(Misc.fixChatMessage(message));
				} else {
					player.sendPublicChatMessage(new PublicChatMessage(Misc.fixChatMessage(message), effects));
				}
				System.out.println("Chat type: " + chatType);
				break;
			case COMMANDS_PACKET: {
				if (!player.isRunning()) {
					return;
				}
				boolean clientCommand = stream.readUnsignedByte() == 1;
				@SuppressWarnings("unused") boolean unknown = stream.readUnsignedByte() == 1;
				String command = stream.readString();
				Commands.processCommand(player, command, true, clientCommand);
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
			default:
				System.out.println("Missing packet " + packetId + ", expected size: " + length + ", actual size: " + PACKET_SIZES[packetId]);
				break;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
