package org.redrune.networking.packet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import org.redrune.cache.huffman.Huffman;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemsContainer;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.entity.projectile.Projectile;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.DynamicRegion;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.global.wordlist.WorldEntry;
import org.redrune.global.wordlist.WorldList;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.packet.outgoing.impl.MessagePacketBuilder;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.functions.TextUtils;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.entity.actor.player.ChatMessage;
import org.redrune.utility.game.entity.actor.player.PublicChatMessage;
import org.redrune.utility.game.entity.actor.player.QuickChatMessage;
import org.redrune.utility.game.map.HintIcon;
import org.redrune.utility.game.map.MapArchiveKeys;

import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class PacketSender {
	

	private final Player player;
	
	/**
	 * The instance of the network session
	 */
	private final NetworkSession session;
	
	public PacketSender(Player player) {
		this.player = player;
		this.session = player.getSession();
	}
	
	public void sendRunButtonConfig() {
		player.getPackets().sendConfig(173, player.getAttributes().isResting() ? 3 : player.isRunModeOn() ? 1 : 0);
	}
	
	public void sendProfanityFilterConfig() {
		player.getVarManager().sendVarBit(8780, player.getAttributes().isFilteringProfanity() ? 0 : 1);
	}
	
	public void refreshSpawnedObjects() {
		for (int regionId : player.getMapRegionsIds()) {
			List<WorldObject> removedObjects = RegionManager.getRegion(regionId).getRemovedObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects) {
					player.getPackets().sendDestroyObject(object);
				}
			}
			List<WorldObject> spawnedObjects = RegionManager.getRegion(regionId).getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects) {
					if (object.getPlane() == player.getPlane()) {
						player.getPackets().sendSpawnedObject(object);
					}
				}
			}
		}
	}
	
	public void refreshSpawnedItems() {
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = RegionManager.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && player != item.getOwner() || item.getTile().getPlane() != player.getPlane()) {
					continue;
				}
				player.getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = RegionManager.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && player != item.getOwner() || item.getTile().getPlane() != player.getPlane()) {
					continue;
				}
				player.getPackets().sendGroundItem(item);
			}
		}
	}
	
	public void switchMouseButtons() {
		player.getAttributes().setMouseButtons(!player.getAttributes().isMouseButtons());
		refreshMouseButtons();
	}
	
	public void refreshMouseButtons() {
		player.getPackets().sendConfig(170, player.getAttributes().isMouseButtons() ? 0 : 1);
	}
	
	public void refreshPrivateChatSetup() {
		player.getPackets().sendConfig(287, player.getAttributes().getPrivateChatSetup());
	}
	
	public void sendDefaultPlayersOptions() {
		player.getPackets().sendPlayerOption("Follow", 2, false);
		player.getPackets().sendPlayerOption("Trade with", 3, false);
		//		getPackets().sendPlayerOption("Req Assist", 4, false);
	}
	
	public void switchAllowChatEffects() {
		player.getAttributes().setAllowChatEffects(!player.getAttributes().isAllowChatEffects());
		refreshAllowChatEffects();
	}
	
	public void refreshAllowChatEffects() {
		player.getPackets().sendConfig(171, player.getAttributes().isAllowChatEffects() ? 0 : 1);
	}
	
	public void sendNPCMessage(int border, NPC npc, String message) {
		sendMessage(message);
	}
	
	public void sendMessage(String text) {
		sendMessage(text, false);
	}
	
	public void sendConsoleMessage(String text) {
		sendMessage(99, text, null);
	}
	
	public void sendMessage(String text, boolean filter) {
		sendMessage(filter ? 109 : 0, text, null);
	}
	
	private void sendMessage(int type, String text, Player p) {
		session.write(new MessagePacketBuilder(p, text, type));
	}
	
	public void sendItems(int key, ItemsContainer<Item> items) {
		sendItems(key, key < 0, items);
	}
	
	public void sendItems(int key, boolean keyLessIntegerSize, ItemsContainer<Item> items) {
		sendItems(key, keyLessIntegerSize, items.getItems());
	}
	
	public void sendItems(int key, boolean negativeKey, Item[] items) {
		PacketBuilder stream = new PacketBuilder(37, PacketType.VAR_SHORT);
		stream.writeShort(negativeKey ? key : key);
		stream.writeByte(negativeKey ? 1 : 0);
		stream.writeShort(items.length);
		for (Item item : items) {
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeByte(amount >= 255 ? 255 : amount);
			if (amount >= 255) {
				stream.writeInt(amount);
			}
			stream.writeShortLE(id + 1);
		}
		session.write(stream);
	}
	
	public void sendItems(int key, Item[] items) {
		sendItems(key, key < 0, items);
	}
	
	public void sendPlayerUnderNPCPriority(boolean priority) {
		PacketBuilder stream = new PacketBuilder(123);
		stream.write128Byte(priority ? 1 : 0);
		session.write(stream);
	}
	
	public void sendInterFullScreen(int id, int type) {
		int[] xteas = new int[4];
		player.getInterfaceManager().setWindowsPane(id);
		PacketBuilder stream = new PacketBuilder(39);
		stream.write128Byte(type);
		stream.writeShort128(id);
		stream.writeIntLE(xteas[1]);
		stream.writeIntV2(xteas[0]);
		stream.writeInt(xteas[3]);
		stream.writeInt(xteas[2]);
		session.write(stream);
	}
	
	public void sendHintIcon(HintIcon icon) {
		PacketBuilder stream = new PacketBuilder(81);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0) {
			stream.skip(11);
		} else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(0); // unknown
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(0); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(0); // unknown
			}
			stream.writeShort(icon.getModelId());
		}
		session.write(stream);
		
	}
	
	public void sendCameraShake(int slotId, int b, int c, int d, int e) {
		PacketBuilder stream = new PacketBuilder(34);
		stream.write128Byte(b);
		stream.writeByte128(slotId);
		stream.writeShortLE128(e);
		stream.write128Byte(c);
		stream.write128Byte(d);
		session.write(stream);
	}
	
	public void sendStopCameraShake() {
		PacketBuilder stream = new PacketBuilder(15);
		session.write(stream);
	}
	
	public void sendIComponentModel(int interfaceId, int componentId, int modelId) {
		PacketBuilder stream = new PacketBuilder(58);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.writeShort128(modelId);
		session.write(stream);
	}
	
	public void sendScrollIComponent(int interfaceId, int componentId, int value) {
		PacketBuilder stream = new PacketBuilder(8);
		stream.writeShort128(value);
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);
	}
	
	public void sendHideIComponent(int interfaceId, int componentId, boolean hidden) {
		PacketBuilder stream = new PacketBuilder(117);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.writeByte128(hidden ? 1 : 0);
		session.write(stream);
	}
	
	public void sendRemoveGroundItem(FloorItem item) {
		sendWorldTile(item.getTile());
		int localX = item.getTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = item.getTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		PacketBuilder stream = new PacketBuilder(16);
		stream.writeShort(item.getId());
		stream.writeByte((offsetX << 4) | offsetY);
		session.write(stream);
		
	}
	
	public void sendWorldTile(WorldTile tile) {
		PacketBuilder stream = new PacketBuilder(46);
		stream.writeByte128(tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()) >> 3);
		stream.writeByte(tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()) >> 3);
		stream.write128Byte(tile.getPlane());
		session.write(stream);
	}
	
	public void sendGroundItem(FloorItem item) {
		sendWorldTile(item.getTile());
		int localX = item.getTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = item.getTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		PacketBuilder stream = new PacketBuilder(48);
		stream.writeByteC((offsetX << 4) | offsetY);
		stream.writeShort128(item.getId());
		stream.writeShort(item.getAmount());
		session.write(stream);
	}
	
	public void sendProjectile(Actor receiver, WorldTile startTile, WorldTile endTile, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
		sendWorldTile(startTile);
		PacketBuilder stream = new PacketBuilder(62);
		int localX = startTile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = startTile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 3) | offsetY);
		stream.writeByte(endTile.getX() - startTile.getX());
		stream.writeByte(endTile.getY() - startTile.getY());
		stream.writeShort(receiver == null ? 0 : (receiver instanceof Player ? -(receiver.getIndex() + 1) : receiver.getIndex() + 1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		int duration = (Misc.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1 : (speed / 10))) + delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeShort(creatorSize * 64 + startDistanceOffset * 64);
		session.write(stream);
	}
	
	public void sendProjectile(Projectile projectile) {
		sendWorldTile(projectile.getSourceTile());
		PacketBuilder stream = new PacketBuilder(62);
		WorldTile end = projectile.isLocationBased() ? projectile.getEndLocation() : projectile.getVictim();
		WorldTile start = projectile.getSourceTile();
		int localX = start.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = start.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 3) | offsetY);
		stream.writeByte(end.getX() - start.getX());
		stream.writeByte(end.getY() - start.getY());
		stream.writeShort(projectile.getVictim() != null ? (projectile.getVictim().isPlayer() ? -(projectile.getVictim().getIndex() + 1) : (projectile.getVictim().getIndex() + 1)) : 0);
		stream.writeShort(projectile.getProjectileId());
		stream.writeByte(projectile.getStartHeight());
		stream.writeByte(projectile.getEndHeight());
		stream.writeShort(projectile.getDelay());
		stream.writeShort(projectile.getSpeed());
		stream.writeByte(projectile.getAngle());
		stream.writeShort(projectile.getCreatorSize() * 64 + projectile.getStartDistanceOffset() * 64);
		session.write(stream);
	}
	
	public void sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for (int slot : optionsSlots) {
			settingsHash |= 2 << slot;
		}
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot, settingsHash);
	}
	
	public void sendIComponentSettings(int interfaceId, int componentId, int fromSlot, int toSlot, int settingsHash) {
		PacketBuilder stream = new PacketBuilder(3);
		stream.writeShortLE(fromSlot);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeShort128(toSlot);
		stream.writeIntLE(settingsHash);
		session.write(stream);
	}
	
	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--) {
			parameters[index++] = options[count];
		}
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(150, parameters); // scriptid 150 does that the method
		// name says*/
	}
	
	public void sendRunScript(int scriptId, Object... params) {
		try {
			PacketBuilder stream = new PacketBuilder(50, PacketType.VAR_SHORT);
			StringBuilder parameterTypes = new StringBuilder();
			if (params != null) {
				for (int count = params.length - 1; count >= 0; count--) {
					if (params[count] instanceof String) {
						parameterTypes.append("s"); // string
					} else {
						parameterTypes.append("i"); // integer
					}
				}
			}
			stream.writeString(parameterTypes.toString());
			if (params != null) {
				int index = 0;
				for (int count = parameterTypes.length() - 1; count >= 0; count--) {
					if (parameterTypes.charAt(count) == 's') {
						stream.writeString((String) params[index++]);
					} else {
						stream.writeInt((Integer) params[index++]);
					}
				}
			}
			stream.writeInt(scriptId);
			session.write(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendRunScriptpz(int id, Object[] params, String types) {
		if (params.length != types.length()) {
			return;
		}
		PacketBuilder packet = new PacketBuilder(70, PacketType.VAR_SHORT);
		packet.writeString(types);
		int idx = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's') {
				packet.writeString((String) params[idx]);
			} else {
				packet.writeInt((Integer) params[idx]);
			}
			idx++;
		}
		packet.writeInt(id);
		session.write(packet);
	}
	
	public void sendGlobalConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendGlobalConfig2(id, value);
		} else {
			sendGlobalConfig1(id, value);
		}
	}
	
	public void sendGlobalConfig2(int id, int value) {
		PacketBuilder stream = new PacketBuilder(112);
		stream.writeShortLE(id);
		stream.writeInt(value);
		session.write(stream);
	}
	
	public void sendGlobalConfig1(int id, int value) {
		PacketBuilder stream = new PacketBuilder(111);
		stream.writeShortLE128(id);
		stream.write128Byte(value);
		session.write(stream);
	}
	
	public void sendConfigByFile(int fileId, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendConfigByFile2(fileId, value);
		} else {
			sendConfigByFile1(fileId, value);
		}
	}
	
	public void sendConfigByFile2(int fileId, int value) {
		PacketBuilder stream = new PacketBuilder(84);
		stream.writeInt(value);
		stream.writeShort(fileId);
		session.write(stream);
	}
	
	public void sendConfigByFile1(int fileId, int value) {
		PacketBuilder stream = new PacketBuilder(14);
		stream.write128Byte(value);
		stream.writeShort128(fileId);
		session.write(stream);
	}
	
	public void sendVarp(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendVarp2(id, value);
		} else {
			sendVarp1(id, value);
		}
	}
	
	private void sendVarp2(int id, int value) {
		PacketBuilder stream = new PacketBuilder(56);
		stream.writeShort128(id);
		stream.writeIntLE(value);
		session.write(stream);
	}
	
	private void sendVarp1(int id, int value) {
		PacketBuilder stream = new PacketBuilder(110);
		stream.writeShortLE128(id);
		stream.writeByte128(value);
		session.write(stream);
	}
	
	public void sendVarbit(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendVarbit2(id, value);
		} else {
			sendVarbit1(id, value);
		}
	}
	
	private void sendVarbit2(int id, int value) {
		PacketBuilder stream = new PacketBuilder(81);
		stream.writeIntV1(value);
		stream.writeShort128(id);
		session.write(stream);
	}
	
	private void sendVarbit1(int id, int value) {
		PacketBuilder stream = new PacketBuilder(111);
		stream.writeShort128(id);
		stream.writeByteC(value);
		session.write(stream);
	}
	
	public void sendRunEnergy() {
		PacketBuilder stream = new PacketBuilder(13);
		stream.writeByte(player.getAttributes().getRunEnergy());
		session.write(stream);
	}
	
	public void sendIComponentText(int interfaceId, int componentId, String text) {
		PacketBuilder stream = new PacketBuilder(33, PacketType.VAR_SHORT);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeString(text);
		session.write(stream);
	}
	
	public void sendIComponentAnimation(int emoteId, int interfaceId, int componentId) {
		PacketBuilder stream = new PacketBuilder(23);
		stream.writeShortLE128(emoteId);
		stream.writeIntV1(interfaceId << 16 | componentId);
		session.write(stream);
	}
	
	public void sendItemOnIComponent(int interfaceid, int componentId, int id, int amount) {
		PacketBuilder stream = new PacketBuilder(9);
		stream.writeShortLE(id);
		stream.writeInt(amount);
		stream.writeIntV2(interfaceid << 16 | componentId);
		session.write(stream);
	}
	
	public void sendEntityOnIComponent(boolean isPlayer, int entityId, int interfaceId, int componentId) {
		if (isPlayer) {
			sendPlayerOnIComponent(interfaceId, componentId);
		} else {
			sendNPCOnIComponent(interfaceId, componentId, entityId);
		}
	}
	
	public void sendPlayerOnIComponent(int interfaceId, int componentId) {
		PacketBuilder stream = new PacketBuilder(114);
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);
		
	}
	
	public void sendNPCOnIComponent(int interfaceId, int componentId, int npcId) {
		PacketBuilder stream = new PacketBuilder(98);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShortLE(npcId);
		session.write(stream);
	}
	
	public void sendObjectAnimation(WorldObject object, Animation animation) {
		PacketBuilder stream = new PacketBuilder(96);
		stream.writeIntV2(object.get30BitsLocationHash());
		stream.writeShort128(animation.getIds()[0]);
		stream.write128Byte((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}
	
	public void sendTileMessage(String message, WorldTile tile, int color) {
		sendTileMessage(message, tile, 5000, 255, color);
	}
	
	public void sendTileMessage(String message, WorldTile tile, int delay, int height, int color) {
		sendWorldTile(tile);
		PacketBuilder stream = new PacketBuilder(32, PacketType.VAR_BYTE);
		stream.skip(1);
		int localX = tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 4) | offsetY);
		stream.writeShort(delay / 30);
		stream.writeByte(height);
		stream.write24BitInteger(color);
		stream.writeString(message);
		session.write(stream);
	}
	
	public void sendSpawnedObject(WorldObject object) {
		sendWorldTile(object);
		int localX = object.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = object.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		PacketBuilder stream = new PacketBuilder(28);
		stream.writeByte((offsetX << 4) | offsetY);
		stream.writeByte((object.getType() << 2) + (object.getRotation() & 0x3));
		stream.writeShort128(object.getId());
		session.write(stream);
	}
	
	public void sendDestroyObject(WorldObject object) {
		sendWorldTile(object);
		int localX = object.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = object.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		PacketBuilder stream = new PacketBuilder(45);
		stream.writeByteC((offsetX << 4) | offsetY);
		stream.writeByte((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}
	
	public void sendFriendsChatChannel() {
		FriendChatsManager manager = player.getCurrentFriendChat();
		//		PacketBuilder stream = new PacketBuilder(manager == null ? 3 : manager.getDataBlock().length + 3);
		PacketBuilder stream = new PacketBuilder(12, PacketType.VAR_SHORT);
		if (manager != null) {
			stream.writeBytes(manager.getDataBlock());
		}
		session.write(stream);
	}
	
	public void sendFriend(String username, String displayName, int world, boolean putOnline, boolean warnMessage) {
		PacketBuilder stream = new PacketBuilder(85, PacketType.VAR_SHORT);
		stream.writeByte(warnMessage ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(displayName.equals(username) ? "" : username);
		stream.writeShort(putOnline ? world : 0);
		stream.writeByte(player.getContactManager().getRank(Misc.formatPlayerNameForProtocol(username)));
		stream.writeByte(0);
		if (putOnline) {
			stream.writeString(GameConstants.SERVER_NAME);
			stream.writeByte(0);
		}
		session.write(stream);
	}
	
	public void sendIgnore(String name, String display) {
		PacketBuilder stream = new PacketBuilder(75, PacketType.VAR_BYTE);
		stream.writeByte(0);
		if (display == name) {
			name = "";
		}
		stream.writeString(display);
		stream.writeString(name);
		stream.writeString(name);
		stream.writeString(display);
		session.write(stream);
	}
	
	public void sendPrivateMessage(String username, String message) {
		PacketBuilder stream = new PacketBuilder(77, PacketType.VAR_SHORT);
		byte[] encryptedData = new byte[message.length() + 1];
		TextUtils.huffmanCompress(message, encryptedData, 0);
		
		stream.writeString(username);
		stream.writeByte(message.length());
		stream.writeBytes(encryptedData);
		
		session.write(stream);
	}
	
	public void sendGameBarStages() {
		sendConfig(1054, 0); // clan on
		sendConfig(1055, 0); // assist on
		sendConfig(1056, player.getAttributes().isFilterGame() ? 1 : 0);
		sendConfig(2159, 0); // friends chat on
		PacketBuilder stream = new PacketBuilder(72);
		stream.writeByte(0); // public on
		stream.writeByte(0); // trade on
		session.write(stream);
		sendPrivateGameBarStage();
	}
	
	public void sendPrivateGameBarStage() {
		PacketBuilder stream2 = new PacketBuilder(134);
		stream2.writeByte(player.getContactManager().getPrivateStatus());
		session.write(stream2);
	}
	
	public void sendConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			sendConfig2(id, value);
		} else {
			sendConfig1(id, value);
		}
	}
	
	private void sendConfig1(int id, int value) {
		PacketBuilder stream = new PacketBuilder(101);
		stream.writeShort(id);
		stream.writeByte128(value);
		session.write(stream);
	}
	
	private void sendConfig2(int id, int value) {
		PacketBuilder stream = new PacketBuilder(39);
		stream.writeIntV2(value);
		stream.writeShort128(id);
		session.write(stream);
	}
	
	public void receivePrivateMessage(String name, String display, int rights, String message) {
		PacketBuilder stream = new PacketBuilder(120, PacketType.VAR_SHORT);
		byte[] encryptedData = new byte[message.length() + 1];
		encryptedData[0] = (byte) message.length();
		TextUtils.huffmanCompress(message, encryptedData, 1);
		
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Misc.getRandom(255));
		}
		stream.writeByte(rights);
		stream.writeBytes(encryptedData);
		session.write(stream);
	}
	
	public void receivePrivateChatQuickMessage(String name, String display, int rights, QuickChatMessage message) {
		PacketBuilder stream = new PacketBuilder(42, PacketType.VAR_BYTE);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Misc.getRandom(255));
		}
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		session.write(stream);
	}
	
	public void sendPrivateQuickMessageMessage(String username, QuickChatMessage message) {
		PacketBuilder stream = new PacketBuilder(97, PacketType.VAR_BYTE);
		stream.writeString(username);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		session.write(stream);
	}
	
	public void receiveFriendChatMessage(String name, String display, int rights, String chatName, ChatMessage message) {
		PacketBuilder stream = new PacketBuilder(40, PacketType.VAR_BYTE);
		String msg = message.getMessage(player.getAttributes().isFilteringProfanity());
		byte[] encryptedData = new byte[msg.length() + 1];
		encryptedData[0] = (byte) msg.length();
		TextUtils.huffmanCompress(msg, encryptedData, 1);
		
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		stream.writeLong(Misc.stringToLong(chatName));
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Misc.getRandom(255));
		}
		stream.writeByte(rights);
		
		stream.writeBytes(encryptedData);
		
		session.write(stream);
	}
	
	public void receiveFriendChatQuickMessage(String name, String display, int rights, String chatName, QuickChatMessage message) {
		PacketBuilder stream = new PacketBuilder(20, PacketType.VAR_BYTE);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display)) {
			stream.writeString(name);
		}
		stream.writeLong(Misc.stringToLong(chatName));
		for (int i = 0; i < 5; i++) {
			stream.writeByte(Misc.getRandom(255));
		}
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage() != null) {
			stream.writeBytes(message.getMessage().getBytes());
		}
		session.write(stream);
	}
	
	public void sendUnlockIgnoreList() {
		PacketBuilder stream = new PacketBuilder(135);
		session.write(stream);
	}
	
	public void sendUnlockFriendList() {
		PacketBuilder stream = new PacketBuilder(85, PacketType.VAR_SHORT);
		session.write(stream);
	}
	
	/*
	 * dynamic map region
	 */
	public void sendDynamicMapRegion(boolean wasAtDynamicRegion) {
		PacketBuilder stream = new PacketBuilder(128, PacketType.VAR_SHORT);
		int regionX = player.getChunkX();
		int regionY = player.getChunkY();
		stream.writeShort(regionY);
		stream.writeByte(player.getMapSize());
		stream.write128Byte(player.getAttributes().isForceNextMapLoadRefresh() ? 1 : 0);
		stream.write128Byte(wasAtDynamicRegion ? 5 : 3); // 5 or 3 else doesnt
		// load.
		stream.writeShortLE(regionX);
		stream.startBitAccess();
		int mapHash = NetworkConstants.MAP_SIZES[player.getMapSize()] >> 4;
		int[] realRegionIds = new int[4 * mapHash * mapHash];
		int realRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int thisRegionX = (regionX - mapHash); thisRegionX <= ((regionX + mapHash)); thisRegionX++) { // real
				// x
				// calcs
				for (int thisRegionY = (regionY - mapHash); thisRegionY <= ((regionY + mapHash)); thisRegionY++) { // real
					// y
					// calcs
					int regionId = (((thisRegionX / 8) << 8) + (thisRegionY / 8));
					Region region = RegionManager.getRegion(regionId);
					int realRegionX;
					int realRegionY;
					int realPlane;
					int rotation;
					if (region instanceof DynamicRegion) { // generated map
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] regionCoords = dynamicRegion.getRegionCoords()[plane][thisRegionX - ((thisRegionX / 8) * 8)][thisRegionY - ((thisRegionY / 8) * 8)];
						realRegionX = regionCoords[0];
						realRegionY = regionCoords[1];
						realPlane = regionCoords[2];
						rotation = regionCoords[3];
					} else { // real map
						// base region + difference * 8 so gets real region
						// coords
						realRegionX = thisRegionX;
						realRegionY = thisRegionY;
						realPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid region, not built region
					if (realRegionX == 0 || realRegionY == 0) {
						stream.writeBits(1, 0);
					} else {
						stream.writeBits(1, 1);
						stream.writeBits(26, (rotation << 1) | (realPlane << 24) | (realRegionX << 14) | (realRegionY << 3));
						int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
						boolean found = false;
						for (int index = 0; index < realRegionIdsCount; index++) {
							if (realRegionIds[index] == realRegionId) {
								found = true;
								break;
							}
						}
						if (!found) {
							realRegionIds[realRegionIdsCount++] = realRegionId;
						}
					}
					
				}
			}
		}
		stream.finishBitAccess();
		for (int index = 0; index < realRegionIdsCount; index++) {
			int[] xteas = MapArchiveKeys.getKey(realRegionIds[index]);
			if (xteas == null) {
				xteas = new int[4];
			}
			for (int keyIndex = 0; keyIndex < 4; keyIndex++) {
				stream.writeInt(xteas[keyIndex]);
			}
		}
		session.write(stream);
	}
	
	/*
	 * normal map region
	 */
	public void sendMapRegion(boolean sendLswp) {
		PacketBuilder stream = new PacketBuilder(43, PacketType.VAR_SHORT);
		if (sendLswp) {
			player.getLocalPlayerUpdate().init(stream);
		}
		stream.writeByteC(player.getMapSize());
		stream.writeByte(player.getAttributes().isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeShortLE(player.getChunkX());
		stream.writeShort(player.getChunkY());
		for (int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapArchiveKeys.getKey(regionId);
			if (xteas == null) {
				xteas = new int[4];
			}
			for (int index = 0; index < 4; index++) {
				stream.writeInt(xteas[index]);
			}
		}
		session.write(stream);
	}
	
	public void sendCutscene(int id) {
		PacketBuilder stream = new PacketBuilder(132, PacketType.VAR_SHORT);
		stream.writeShort(id);
		stream.writeShort(20); // xteas count
		for (int count = 0; count < 20; count++) {
			for (int i = 0; i < 4; i++) {
				stream.writeInt(0);
			}
		}
		byte[] appearence = player.getAppearance().getAppearanceData();
		stream.writeByte(appearence.length);
		stream.writeBytes(appearence);
		session.write(stream);
	}
	
	/*
	 * sets the pane interface
	 */
	public void sendWindowsPane(int id, int type) {
		player.getInterfaceManager().setWindowsPane(id);
		PacketBuilder stream = new PacketBuilder(67);
		stream.writeShortLE128(id);
		stream.write128Byte(type);
		session.write(stream);
	}
	
	public void sendPlayerOption(String option, int slot, boolean top) {
		sendPlayerOption(option, slot, top, -1);
	}
	
	public void sendPlayerOption(String option, int slot, boolean top, int cursor) {
		PacketBuilder stream = new PacketBuilder(1, PacketType.VAR_BYTE);
		stream.writeByte128(top ? 1 : 0);
		stream.writeShortLE(cursor);
		stream.writeString(option);
		stream.writeByteC(slot);
		session.write(stream);
	}
	
	public void sendPublicMessage(Player p, PublicChatMessage message) {
		PacketBuilder stream = new PacketBuilder(91, PacketType.VAR_BYTE);
		stream.writeShort(p.getIndex());
		stream.writeShort(message.getEffects());
		stream.writeByte(p.getMessageIcon());
		if (message instanceof QuickChatMessage) {
			QuickChatMessage qcMessage = (QuickChatMessage) message;
			stream.writeShort(qcMessage.getFileId());
			if (qcMessage.getMessage(false) != null) {
				stream.writeBytes(message.getMessage(false).getBytes());
			}
		} else {
			byte[] chatStr = new byte[250];
			chatStr[0] = (byte) message.getMessage(player.getAttributes().isFilteringProfanity()).length();
			int offset = 1 + Huffman.encryptMessage(1, message.getMessage(player.getAttributes().isFilteringProfanity()).length(), chatStr, 0, message.getMessage(player.getAttributes().isFilteringProfanity()).getBytes());
			stream.writeBytes(chatStr, 0, offset);
		}
		session.write(stream);
	}
	
	public void sendLocalPlayersUpdate() {
		session.write(player.getLocalPlayerUpdate().createPacketAndProcess());
	}
	
	public void sendLocalNPCsUpdate() {
		session.write(player.getLocalNPCUpdate().createPacketAndProcess());
	}
	
	public void sendGraphics(Graphics graphics, Object target) {
		PacketBuilder stream = new PacketBuilder(108);
		int hash = 0;
		if (target instanceof WorldTile) {
			WorldTile tile = (WorldTile) target;
			hash = tile.getPlane() << 28 | tile.getX() << 14 | tile.getY() & 0x3fff | 1 << 30;
		} else if (target instanceof Player) {
			Player p = (Player) target;
			hash = p.getIndex() & 0xffff | 1 << 28;
		} else {
			NPC n = (NPC) target;
			hash = n.getIndex() & 0xffff;
		}
		stream.writeShort128(graphics.getSpeed());
		stream.writeIntV2(hash);
		stream.writeByte128(0); // slot id used for entitys
		stream.writeByte128(graphics.getSettings2Hash());
		stream.writeShort(graphics.getHeight());
		stream.writeShortLE(graphics.getId());
		session.write(stream);
	}
	
	public void sendInterface(boolean nocliped, int windowId, int windowComponentId, int interfaceId) {
		if (!(windowId == 752 && (windowComponentId == 9 || windowComponentId == 12))) {
			if (player.getInterfaceManager().containsInterface(windowComponentId, interfaceId)) {
				closeInterface(windowComponentId);
			}
			if (!player.getInterfaceManager().addInterface(windowId, windowComponentId, interfaceId)) {
				System.out.println("Error adding interface: " + windowId + " , " + windowComponentId + " , " + interfaceId);
				return;
			}
		}
		PacketBuilder stream = new PacketBuilder(5);
		stream.writeShortLE128(interfaceId);
		stream.writeIntLE(windowId << 16 | windowComponentId);
		stream.writeByte(nocliped ? 1 : 0);
		session.write(stream);
	}
	
	public void closeInterface(int windowComponentId) {
		closeInterface(player.getInterfaceManager().getTabWindow(windowComponentId), windowComponentId);
		player.getInterfaceManager().removeTab(windowComponentId);
	}
	
	public void closeInterface(int windowId, int windowComponentId) {
		PacketBuilder stream = new PacketBuilder(73);
		stream.writeIntLE(windowId << 16 | windowComponentId);
		session.write(stream);
	}
	
	public void sendSystemUpdate(int delay) {
		PacketBuilder stream = new PacketBuilder(125);
		stream.writeShort((int) (delay * 1.6));
		session.write(stream);
	}
	
	public void sendUpdateItems(int key, ItemsContainer<Item> items, int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
	}
	
	public void sendUpdateItems(int key, Item[] items, int... slots) {
		sendUpdateItems(key, key < 0, items, slots);
	}
	
	public void sendUpdateItems(int key, boolean negativeKey, Item[] items, int... slots) {
		PacketBuilder stream = new PacketBuilder(80, PacketType.VAR_SHORT);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (int slotId : slots) {
			if (slotId >= items.length) {
				continue;
			}
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255) {
					stream.writeInt(amount);
				}
			}
		}
		session.write(stream);
	}
	
	public void sendGlobalString(int id, String string) {
		PacketBuilder stream = new PacketBuilder(54, PacketType.VAR_BYTE);
		stream.writeShortLE128(id);
		stream.writeString(string);
		session.write(stream);
	}
	
	public void sendLogout(boolean lobby) {
		PacketBuilder stream = new PacketBuilder(lobby ? 59 : 51);
		ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}
	
	public void sendPanelBoxMessage(String text) {
		sendMessage(99, text, null);
	}
	
	public void sendTradeRequestMessage(Player p) {
		sendMessage(100, "wishes to trade with you.", p);
	}
	
	public void sendClanWarsRequestMessage(Player p) {
		sendMessage(101, "wishes to challenge your clan to a clan war.", p);
	}
	
	public void sendDuelChallengeRequestMessage(Player p, boolean friendly) {
		sendMessage(101, "wishes to duel with you(" + (friendly ? "friendly" : "stake") + ").", p);
	}
	
	public void sendVoice(int id) {
		resetSounds();
		sendSound(id, 0, 2);
	}
	
	public void resetSounds() {
		PacketBuilder stream = new PacketBuilder(142);
		session.write(stream);
	}
	
	public void sendSound(int id, int delay, int effectType) {
		if (effectType == 1) {
			sendIndex14Sound(id, delay);
		} else if (effectType == 2) {
			sendIndex15Sound(id, delay);
		}
	}
	
	public void sendIndex14Sound(int id, int delay) {
		PacketBuilder stream = new PacketBuilder(106);
		stream.writeShort(id);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		stream.writeShort(256);
		session.write(stream);
	}
	
	public void sendIndex15Sound(int id, int delay) {
		PacketBuilder stream = new PacketBuilder(121);
		stream.writeShort(id);
		stream.writeByte(1); // amt of times it repeats
		stream.writeShort(delay);
		stream.writeByte(255); // volume
		session.write(stream);
	}
	
	public void sendMusicEffect(int id) {
		PacketBuilder stream = new PacketBuilder(0);
		stream.writeShort128(id);
		stream.write24BitInteger(0);
		stream.writeByteC(255); // volume
		session.write(stream);
	}
	
	public void sendMusic(int id) {
		sendMusic(id, 100, 255);
	}
	
	public void sendMusic(int id, int delay, int volume) {
		PacketBuilder stream = new PacketBuilder(31);
		stream.write128Byte(delay);
		stream.writeShortLE(id);
		stream.writeByteC(volume);
		session.write(stream);
	}
	
	public void sendSkillLevel(int skill) {
		PacketBuilder stream = new PacketBuilder(93);
		stream.write128Byte(player.getSkills().getLevel(skill));
		stream.writeByte128(skill);
		stream.writeIntLE((int) player.getSkills().getXp(skill));
		session.write(stream);
	}
	
	public void sendBlackOut(int area) {
		PacketBuilder out = new PacketBuilder(68);
		out.writeByte(area);
		session.write(out);
	}
	
	// instant
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}
	
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ, int speed1, int speed2) {
		PacketBuilder stream = new PacketBuilder(127);
		stream.writeByteC(viewLocalY);
		stream.writeShortLE128(viewZ >> 2);
		stream.write128Byte(viewLocalX);
		stream.writeByte(speed1);
		stream.write128Byte(speed2);
		session.write(stream);
	}
	
	public void sendResetCamera() {
		PacketBuilder stream = new PacketBuilder(10);
		session.write(stream);
	}
	
	public void sendCameraRotation(int unknown1, int unknown2) {
		PacketBuilder stream = new PacketBuilder(107);
		stream.writeShortLE128(unknown1);
		stream.writeShort128(unknown1);
		session.write(stream);
	}
	
	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}
	
	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ, int speed1, int speed2) {
		PacketBuilder stream = new PacketBuilder(29);
		stream.write128Byte(speed1);
		stream.writeByteC(moveLocalY);
		stream.writeByteC(moveLocalX);
		stream.writeByteC(speed2);
		stream.writeShortLE128(moveZ >> 2);
		session.write(stream);
	}
	
	/**
	 * Sends the grand exchange progress bar appropriately with the information provided
	 *
	 * @param slot
	 * 		The slot of the grand exchange offer
	 * @param progress
	 * 		The progress bar Id
	 * @param item
	 * 		The item identification number used to display the item
	 * @param price
	 * 		The price of the item.
	 * @param amountSold
	 * 		The progress percentage of the offer being completed
	 */
	public void sendGrandExchangeBar(int slot, int progress, int item, int price, int amountOffered, int amountSold) {
		PacketBuilder output = new PacketBuilder(53);
		output.writeByte(slot);
		output.writeByte(progress);
		output.writeShort(item);
		output.writeInt(price);
		output.writeInt(amountOffered);
		output.writeInt(amountSold);
		output.writeInt(price * amountSold);
		session.write(output);
	}
	
	public void sendInterFlashScript(int interfaceId, int componentId, int width, int height, int slot) {
		Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(143, parameters);
	}
	
	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, boolean negativeKey, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--) {
			parameters[index++] = options[count];
		}
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters); // scriptid 150 does
		// that the method
		// name says*/
	}
	
	public void sendAccessMask(int min, int max, int interfaceId, int childId, int hash) {
		PacketBuilder stream = new PacketBuilder();
		stream.writeIntV2(hash);
		stream.writeInt(interfaceId << 16 | childId);
		stream.writeShort128(min);
		stream.writeShortLE(max);
		session.write(stream);
	}
	
	public void sendInputNameScript(String message) {
		sendRunScript(109, message);
	}
	
	public void sendInputIntegerScript(boolean integerEntryOnly, String message) {
		sendRunScript(108, message);
	}
	
	public void sendInputLongTextScript(String message) {
		sendRunScript(110, message);
	}
	
	public void sendWorldList(boolean full) {
		PacketBuilder packet = new PacketBuilder(88, PacketType.VAR_SHORT);
		packet.writeByte(1);// This was 0
		packet.writeByte(2);
		packet.writeByte(full ? 1 : 0);
		int size = WorldList.INSTANCE.getWorlds().size();
		if (full) {
			packet.writeSmart(size);
			for (WorldEntry world : WorldList.INSTANCE.getWorlds().values()) {
				packet.writeSmart(world.getCountryId());
				packet.writeString(world.getCountryName());
			}
			packet.writeSmart(0);
			packet.writeSmart(size + 1);
			packet.writeSmart(size);
			for (int world = 1; world <= WorldList.INSTANCE.getWorlds().size(); world++) {
				packet.writeSmart(world); // wid
				packet.writeByte(0); // loc (idx in list) ^ KEEP THIS 0
				packet.writeInt(WorldList.INSTANCE.getWorlds().get(world).getFlag());
				packet.writeString(WorldList.INSTANCE.getWorlds().get(world).getActivity()); // activity
				packet.writeString(WorldList.INSTANCE.getWorlds().get(world).getIp()); // ip
			}
			packet.writeInt(0x94DA4A87);
		}
		for (int world = 1; world <= WorldList.INSTANCE.getWorlds().size(); world++) {
			packet.writeSmart(world); // wid
			packet.writeShort(1337/*WorldList.getWorlds().get(world)*/);
		}
		session.write(packet);
	}
	
	public void sendMinimapFlag(int x, int y) {
		PacketBuilder stream = new PacketBuilder(55);
		stream.writeByte(x);
		stream.writeByte128(y);
		session.write(stream);
	}
	
	public void sendResetMinimapFlag() {
		PacketBuilder stream = new PacketBuilder(55);
		stream.writeByte(255);
		stream.writeByte128(255);
		session.write(stream);
	}
	
	public void requestClientInput(InputEvent event) {
		sendRunScript(event.getType().getScriptId(), event.getText());
		player.putTemporaryAttribute("input_event", event);
	}
	
	public void cancelInputRequest() {
		sendRunScript(1548, 0);
		player.removeTemporaryAttribute("input_event");
	}
	
}
